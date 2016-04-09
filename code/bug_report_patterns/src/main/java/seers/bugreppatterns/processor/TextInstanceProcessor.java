package seers.bugreppatterns.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import net.quux00.simplecsv.CsvWriter;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.entity.xml.DescriptionParagraph;
import seers.bugreppatterns.entity.xml.DescriptionSentence;
import seers.bugreppatterns.main.MainHRClassifier;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public abstract class TextInstanceProcessor extends ThreadProcessor {

	protected String dataFolder;
	protected CsvWriter predictionWriter;
	protected List<PatternMatcher> patterns;
	protected List<File> files;
	protected String granularity;
	protected String system;
	private CsvWriter featuresWriter;

	public TextInstanceProcessor(ThreadParameters params) {
		super(params);

		dataFolder = params.getStringParam(MainHRClassifier.DATA_FOLDER);
		predictionWriter = params.getParam(CsvWriter.class, MainHRClassifier.PREDICTION_WRITER);
		patterns = params.getListParam(PatternMatcher.class, MainHRClassifier.PATTERNS);
		files = params.getListParam(File.class, ThreadExecutor.ELEMENTS_PARAM);
		granularity = params.getStringParam(MainHRClassifier.GRANULARITY);
		system = params.getStringParam(MainHRClassifier.SYSTEM);
		featuresWriter = params.getParam(CsvWriter.class, MainHRClassifier.FEATURES_WRITER);
	}

	protected void writePrediction(String bugRepId, String instanceId,
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

		List<String> nextLine = Arrays
				.asList(new String[] { system + "-" + bugRepId + "-" + instanceId, isEB.toString(), isSR.toString() });
		predictionWriter.writeNext(nextLine);
	}

	protected void writeFeatures(String bugRepId, String instanceId,
			LinkedHashMap<PatternMatcher, Integer> patternMatches) {
		List<String> nextLine = new ArrayList<>();
		nextLine.add(system + "-" + bugRepId + "-" + instanceId);

		patternMatches.forEach((k, v) -> {
			nextLine.add(k.getCode() + ":" + v);
		});

		featuresWriter.writeNext(nextLine);
	}

	protected Paragraph parseParagraph(String bugId, DescriptionParagraph paragraph) {

		Paragraph par = new Paragraph(paragraph.getId());

		List<DescriptionSentence> elements = paragraph.getSentences();
		if (elements != null) {
			for (DescriptionSentence textElement : elements) {

				List<Sentence> sentences = TextProcessor.processText(textElement.getValue());

				// if (sentences.size() > 1) {
				// LOGGER.warn("[" + system + "] Sentence " +
				// textElement.getId() +
				// " of bug " + bugId
				// + " can be processed into multiple sentences!");
				// }
				if (!sentences.isEmpty()) {
					par.addSentence(sentences.get(0));
				}

			}
		}

		return par;
	}

}
