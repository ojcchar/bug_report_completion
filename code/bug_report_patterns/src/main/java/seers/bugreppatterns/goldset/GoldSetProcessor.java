package seers.bugreppatterns.goldset;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.bugreppatterns.pattern.predictor.Labels;

public class GoldSetProcessor extends ThreadProcessor {

	public static ConcurrentHashMap<TextInstance, Labels> goldSetBugs = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<TextInstance, Labels> goldSetParagraphs = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<TextInstance, Labels> goldSetSentences = new ConcurrentHashMap<>();
	private List<List<String>> sentences;

	@SuppressWarnings("unchecked")
	public GoldSetProcessor(ThreadParameters params) {
		super(params);
		sentences = params.getParam(List.class, ThreadExecutor.ELEMENTS_PARAM);
	}

	@Override
	public void executeJob() throws Exception {
		for (List<String> sentence : sentences) {

			if (sentence.isEmpty() || isLineEmpty(sentence)) {
				continue;
			}

			String project = sentence.get(0);
			String bugId = sentence.get(1);
			String instanceId = sentence.get(14);

			// no titles
			if (instanceId.startsWith("0")) {
				continue;
			}

			String ob = sentence.get(5);
			String eb = sentence.get(6);
			String sr = sentence.get(7);

			TextInstance ins = new TextInstance(project, bugId, instanceId);
			Labels label = new Labels(ob.toLowerCase(), eb.toLowerCase(), sr.toLowerCase());

			boolean isSentence = instanceId.contains(".");

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

	private boolean isLineEmpty(List<String> sentence) {

		for (String field : sentence) {
			if (!field.trim().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static class TextInstance {
		String project;
		String bugId;
		String instanceId;

		public TextInstance(String project, String bugId, String instanceId) {
			super();
			this.project = project;
			this.bugId = bugId;
			this.instanceId = instanceId;
		}

		public String getProject() {
			return project;
		}

		public String getBugId() {
			return bugId;
		}

		public String getInstanceId() {
			return instanceId;
		}

		public void setProject(String project) {
			this.project = project;
		}

		public void setBugId(String bugId) {
			this.bugId = bugId;
		}

		public void setInstanceId(String instanceId) {
			this.instanceId = instanceId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((bugId == null) ? 0 : bugId.hashCode());
			result = prime * result + ((instanceId == null) ? 0 : instanceId.hashCode());
			result = prime * result + ((project == null) ? 0 : project.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TextInstance other = (TextInstance) obj;
			if (bugId == null) {
				if (other.bugId != null)
					return false;
			} else if (!bugId.equals(other.bugId))
				return false;
			if (instanceId == null) {
				if (other.instanceId != null)
					return false;
			} else if (!instanceId.equals(other.instanceId))
				return false;
			if (project == null) {
				if (other.project != null)
					return false;
			} else if (!project.equals(other.project))
				return false;
			return true;
		}

	}

}