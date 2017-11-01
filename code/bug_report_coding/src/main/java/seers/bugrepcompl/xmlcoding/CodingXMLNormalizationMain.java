package seers.bugrepcompl.xmlcoding;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.CodedDataEntry;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReport;
import seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReportTitle;
import seers.bugrepcompl.entity.patterncoding.PatternLabeledDescriptionParagraph;
import seers.bugrepcompl.entity.patterncoding.PatternLabeledDescriptionSentence;
import seers.bugrepcompl.utils.DataReader;

public class CodingXMLNormalizationMain {

	private static String oldPatternCodingFile = "C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/test_data/matcher/sentences_coding.csv";
	private static String codedDataFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/final_data";
	private static String sentencesChangedFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/final_bug_data_old/changed_sentences.csv";
	private static String bugGoldSetFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/gold-set-B-coded_by_us.csv";
	private static String patternCodingFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/final_data_pattern_coding";
	private static List<CodedDataEntry> oldCodedData;
	private static HashMap<TextInstance, List<ChangedId>> changedSentences;
	static boolean checkTitle = true;

	public static void main(String[] args) throws Exception {

		HashMap<TextInstance, Labels> goldSet = DataReader.readGoldSet(bugGoldSetFile);

		oldCodedData = DataReader.readCodedData(oldPatternCodingFile);

		changedSentences = readSentencesChanged(sentencesChangedFile);

		// -----------------------------------------

		Set<Entry<TextInstance, Labels>> bugInstances = goldSet.entrySet();
		for (Entry<TextInstance, Labels> bugEntry : bugInstances) {

			TextInstance bugInstance = bugEntry.getKey();
			Labels labels = bugEntry.getValue();
			try {

				// System.out.println("Processing " + bugInstance + " ");

				String project = bugInstance.getProject();
				String bugId = bugInstance.getBugId();

				// -----------------------------------------

				File xmlFile = new File(
						codedDataFolder + File.separator + project + File.separator + bugId + ".parse.xml");
				seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport bugRep = XMLHelper
						.readXML(seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport.class, xmlFile);
				PatternLabeledBugReport codedBug = bugRep.toPatternCodingBug();

				// -----------------------------------------

				List<CodedDataEntry> bugCodedEntries = oldCodedData.stream().filter(
						entry -> entry.equals(new CodedDataEntry(bugInstance.getProject(), bugInstance.getBugId())))
						.collect(Collectors.toList());

				if (!bugCodedEntries.isEmpty()) {
					processBug(codedBug, bugInstance, labels, bugCodedEntries);
				} else {
					System.err.println("No coded entries!!!");
				}

				// -----------------------------------------

				int result = checkCoding(codedBug);
				switch (result) {
				case -1:
					System.out.println("Inconsistent coding for: " + bugInstance);
					break;

				case -2:
					System.out.println("Missing or coding for: " + bugInstance);

					break;

				default:
					break;
				}

				// -----------------------------------------

				File projectFolder = new File(patternCodingFolder + File.separator + project);
				if (!projectFolder.exists()) {
					projectFolder.mkdirs();
				}
				File outputFile = new File(projectFolder + File.separator + bugId + ".parse.xml");
				XMLHelper.writeXML(PatternLabeledBugReport.class, codedBug, outputFile);

			} catch (Exception e) {
				System.err.println("Error for " + bugInstance);
				e.printStackTrace();
			}

		}

	}

	private static int checkCoding(PatternLabeledBugReport codedBug) {

		boolean isEverythingRight = true;
		if (checkTitle) {

			PatternLabeledBugReportTitle title = codedBug.getTitle();
			Labels titleLabels = new Labels(title.getOb(), title.getEb(), title.getSr());
			Set<String> patterns = pattenrsToSet(title.getPatterns());

			isEverythingRight = checkMissingCoding(titleLabels, patterns);
			if (!isEverythingRight) {
				return -2;
			}
			isEverythingRight = checkInconsistentCoding(titleLabels, patterns);
			if (!isEverythingRight) {
				return -1;
			}
		}

		// ---------------------------------------------------

		List<PatternLabeledDescriptionParagraph> paragraphs = codedBug.getDescription().getParagraphs();
		for (PatternLabeledDescriptionParagraph paragraph : paragraphs) {
			Labels labels2 = new Labels(paragraph.getOb(), paragraph.getEb(), paragraph.getSr());
			Set<String> patterns2 = pattenrsToSet(paragraph.getPatterns());

			if (paragraph.getSentences().size() > 1) {
				isEverythingRight = checkMissingCoding(labels2, patterns2);
				if (!isEverythingRight) {
					return -2;
				}
				isEverythingRight = checkInconsistentCoding(labels2, patterns2);
				if (!isEverythingRight) {
					return -1;
				}

			} else {
				if (!patterns2.isEmpty()) {

					isEverythingRight = checkInconsistentCoding(labels2, patterns2);
					if (!isEverythingRight) {
						return -1;
					}
				}
			}
		}

		// ---------------------------------------------------

		List<PatternLabeledDescriptionSentence> sentences = codedBug.getDescription().getAllSentences();
		for (PatternLabeledDescriptionSentence sentence : sentences) {
			Labels labels2 = new Labels(sentence.getOb(), sentence.getEb(), sentence.getSr());
			Set<String> patterns2 = pattenrsToSet(sentence.getPatterns());

			isEverythingRight = checkMissingCoding(labels2, patterns2);
			if (!isEverythingRight) {
				return -2;
			}
			isEverythingRight = checkInconsistentCoding(labels2, patterns2);
			if (!isEverythingRight) {
				return -1;
			}
		}
		return 0;
	}

	private static boolean checkInconsistentCoding(Labels entryLabels, Set<String> patterns) {
		if (!entryLabels.getIsOB().isEmpty()) {
			if (patterns.stream().noneMatch(pat -> pat.contains("_OB_"))) {
				return false;
			}
		}
		if (!entryLabels.getIsEB().isEmpty()) {
			if (patterns.stream().noneMatch(pat -> pat.contains("_EB_"))) {
				return false;
			}
		}
		if (!entryLabels.getIsSR().isEmpty()) {
			if (patterns.stream().noneMatch(pat -> pat.contains("_SR_"))) {
				return false;
			}
		}

		return true;
	}

	private static boolean checkMissingCoding(Labels entryLabels, Set<String> patterns) {
		if (!entryLabels.getIsOB().isEmpty() || !entryLabels.getIsEB().isEmpty() || !entryLabels.getIsSR().isEmpty()) {
			if (patterns.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public static Set<String> pattenrsToSet(String patterns) {
		if (patterns == null || patterns.trim().isEmpty()) {
			return new LinkedHashSet<>();
		}
		return new LinkedHashSet<>(Arrays.asList(patterns.split(",")));
	}

	private static HashMap<TextInstance, List<ChangedId>> readSentencesChanged(String sentencesChangedFile2)
			throws Exception {
		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(
				new InputStreamReader(new FileInputStream(sentencesChangedFile2), "Cp1252"), csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			HashMap<TextInstance, List<ChangedId>> entries = new HashMap<>();

			allLines.subList(1, allLines.size()).forEach(line -> {

				String project = line.get(0);
				String bugId = line.get(1);
				String oldId = line.get(2);
				String newId = line.get(3);

				TextInstance instance = new TextInstance(project, bugId, "0");

				List<ChangedId> changedSenten = entries.get(instance);
				if (changedSenten == null) {
					changedSenten = new ArrayList<>();
					entries.put(instance, changedSenten);
				}

				changedSenten.add(new ChangedId(oldId, newId));
			});

			return entries;
		}
	}

	private static void processBug(PatternLabeledBugReport bugRep, TextInstance bugInstance, Labels labels,
			List<CodedDataEntry> bugEntries) {

		for (CodedDataEntry bugCodedEntry : bugEntries) {

			String instanceId = bugCodedEntry.instanceId;
			instanceId = instanceId.replace(",", ".");

			// -----------------------------------------------------

			boolean ob = bugCodedEntry.isObsBehavior;
			boolean eb = bugCodedEntry.isExpecBehavior;
			boolean sr = bugCodedEntry.isStepsToRepro;

			Labels entryLabels = new Labels(ob ? "x" : "", eb ? "x" : "", sr ? "x" : "");

			Set<String> patterns = new LinkedHashSet<>(Arrays.asList(bugCodedEntry.pattern1, bugCodedEntry.pattern2,
					bugCodedEntry.pattern3, bugCodedEntry.pattern4));

			checkLabelsAndPatterns(entryLabels, patterns);

			// -----------------------------------------------------

			boolean isTitle = false;
			boolean isParagraph = false;
			boolean isSentence = false;

			if (instanceId.contains(".")) {
				if (instanceId.startsWith("0.")) {
					isTitle = true;
				} else {
					isSentence = true;
				}
			} else {
				isParagraph = true;
			}

			// -----------------------------------------------------

			String changedId = getNewId(bugInstance, instanceId);

			// title
			if (isTitle) {
				PatternLabeledBugReportTitle title = bugRep.getTitle();
				setPatterns(title, patterns);
			} else if (isParagraph) {
				// paragraph

				PatternLabeledDescriptionParagraph paragraph = findParagraph(bugRep, changedId);
				setPatterns(paragraph, patterns);
			} else if (isSentence) {
				// sentence
				PatternLabeledDescriptionSentence sentence = findSentence(bugRep, changedId);
				setPatterns(sentence, patterns);
			} else {
				throw new RuntimeException("Can't identify entry: " + bugCodedEntry);
			}

		}

	}

	private static String getNewId(TextInstance bugInstance, String instanceId) {
		List<ChangedId> changedIds = changedSentences.get(bugInstance);

		String changedId = instanceId;
		if (changedIds != null) {
			int indexOf = changedIds.indexOf(new ChangedId(instanceId));
			if (indexOf != -1) {
				changedId = changedIds.get(indexOf).newId;
			}
		}
		return changedId;
	}

	private static void setPatterns(PatternLabeledDescriptionSentence sentence, Set<String> patterns) {
		List<String> allPatterns = new ArrayList<>();
		if (!sentence.getOb().isEmpty()) {
			allPatterns.addAll(patterns.stream().filter(p -> p.contains("_OB_")).collect(Collectors.toList()));
		}
		if (!sentence.getEb().isEmpty()) {
			allPatterns.addAll(patterns.stream().filter(p -> p.contains("_EB_")).collect(Collectors.toList()));
		}
		if (!sentence.getSr().isEmpty()) {
			allPatterns.addAll(patterns.stream().filter(p -> p.contains("_SR_")).collect(Collectors.toList()));
		}

		sentence.setPatterns(getPatternsString(allPatterns));
	}

	private static PatternLabeledDescriptionSentence findSentence(PatternLabeledBugReport bugRep, String instanceId) {
		Optional<PatternLabeledDescriptionSentence> sentences = bugRep.getDescription().getAllSentences().stream()
				.filter(s -> s.getId().equals(instanceId)).findFirst();

		if (!sentences.isPresent()) {
			throw new RuntimeException("Sentence not found: " + instanceId);
		}

		return sentences.get();
	}

	private static void setPatterns(PatternLabeledDescriptionParagraph paragraph, Set<String> patterns) {
		List<String> allPatterns = new ArrayList<>();
		if (!paragraph.getOb().isEmpty()) {
			allPatterns.addAll(patterns.stream().filter(p -> p.contains("_OB_")).collect(Collectors.toList()));
		}
		if (!paragraph.getEb().isEmpty()) {
			allPatterns.addAll(patterns.stream().filter(p -> p.contains("_EB_")).collect(Collectors.toList()));
		}
		if (!paragraph.getSr().isEmpty()) {
			allPatterns.addAll(patterns.stream().filter(p -> p.contains("_SR_")).collect(Collectors.toList()));
		}

		paragraph.setPatterns(getPatternsString(allPatterns));
	}

	private static PatternLabeledDescriptionParagraph findParagraph(PatternLabeledBugReport bugRep, String instanceId) {
		Optional<PatternLabeledDescriptionParagraph> paragraph = bugRep.getDescription().getParagraphs().stream()
				.filter(p -> p.getId().equals(instanceId)).findFirst();

		if (!paragraph.isPresent()) {
			throw new RuntimeException("Paragraph not found: " + instanceId);
		}

		return paragraph.get();
	}

	private static void setPatterns(PatternLabeledBugReportTitle title, Set<String> patterns) {

		List<String> allPatterns = new ArrayList<>();
		if (!title.getOb().isEmpty()) {
			allPatterns.addAll(patterns.stream().filter(p -> p.contains("_OB_")).collect(Collectors.toList()));
		}
		if (!title.getEb().isEmpty()) {
			allPatterns.addAll(patterns.stream().filter(p -> p.contains("_EB_")).collect(Collectors.toList()));
		}
		if (!title.getSr().isEmpty()) {
			allPatterns.addAll(patterns.stream().filter(p -> p.contains("_SR_")).collect(Collectors.toList()));
		}

		title.setPatterns(getPatternsString(allPatterns));

	}

	private static String getPatternsString(List<String> allPatterns) {
		StringBuffer buffer = new StringBuffer();
		for (String pattern : allPatterns) {
			buffer.append(pattern);
			buffer.append(",");
		}
		if (buffer.length() != 0) {
			buffer.delete(buffer.length() - 1, buffer.length());
		}
		return buffer.toString();
	}

	private static void checkLabelsAndPatterns(Labels entryLabels, Set<String> patterns) {
		if (!entryLabels.getIsOB().isEmpty()) {
			if (patterns.stream().noneMatch(pat -> pat.contains("_OB_"))) {
				throw new RuntimeException("Entry was not coded with an OB pattern");
			}
		}
		if (!entryLabels.getIsEB().isEmpty()) {
			if (patterns.stream().noneMatch(pat -> pat.contains("_EB_"))) {
				throw new RuntimeException("Entry was not coded with an EB pattern");
			}
		}
		if (!entryLabels.getIsSR().isEmpty()) {
			if (patterns.stream().noneMatch(pat -> pat.contains("_SR_"))) {
				throw new RuntimeException("Entry was not coded with an SR pattern");
			}
		}
	}

	public static class ChangedId {
		public String oldId;
		public String newId;

		public ChangedId(String oldId, String newId) {
			super();
			this.oldId = oldId;
			this.newId = newId;
		}

		public ChangedId(String oldId) {
			super();
			this.oldId = oldId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((oldId == null) ? 0 : oldId.hashCode());
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
			ChangedId other = (ChangedId) obj;
			if (oldId == null) {
				if (other.oldId != null)
					return false;
			} else if (!oldId.equals(other.oldId))
				return false;
			return true;
		}

	}
}
