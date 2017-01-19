package seers.bugrepcompl.xmlcoding;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.parse2.BugReport;
import seers.bugrepcompl.entity.parse2.DescriptionParagraph;
import seers.bugrepcompl.entity.parse2.DescriptionSentence;

public class CodingConverterMain {

	// xml files folder
	static String xmlFilesFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/xml_coding_files/originals";
	// coder
	static String coder = "alex";
	// copded data folder
	static String codedDataFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/coded_data";
	// output folder
	static String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/coded_data2";

	static Set<TextInstance> noBugs = new LinkedHashSet<>();
	static Set<TextInstance> bugs = new LinkedHashSet<>();
	static Set<TextInstance> processedBugs = new LinkedHashSet<>();
	static HashMap<TextInstance, HashMap<TextInstance, Labels>> codedInstances = new LinkedHashMap<>();

	public static void main(String[] args) throws Exception {

		loadCSVcoding();

		Set<Entry<TextInstance, HashMap<TextInstance, Labels>>> codedBugs = codedInstances.entrySet();
		for (Entry<TextInstance, HashMap<TextInstance, Labels>> bugEntry : codedBugs) {

			TextInstance bugInstance = bugEntry.getKey();
			Set<Entry<TextInstance, Labels>> codedSentences = bugEntry.getValue().entrySet();

			// read XML of bug
			File xmlFile = new File(
					xmlFilesFolder + File.separator + coder + File.separator + "bugs_parsed" + File.separator
							+ bugInstance.getProject() + File.separator + bugInstance.getBugId() + ".parse.xml");
			if (!xmlFile.exists()) {
				System.err.println("File does not exist: " + xmlFile);
				continue;
			}
			BugReport bugRep = XMLHelper.readXML(BugReport.class, xmlFile);

			// merge
			mergeCoding(bugRep, codedSentences);

			// write XML
			File outputFile = new File(
					outputFolder + File.separator + coder + File.separator + "bugs_parsed" + File.separator
							+ bugInstance.getProject() + File.separator + bugInstance.getBugId() + ".parse.xml");
			XMLHelper.writeXML(BugReport.class, bugRep, outputFile);

			processedBugs.add(bugInstance);
		}

		// ---------------------------

		boolean removeAll = bugs.removeAll(processedBugs);
		if (!removeAll && bugs.size() != processedBugs.size()) {
			throw new RuntimeException("Could determine which bugs didnt have any info!");
		}

		for (TextInstance bugInstance : bugs) {
			File srcFile = new File(
					xmlFilesFolder + File.separator + coder + File.separator + "bugs_parsed" + File.separator
							+ bugInstance.getProject() + File.separator + bugInstance.getBugId() + ".parse.xml");
			File destFile = new File(
					outputFolder + File.separator + coder + File.separator + "bugs_parsed" + File.separator
							+ bugInstance.getProject() + File.separator + bugInstance.getBugId() + ".parse.xml");
			FileUtils.copyFile(srcFile, destFile);
		}

		// ---------------------------

		for (TextInstance bugInstance : noBugs) {
			// read XML of bug
			File xmlFile = new File(
					xmlFilesFolder + File.separator + coder + File.separator + "bugs_parsed" + File.separator
							+ bugInstance.getProject() + File.separator + bugInstance.getBugId() + ".parse.xml");
			BugReport bugRep = XMLHelper.readXML(BugReport.class, xmlFile);

			bugRep.setNoBug("x");

			// write XML
			File outputFile = new File(
					outputFolder + File.separator + coder + File.separator + "bugs_parsed" + File.separator
							+ bugInstance.getProject() + File.separator + bugInstance.getBugId() + ".parse.xml");
			XMLHelper.writeXML(BugReport.class, bugRep, outputFile);
		}

	}

	private static void mergeCoding(BugReport bugRep, Set<Entry<TextInstance, Labels>> codedSentences) {

		for (Entry<TextInstance, Labels> sentenceEntry : codedSentences) {

			TextInstance sentInst = sentenceEntry.getKey();
			Labels labels = sentenceEntry.getValue();

			String instanceId = sentInst.getInstanceId();

			// sentence
			if (instanceId.contains(".")) {
				Optional<DescriptionSentence> sent = bugRep.getDescription().getAllSentences().stream()
						.filter(s -> s.getId().equals(instanceId)).findFirst();

				if (!sent.isPresent()) {
					System.err.println("Sentence not found: " + sentInst);
					continue;
				}

				sent.get().setOb(labels.getIsOB());
				sent.get().setEb(labels.getIsEB());
				sent.get().setSr(labels.getIsSR());
			} else {
				// paragraph or title

				if (instanceId.equals("0")) {
					bugRep.getTitle().setOb(labels.getIsOB());
					bugRep.getTitle().setEb(labels.getIsEB());
					bugRep.getTitle().setSr(labels.getIsSR());
				} else {
					Optional<DescriptionParagraph> par = bugRep.getDescription().getParagraphs().stream()
							.filter(p -> p.getId().equals(instanceId)).findFirst();

					if (!par.isPresent()) {
						System.err.println("Paragraph not found: " + sentInst);
						continue;
					}

					par.get().setOb(labels.getIsOB());
					par.get().setEb(labels.getIsEB());
					par.get().setSr(labels.getIsSR());
				}
			}

		}

	}

	private static void loadCSVcoding() throws Exception {

		File file = new File(
				codedDataFolder + File.separator + coder + File.separator + "0_list_of_bugs_" + coder + ".csv");

		if (!file.exists()) {
			return;
		}

		System.out.println("Reading bugs for " + coder);

		// ----------------------------------

		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(file), "Cp1252"),
				csvParser)) {

			List<List<String>> allLines = csvReader.readAll();
			List<List<String>> subList = allLines.subList(1, allLines.size());

			// ----------------------------------

			for (List<String> line : subList) {

				String bugCoded = line.get(5);

				String project = line.get(0);
				String bugId = line.get(1);
				TextInstance instance = new TextInstance(project, bugId, "0");

				if (bugCoded.trim().isEmpty()) {
					noBugs.add(instance);
				} else {
					bugs.add(instance);
				}

			}

		}

		// ---------------------------------

		File file2 = new File(
				codedDataFolder + File.separator + coder + File.separator + "1_bug_coding_" + coder + ".csv");

		if (!file2.exists()) {
			return;
		}

		System.out.println("Reading coding for " + coder);

		// ----------------------------------

		CsvParser csvParser2 = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(file2), "Cp1252"),
				csvParser2)) {

			List<List<String>> allLines = csvReader.readAll();
			List<List<String>> subList = allLines.subList(1, allLines.size());

			// ----------------------------------

			for (List<String> line : subList) {

				String project = line.get(0);
				String bugId = line.get(1);
				String instanceId = line.get(2); // paragraph or sentence id

				if (instanceId.trim().isEmpty()) {
					continue;
				}

				TextInstance bugInstance = new TextInstance(project, bugId, "0");
				HashMap<TextInstance, Labels> codedSentences = codedInstances.get(bugInstance);
				if (codedSentences == null) {
					codedSentences = new HashMap<>();
					codedInstances.put(bugInstance, codedSentences);
				}

				// ----------------------------------

				TextInstance sentInstance = new TextInstance(project, bugId, instanceId);
				Labels labels = codedSentences.get(sentInstance);
				if (labels != null) {
					System.err.println("Repeated sentence: " + sentInstance);
					continue;
				}

				// ----------------------------------

				String isOB = line.get(3);
				String isEB = line.get(4);
				String isSR = line.get(5);

				labels = new Labels(isOB, isEB, isSR);
				codedSentences.put(sentInstance, labels);

			}

		}

	}

}
