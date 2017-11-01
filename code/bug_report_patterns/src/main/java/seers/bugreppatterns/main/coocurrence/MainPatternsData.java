package seers.bugreppatterns.main.coocurrence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.CodedDataEntry;
import seers.bugrepcompl.entity.PatternEntry;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.regularparse.ParsedBugReport;
import seers.bugrepcompl.utils.DataReader;
import seers.bugreppatterns.entity.Document;
import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.main.validation.MainMatcher;
import seers.bugreppatterns.utils.ParsingUtils;
import seers.textanalyzer.entity.Sentence;

public class MainPatternsData {

	public static String bugTypesFilePath = "test_data" + File.separator + "matcher" + File.separator + "bug_types.csv";
	public static String bugsDataFolder = "test_data" + File.separator + "data";

	static LinkedHashMap<PatternEntry2, Set<String>> sentenceOrderedPatterns = new LinkedHashMap<>();

	static LinkedHashSet<PatternEntry> bugEntries = new LinkedHashSet<>();
	static LinkedHashSet<PatternEntry> paragraphEntries = new LinkedHashSet<>();
	static LinkedHashSet<PatternEntry> sentencesEntries = new LinkedHashSet<>();

	static LinkedHashMap<TextInstance, Document> parsedBugs = new LinkedHashMap<>();

	private static HashMap<TextInstance, String> bugsType;

	public static void main(String[] args) throws Exception {

		String dataFilePath = MainMatcher.fileCodedData;

		List<CodedDataEntry> codedData = DataReader.readCodedData(dataFilePath);
		bugsType = DataReader.readTypeOfIssues(bugTypesFilePath);

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
			String bugType = bugsType.get(new TextInstance(txtInstance.getProject(), txtInstance.getBugId(), "0"));

			// --------------------------------------

			TextInstance bugInstance = new TextInstance(txtInstance.getProject(), txtInstance.getBugId(), "0");
			Document bugRepDoc = parsedBugs.get(bugInstance);
			if (bugRepDoc == null) {

				// read xml
				String bugFile = bugsDataFolder + File.separator + project + "_parse" + File.separator + bugId
						+ ".xml.parse";
				ParsedBugReport bugRep = XMLHelper.readXML(ParsedBugReport.class, bugFile);
				bugRepDoc = ParsingUtils.parseDocument(project, bugRep);

				parsedBugs.put(bugInstance, bugRepDoc);

			}

			// --------------------------------------

			// if sentence
			if (isSentence) {

				/// ------------------------------------
				// sentences

				addSentencePatterns(txtInstance, dataEntry, bugType, bugRepDoc);
			} else {

				/// ------------------------------------
				// paragraphs
				addParagraphPatterns(txtInstance, dataEntry, bugType, bugRepDoc);
			}
		}

		writeEntries(bugEntries, "test_data" + File.separator + "pattern-data-B.csv");
		writeEntries(paragraphEntries, "test_data" + File.separator + "pattern-data-P.csv");
		writeEntries(sentencesEntries, "test_data" + File.separator + "pattern-data-S.csv");

		writeEntries2(sentenceOrderedPatterns, "test_data" + File.separator + "pattern-data-ordered-S.csv");
	}

	private static void writeEntries2(LinkedHashMap<PatternEntry2, Set<String>> entries, String outputFilePath)
			throws IOException {

		try (CsvWriter writer = new CsvWriterBuilder(new FileWriter(outputFilePath)).separator(';').build()) {

			writer.writeNext(Arrays.asList(new String[] { "project", "bug_id", "instance_id", "order", "info_types" }));

			for (Entry<PatternEntry2, Set<String>> entry : entries.entrySet()) {

				List<String> line = new ArrayList<>();
				TextInstance instance = entry.getKey().instance;

				line.add(instance.getProject());
				line.add(instance.getBugId());
				line.add(instance.getInstanceId());
				line.add(String.valueOf(entry.getKey().order));

				ArrayList<String> list = new ArrayList<>(entry.getValue());
				list.sort((p1, p2) -> p1.compareTo(p2));

				line.add(list.toString());

				writer.writeNext(line);
			}
		}
	}

	private static void writeEntries(LinkedHashSet<PatternEntry> entries, String outputFilePath) throws IOException {

		try (CsvWriter writer = new CsvWriterBuilder(new FileWriter(outputFilePath)).separator(';').build()) {

			writer.writeNext(Arrays.asList(new String[] { "project", "bug_id", "instance_id", "pattern", "info_type",
					"pattern_type", "bug_type", "order" }));

			for (PatternEntry entry : entries) {

				List<String> line = new ArrayList<>();
				TextInstance instance = entry.instance;

				line.add(instance.getProject());
				line.add(instance.getBugId());
				line.add(instance.getInstanceId());
				line.add(entry.pattern);
				line.add(entry.infoType);
				line.add(entry.patternType);
				line.add(entry.bugType);
				line.add(String.valueOf(entry.order));

				writer.writeNext(line);
			}
		}

	}

	private static void addParagraphPatterns(TextInstance txtInstance, CodedDataEntry dataEntry, String bugType,
			Document bugRepDoc) {

		List<Paragraph> paragraphs = bugRepDoc.getParagraphs();
		int order = paragraphs.indexOf(new Paragraph(txtInstance.getInstanceId()));

		addPatternEntry(txtInstance, dataEntry, bugType, paragraphEntries, order);

		TextInstance bugInstance = new TextInstance(txtInstance.getProject(), txtInstance.getBugId(), "0");

		addBugPatterns(bugInstance, dataEntry, bugType);

	}

	private static void addBugPatterns(TextInstance txtInstance, CodedDataEntry dataEntry, String bugType) {

		addPatternEntry(txtInstance, dataEntry, bugType, bugEntries, 0);
	}

	private static void addSentencePatterns(TextInstance txtInstance, CodedDataEntry dataEntry, String bugType,
			Document bugRepDoc) {

		List<Sentence> sentences = bugRepDoc.getSentences();
		int order = sentences.indexOf(new Sentence(txtInstance.getInstanceId()));

		addPatternEntry(txtInstance, dataEntry, bugType, sentencesEntries, order);

		// --------------------------

		PatternEntry2 entry2 = new PatternEntry2(txtInstance, order);
		Set<String> infoTypes = sentenceOrderedPatterns.get(entry2);
		if (infoTypes == null) {
			infoTypes = new HashSet<>();
			sentenceOrderedPatterns.put(entry2, infoTypes);
		}

		String[] patterns = { dataEntry.pattern1, dataEntry.pattern2, dataEntry.pattern3, dataEntry.pattern4 };
		for (String pattern : patterns) {

			if (pattern.isEmpty()) {
				continue;
			}

			String infoType = pattern.substring(2, 4);
			infoTypes.add(infoType);

		}

		// -----------------------------

		String pararagraphId = txtInstance.getInstanceId().split("\\.")[0];
		TextInstance paragraphInstance = new TextInstance(txtInstance.getProject(), txtInstance.getBugId(),
				pararagraphId);

		addParagraphPatterns(paragraphInstance, dataEntry, bugType, bugRepDoc);

	}

	private static void addPatternEntry(TextInstance txtInstance, CodedDataEntry dataEntry, String bugType,
			LinkedHashSet<PatternEntry> entries, int order) {

		if (order < 0) {
			throw new RuntimeException("Order less than zero for " + txtInstance);
		}

		String[] patterns = { dataEntry.pattern1, dataEntry.pattern2, dataEntry.pattern3, dataEntry.pattern4 };

		for (String pattern : patterns) {

			pattern = pattern.trim();

			if (pattern.isEmpty()) {
				continue;
			}

			String patternType = pattern.substring(0, 1);
			String infoType = pattern.substring(2, 4);

			PatternEntry entry = new PatternEntry(txtInstance, pattern, infoType, patternType, bugType, order);
			entries.add(entry);
		}
	}

}
