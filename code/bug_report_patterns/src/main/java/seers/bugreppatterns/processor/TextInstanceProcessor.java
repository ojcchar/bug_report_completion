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
import seers.textanalyzer.entity.Token;

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
		nextLine.add(system);
		nextLine.add(bugRepId);
		nextLine.add(instanceId);

		patternMatches.forEach((k, v) -> {
			nextLine.add(k.getCode() + ":" + v);
		});

		featuresWriter.writeNext(nextLine);
	}

	protected Paragraph parseParagraph(String bugId, DescriptionParagraph paragraph) {

		Paragraph par = new Paragraph(paragraph.getId());

		List<DescriptionSentence> elements = paragraph.getSentences();
		if (elements != null) {
			for (DescriptionSentence descSentence : elements) {

				String sentenceTxt = descSentence.getValue();
				List<Sentence> sentences = TextProcessor.processText(sentenceTxt);

				if (!sentences.isEmpty()) {
					List<Token> allTokens = TextProcessor.getAllTokens(sentences);

					Sentence sentence = new Sentence(descSentence.getId(), allTokens);

					par.addSentence(sentence);
				}

			}
		}

		return par;
	}

}
