package seers.bugreppatterns.main.coocurrence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.bugrepcompl.entity.CodedDataEntry;
import seers.bugrepcompl.utils.DataReader;
import seers.bugreppatterns.goldset.TextInstance;
import seers.bugreppatterns.main.validation.MainMatcher;
import seers.bugreppatterns.utils.JavaUtils;

public class MainCoOcurrenceAnalysis {

	static ConcurrentHashMap<TextInstance, Set<String>> sentenceCooccurrences = new ConcurrentHashMap<>();
	static ConcurrentHashMap<TextInstance, Set<String>> paragraphCooccurrences = new ConcurrentHashMap<>();
	static ConcurrentHashMap<TextInstance, Set<String>> bugsCooccurrences = new ConcurrentHashMap<>();

	static final Set<String> PATTERNS_TO_AVOID = JavaUtils.getSet("S_EB_CAN", "S_EB_IMPERATIVE");
//	static final Set<String> PATTERNS_TO_AVOID = JavaUtils.getSet();

	public static void main(String[] args) throws Exception {

		String dataFilePath = MainMatcher.fileCodedData;

		List<CodedDataEntry> codedData = DataReader.readCodedData(dataFilePath);
		for (CodedDataEntry dataEntry : codedData) {

			String project = dataEntry.project;
			String bugId = dataEntry.bugId;
			String instanceId = dataEntry.instanceId;

			// no project or bug id
			if (project.trim().isEmpty() || bugId.trim().isEmpty()) {
				continue;
			}

			// no titles
			if (instanceId.startsWith("0")) {
				continue;
			}

			instanceId = instanceId.replace(",", ".");
			boolean isSentence = instanceId.matches("\\d+\\.\\d+");

			TextInstance txtInstance = new TextInstance(project, bugId, instanceId);

			// if sentence
			if (isSentence) {

				/// ------------------------------------
				// sentences

				addSentencePatterns(txtInstance, dataEntry);
			} else {

				/// ------------------------------------
				// paragraphs
				addParagraphPatterns(txtInstance, dataEntry);
			}

		}

		writeCoocurrence(sentenceCooccurrences, "test_data" + File.separator + "coocurrence-S.csv");
		writeCoocurrence(paragraphCooccurrences, "test_data" + File.separator + "coocurrence-P.csv");
		writeCoocurrence(bugsCooccurrences, "test_data" + File.separator + "coocurrence-B.csv");

	}

	private static void writeCoocurrence(ConcurrentHashMap<TextInstance, Set<String>> cooccurrenceSet,
			String outputFilePath) throws IOException {

		try (CsvWriter writer = new CsvWriterBuilder(new FileWriter(outputFilePath)).separator(';').build()) {

			for (Entry<TextInstance, Set<String>> entry : cooccurrenceSet.entrySet()) {
				TextInstance instance = entry.getKey();

				List<String> patterns = new ArrayList<>(entry.getValue());
				patterns.sort((p1, p2) -> p1.compareTo(p2));

				List<String> line = new ArrayList<>();
				line.add(instance.getProject());
				line.add(instance.getBugId());
				line.add(instance.getInstanceId());
				// line.addAll(patterns);
				line.add(patterns.toString());
				line.add(patterns.stream().filter(p -> p.contains("_OB_")).collect(Collectors.toList()).toString());
				line.add(patterns.stream().filter(p -> p.contains("_EB_")).collect(Collectors.toList()).toString());
				line.add(patterns.stream().filter(p -> p.contains("_SR_")).collect(Collectors.toList()).toString());

				writer.writeNext(line);
			}
		}

	}

	private static void addParagraphPatterns(TextInstance txtInstance, CodedDataEntry dataEntry) {

		addCooccurrence(txtInstance, dataEntry, paragraphCooccurrences);

		// update bug co-occurrence

		TextInstance bugInstance = new TextInstance(txtInstance.getProject(), txtInstance.getBugId(), "0");

		addBugPatterns(bugInstance, dataEntry);

	}

	private static void addBugPatterns(TextInstance txtInstance, CodedDataEntry dataEntry) {

		addCooccurrence(txtInstance, dataEntry, bugsCooccurrences);

	}

	private static void addSentencePatterns(TextInstance txtInstance, CodedDataEntry dataEntry) {
		addCooccurrence(txtInstance, dataEntry, sentenceCooccurrences);

		// update paragraph co-occurrence

		String pararagraphId = txtInstance.getInstanceId().split("\\.")[0];
		TextInstance paragraphInstance = new TextInstance(txtInstance.getProject(), txtInstance.getBugId(),
				pararagraphId);

		addParagraphPatterns(paragraphInstance, dataEntry);

	}

	private static void addCooccurrence(TextInstance txtInstance, CodedDataEntry dataEntry,
			ConcurrentHashMap<TextInstance, Set<String>> cooccurrenceSet) {

		Set<String> patterns = cooccurrenceSet.get(txtInstance);
		if (patterns == null) {
			patterns = new LinkedHashSet<>();
			cooccurrenceSet.put(txtInstance, patterns);
		}

		if (!dataEntry.pattern1.trim().isEmpty() && !PATTERNS_TO_AVOID.contains(dataEntry.pattern1.trim())) {
			patterns.add(dataEntry.pattern1.trim());
		}
		if (!dataEntry.pattern2.trim().isEmpty() && !PATTERNS_TO_AVOID.contains(dataEntry.pattern2.trim())) {
			patterns.add(dataEntry.pattern2.trim());
		}
		if (!dataEntry.pattern3.trim().isEmpty() && !PATTERNS_TO_AVOID.contains(dataEntry.pattern3.trim())) {
			patterns.add(dataEntry.pattern3.trim());
		}
		if (!dataEntry.pattern4.trim().isEmpty() && !PATTERNS_TO_AVOID.contains(dataEntry.pattern4.trim())) {
			patterns.add(dataEntry.pattern4.trim());
		}
	}
}
