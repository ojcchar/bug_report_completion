package seers.bugreppatterns.goldset;

import java.io.File;
import java.util.List;

import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugreppatterns.entity.Document;
import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.entity.xml.BugReport;
import seers.bugreppatterns.pattern.predictor.Labels;
import seers.bugreppatterns.utils.ParsingUtils;
import seers.textanalyzer.entity.Sentence;

public class BugListProcessor extends ThreadProcessor {

	private String bugsDataFolder;
	private List<TextInstance> bugsList;

	public BugListProcessor(ThreadParameters params) {
		super(params);
		bugsDataFolder = params.getStringParam("bugsDataFolder");
		bugsList = params.getListParam(TextInstance.class, ThreadExecutor.ELEMENTS_PARAM);
	}

	@Override
	public void executeJob() throws Exception {

		for (TextInstance bugInstance : bugsList) {

			String system = bugInstance.getProject();
			String bugId = bugInstance.getBugId();

			// read xml
			String bugFile = bugsDataFolder + File.separator + system + "_parse" + File.separator + bugId
					+ ".xml.parse";
			BugReport bugRep = XMLHelper.readXML(BugReport.class, bugFile);
			Document bugRepDoc = ParsingUtils.parseDocument(system, bugRep);

			// read paragraphs
			List<Paragraph> paragraphs = bugRepDoc.getParagraphs();

			// for each paragraph
			for (Paragraph paragraph : paragraphs) {
				
				if (paragraph.isEmpty()) {
					continue;
				}

				// if not in the goldset add it, label are empty
				TextInstance instance = new TextInstance(system, bugId, paragraph.getId());
				Labels labels = GoldSetProcessor.goldSetParagraphs.get(instance);
				if (labels == null) {
					GoldSetProcessor.goldSetParagraphs.put(instance, new Labels());
				}
			}

			// read sentences
			List<Sentence> sentences = bugRepDoc.getSentences();

			// for each sentence
			for (Sentence sentence : sentences) {
				
				if (sentence.isEmpty()) {
					continue;
				}

				// if not in the goldset add it, label are empty
				TextInstance instance = new TextInstance(system, bugId, sentence.getId());
				Labels labels = GoldSetProcessor.goldSetSentences.get(instance);
				if (labels == null) {
					GoldSetProcessor.goldSetSentences.put(instance, new Labels());
				}
			}

		}

	}

}
