package seers.bugreppatterns.processor;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import net.quux00.simplecsv.CsvWriter;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.appcore.xml.XMLHelper;
import seers.bugreppatterns.entity.paragraph.BugReport;
import seers.bugreppatterns.entity.paragraph.DescriptionSentence;
import seers.bugreppatterns.main.MainHRClassifier;
import seers.bugreppatterns.pattern.PatternMatcher;

public class SentenceProcessor extends ThreadProcessor {

	private String dataFolder;
	private CsvWriter wr;
	private List<PatternMatcher> patterns;
	private List<File> files;
	private String granularity;
	private String system;

	public SentenceProcessor(ThreadParameters params) {
		super(params);

		dataFolder = params.getStringParam(MainHRClassifier.DATA_FOLDER);
		wr = params.getParam(CsvWriter.class, MainHRClassifier.OUT_WR);
		patterns = params.getListParam(PatternMatcher.class, MainHRClassifier.PATTERNS);
		files = params.getListParam(File.class, ThreadExecutor.ELEMENTS_PARAM);
		granularity = params.getStringParam(MainHRClassifier.GRANULARITY);
		system = params.getStringParam(MainHRClassifier.SYSTEM);
	}

	@Override
	public void executeJob() throws Exception {

		for (File file : files) {

			LOGGER.debug(file.toString());

			BugReport bugRep = XMLHelper.readXML(BugReport.class, file);
			List<DescriptionSentence> elements = bugRep.getDescription().getAllSentences();

			for (DescriptionSentence textElement : elements) {

				LinkedHashMap<PatternMatcher, Integer> patternMatches = new LinkedHashMap<>();

				for (PatternMatcher patternMatcher : patterns) {
					boolean matchSentence = patternMatcher.matchSentence(textElement.getValue());
					if (matchSentence) {
						patternMatches.put(patternMatcher, 1);
					}
				}

				writeFeatures(textElement, patternMatches);
				writePrediction(bugRep.getId(), textElement, patternMatches);
			}
		}

	}

	private void writePrediction(String bugRepId, DescriptionSentence textElement,
			LinkedHashMap<PatternMatcher, Integer> patternMatches) {
		Integer isEB = 0;
		Integer isSR = 0;

		List<PatternMatcher> patterns = patternMatches.keySet().stream()
				.filter(p -> PatternMatcher.EB.equals(p.getType())).collect(Collectors.toList());
		if (!patterns.isEmpty()) {
			isEB = 1;
		}

		patterns = patternMatches.keySet().stream().filter(p -> PatternMatcher.SR.equals(p.getType()))
				.collect(Collectors.toList());
		if (!patterns.isEmpty()) {
			isSR = 1;
		}

		List<String> nextLine = Arrays.asList(
				new String[] { system, bugRepId, textElement.getId().toString(), isEB.toString(), isSR.toString() });
		wr.writeNext(nextLine);
	}

	private void writeFeatures(DescriptionSentence textElement, LinkedHashMap<PatternMatcher, Integer> patternMatches) {
		// TODO Auto-generated method stub

	}

}
