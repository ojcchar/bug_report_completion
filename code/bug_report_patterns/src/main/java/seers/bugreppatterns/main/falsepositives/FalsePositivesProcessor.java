package seers.bugreppatterns.main.falsepositives;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.regularparse.BugReport;
import seers.bugreppatterns.entity.Document;
import seers.bugreppatterns.main.falsepositives.FalsePositiveSummarizer.BugInstance;
import seers.bugreppatterns.utils.ParsingUtils;
import seers.textanalyzer.entity.Sentence;

public class FalsePositivesProcessor extends ThreadProcessor {

	@SuppressWarnings("rawtypes")
	private List<List> allLines;
	private ConcurrentHashMap<String, HashMap<BugInstance, List<String>>> predictions;
	private String[] patternTypes;
	private String[] inputPatterns;

	@SuppressWarnings("unchecked")
	public FalsePositivesProcessor(ThreadParameters params) {
		super(params);
		allLines = params.getListParam(List.class, ThreadExecutor.ELEMENTS_PARAM);
		predictions = params.getParam(ConcurrentHashMap.class, "predictions");
		patternTypes = params.getParam(String[].class, "patternTypes");
		inputPatterns = params.getParam(String[].class, "inputPatterns");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void executeJob() throws Exception {
		for (List<String> line : allLines) {

			String system = line.get(0);
			String bugId = line.get(1);

			// -----------------------------------------

			String sysBugIdKey = FalsePositiveSummarizer.getSysBugIdKey(system, bugId);

			HashMap<BugInstance, List<String>> sentences = predictions.get(sysBugIdKey);
			if (sentences == null) {
				sentences = new HashMap<>();
				predictions.put(sysBugIdKey, sentences);
			}

			// -----------------------------------------

			String instanceId = line.get(2);
			BugInstance instance = new FalsePositiveSummarizer.BugInstance(instanceId, "");

			List<String> patterns = sentences.get(instanceId);
			if (patterns == null) {
				patterns = new ArrayList<>();
				sentences.put(instance, patterns);

				setBugInstanceText(system, bugId, instanceId, instance);
			}

			// -----------------------------------------

			List<String> patternList = line.subList(3, line.size());

			for (int i = 0; i < patternList.size(); i += 2) {
				String pattern = patternList.get(i);
				if (!pattern.isEmpty() && matchPatternTypes(pattern, patternTypes, inputPatterns)) {
					patterns.add(pattern);
				}
			}
		}
	}

	private static HashMap<String, Document> bugReportsHash = new HashMap<>();

	private void setBugInstanceText(String system, String bugId, String instanceId, BugInstance instance)
			throws Exception {

		String sysBugIdKey = FalsePositiveSummarizer.getSysBugIdKey(system, bugId);
		Document bugRepDoc = bugReportsHash.get(sysBugIdKey);

		if (bugRepDoc == null) {
			String file = "test_data\\data\\" + system + "_parse\\" + bugId + ".xml.parse";
			BugReport bugRep = XMLHelper.readXML(BugReport.class, file);
			bugRepDoc = ParsingUtils.parseDocument(system, bugRep);
			bugReportsHash.put(sysBugIdKey, bugRepDoc);
		}

		if (instanceId.contains(".")) {
			Optional<Sentence> first = bugRepDoc.getSentences().stream().filter(sent -> sent.getId().equals(instanceId))
					.findFirst();
			instance.text = first.get().getText().replace("\n", "\\n");
		} else {
			final StringBuffer buffer = new StringBuffer();
			List<Sentence> sentences = bugRepDoc.getParagraphs().stream().filter(p -> p.getId().equals(instanceId))
					.findFirst().get().getSentences();
			sentences.forEach(s -> {
				buffer.append(s.getText());
				buffer.append(". ");
			});
			instance.text = buffer.toString().replace("\n", "\\n");
		}

	}

	private boolean matchPatternTypes(String pattern, String[] patternTypes, String[] inputPatterns2) {
		return Arrays.stream(patternTypes).anyMatch(pattType -> pattern.contains("_" + pattType + "_"))
				&& (inputPatterns2.length == 0
						|| Stream.of(inputPatterns2).anyMatch(inputPatt -> pattern.equals(inputPatt)));
	}

}
