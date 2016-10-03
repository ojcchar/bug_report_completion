package seers.bugreppatterns.processor;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.xml.XMLHelper;
import seers.bugreppatterns.entity.Document;
import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.entity.xml.BugReport;
import seers.bugreppatterns.entity.xml.DescriptionParagraph;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.Labels;

public class BugReportProcessor extends TextInstanceProcessor {

	public BugReportProcessor(ThreadParameters params) {
		super(params);
	}

	@Override
	public void executeJob() throws Exception {

		for (File file : files) {

			try {

				BugReport bugRep = XMLHelper.readXML(BugReport.class, file);
				Document bugReport = parseDocument(bugRep);
				LinkedHashMap<PatternMatcher, Integer> patternMatches = new LinkedHashMap<>();

				for (PatternMatcher patternMatcher : patterns) {
					int numMatches = patternMatcher.matchDocument(bugReport);
					if (numMatches > 0) {
						patternMatches.put(patternMatcher, numMatches);
					}
				}

				Labels labels = predictor.predictLabels(bugRep.getId(), "0", patternMatches, granularity);

				writeFeatures(bugRep.getId(), "0", patternMatches);
				writePrediction(bugRep.getId(), "0", labels);

			} catch (Exception e) {
				LOGGER.error("[" + system + "] Error for file: " + file + ", " + e.getClass().getSimpleName() + ": "
						+ e.getMessage(), e);
			}
		}
	}

	private Document parseDocument(BugReport bugRep) {
		Document doc = new Document(bugRep.getId());
		List<DescriptionParagraph> paragraphs = bugRep.getDescription().getParagraphs();
		for (DescriptionParagraph par : paragraphs) {
			Paragraph paragraph = parseParagraph(bugRep.getId(), par);
			if (paragraph.isEmpty()) {
				LOGGER.warn("[" + system + "] Bug " + bugRep.getId() + ", paragraph " + paragraph.getId()
						+ " is empty, skipping it!");
				continue;
			}
			doc.addParagraph(paragraph);
		}
		return doc;
	}

}
