package seers.bugreppatterns.goldset;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.bugrepcompl.entity.CodedDataEntry;
import seers.bugreppatterns.pattern.predictor.Labels;

public class GoldSetProcessor extends ThreadProcessor {

	public static ConcurrentHashMap<TextInstance, Labels> goldSetBugs = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<TextInstance, Labels> goldSetParagraphs = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<TextInstance, Labels> goldSetSentences = new ConcurrentHashMap<>();
	private List<CodedDataEntry> sentences;

	@SuppressWarnings("unchecked")
	public GoldSetProcessor(ThreadParameters params) {
		super(params);
		sentences = params.getParam(List.class, ThreadExecutor.ELEMENTS_PARAM);
	}

	@Override
	public void executeJob() throws Exception {
		for (CodedDataEntry sentence : sentences) {

			String project = sentence.project;
			String bugId = sentence.bugId;
			String instanceId = sentence.instanceId;

			// no project or bug id
			if (project.trim().isEmpty() || bugId.trim().isEmpty()) {
				continue;
			}

			// no titles
			if (instanceId.startsWith("0")) {
				continue;
			}

			boolean ob = sentence.isObsBehavior;
			boolean eb = sentence.isExpecBehavior;
			boolean sr = sentence.isStepsToRepro;

			instanceId = instanceId.replace(",", ".");

			TextInstance ins = new TextInstance(project, bugId, instanceId);
			Labels label = new Labels(ob ? "x" : "", eb ? "x" : "", sr ? "x" : "");

			boolean isSentence = instanceId.matches("\\d+\\.\\d+");

			// if sentence
			if (isSentence) {
				goldSetSentences.put(ins, label);

				String parId = ins.getInstanceId().split("\\.")[0];
				TextInstance ins2 = new TextInstance(ins.getProject(), ins.getBugId(), parId);

				updateParagraphGoldSet(ins2, label);
			} else {
				updateParagraphGoldSet(ins, label);
			}

		}
	}

	private void updateParagraphGoldSet(TextInstance ins, Labels label) {
		Labels labels = goldSetParagraphs.get(ins);
		if (labels == null) {
			labels = label;
		} else {
			labels = mergeLabels(labels, label);
		}

		goldSetParagraphs.put(ins, labels);

		TextInstance ins2 = new TextInstance(ins.getProject(), ins.getBugId(), "0");
		updateBugsGoldSet(ins2, label);
	}

	private void updateBugsGoldSet(TextInstance ins, Labels label) {

		Labels labels = goldSetBugs.get(ins);
		if (labels == null) {
			labels = label;
		} else {
			labels = mergeLabels(labels, label);
		}

		goldSetBugs.put(ins, labels);
	}

	private Labels mergeLabels(Labels labels, Labels label) {

		Labels labels2 = new Labels(labels);
		if (labels2.getIsOB().isEmpty()) {
			labels2.setIsOB(label.getIsOB());
		}
		if (labels2.getIsEB().isEmpty()) {
			labels2.setIsEB(label.getIsEB());
		}
		if (labels2.getIsSR().isEmpty()) {
			labels2.setIsSR(label.getIsSR());
		}
		return labels2;
	}

	

}
