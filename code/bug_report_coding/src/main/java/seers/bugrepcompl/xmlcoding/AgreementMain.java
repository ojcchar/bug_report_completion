package seers.bugrepcompl.xmlcoding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.parse2.BugReport;
import seers.bugrepcompl.entity.parse2.DescriptionParagraph;
import seers.bugrepcompl.entity.parse2.DescriptionSentence;

public class AgreementMain {

	static List<String> allowedCoders = new ArrayList<>(
			new HashSet<String>(Arrays.asList("alex", "juan", "laura", "fiorella", "jing", "oscar", "alejo")));
	// static HashSet<String> allowedCoders = new
	// HashSet<String>(Arrays.asList(new String[] { "jing", "oscar" }));

	static String completeSamplePath = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/sample/andi_students/complete_sample_seers50.csv";
	static String codedDataPath = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/coded_data";
	static String outFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/agreement";
	static boolean onlyBugs = true;

	static HashMap<String, Set<String>> noBugsCoders = new HashMap<>();
	static HashMap<String, HashMap<TextInstance, Labels>> codedBugsCoder = new HashMap<>();

	public static void main(String[] args) throws Exception {

		// read complete sample with allowed coders
		List<SampleEntry> sample = readCompleteSample();

		// print size of the sample
		System.out.println("# of entries in filtered sample: " + sample.size());

		// read csv files of the coders
		readCSVfiles();

		try (CsvWriter isBugWr = createWriter("is_bug");
				CsvWriter isOBWr = createWriter("is_ob");
				CsvWriter isEBWr = createWriter("is_eb");
				CsvWriter isSRWr = createWriter("is_sr");) {

			printHeaders(isBugWr);
			printHeaders(isOBWr);
			printHeaders(isEBWr);
			printHeaders(isSRWr);

			// for each entry
			for (SampleEntry sampleEntry : sample) {

				try {
					// ready bug of coder1
					CodedBug bug1 = getBugCoder(sampleEntry, 1);

					// ready bug of coder2
					CodedBug bug2 = getBugCoder(sampleEntry, 2);

					printAgreement(bug1, bug2, sampleEntry, isBugWr);
					printAgreement2(bug1, bug2, sampleEntry, isOBWr, new LabelReader() {
						@Override
						public String getInfo(Labels labels) {
							String isOB = labels.getIsOB();
							return isOB.trim();
						}
					});
					printAgreement2(bug1, bug2, sampleEntry, isEBWr, new LabelReader() {
						@Override
						public String getInfo(Labels labels) {
							String isEB = labels.getIsEB();
							return isEB.trim();
						}
					});

					printAgreement2(bug1, bug2, sampleEntry, isSRWr, new LabelReader() {
						@Override
						public String getInfo(Labels labels) {
							return labels.getIsSR().trim();
						}
					});

					// check conflicts
					printConflicts(bug1, bug2, sampleEntry);
				} catch (Exception e) {
					System.err.println("Error for entry: " + sampleEntry.toString());
					e.printStackTrace();
				}

			}
		}

	}

	public interface LabelReader {
		String getInfo(Labels labels);
	}

	private static void printAgreement2(CodedBug bug1, CodedBug bug2, SampleEntry sampleEntry, CsvWriter wr,
			LabelReader labelReader) {

		if (onlyBugs) {

			if (bug1.isBug() != bug2.isBug()) {
				return;
			}

			if (!bug1.isBug()) {
				return;
			}

		}

		TextInstance instance = sampleEntry.getInstance();

		List<String> row = new ArrayList<>(Arrays.asList(instance.getProject(), instance.getBugId()));
		for (String coder : allowedCoders) {
			if (coder.equals(sampleEntry.getCoder1())) {
				row.add(labelReader.getInfo(bug1.getLabels()).isEmpty() ? "false" : "true");
			} else if (coder.equals(sampleEntry.getCoder2())) {
				row.add(labelReader.getInfo(bug2.getLabels()).isEmpty() ? "false" : "true");
			} else {
				row.add("NA");
			}
		}

		wr.writeNext(row);
	}

	private static CsvWriter createWriter(String fileName) throws IOException {
		return new CsvWriterBuilder(new FileWriter(outFolder + File.separator + fileName + ".csv")).separator(';')
				.quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
	}

	private static void printAgreement(CodedBug bug1, CodedBug bug2, SampleEntry sampleEntry, CsvWriter isBugWr) {
		TextInstance instance = sampleEntry.getInstance();

		List<String> row = new ArrayList<>(Arrays.asList(instance.getProject(), instance.getBugId()));
		for (String coder : allowedCoders) {
			if (coder.equals(sampleEntry.getCoder1())) {
				row.add(bug1.isBug() + "");
			} else if (coder.equals(sampleEntry.getCoder2())) {
				row.add(bug2.isBug() + "");
			} else {
				row.add("NA");
			}
		}

		isBugWr.writeNext(row);
	}

	private static void printHeaders(CsvWriter isBugWr) {
		List<String> header = new ArrayList<>(Arrays.asList("project", "bug_id"));

		header.addAll(allowedCoders);
		isBugWr.writeNext(header);
	}

	private static void printConflicts(CodedBug bug1, CodedBug bug2, SampleEntry sampleEntry) {

		// TextInstance instance = sampleEntry.getInstance();
		if (bug1.isBug() != bug2.isBug()) {
			System.out.println(sampleEntry.toString() + ";is_bug;" + bug1.isBug() + ";" + bug2.isBug() + ";");
		}

		// String obDiff = "";
		// String ebDiff = "";
		// String srDiff = "";
		// if (!bug1.getLabels().getIsOB().equals(bug2.getLabels().getIsOB())) {
		// obDiff = "x";
		// }

		// check conflicts:
		// no bug
		// ob, eb, sr at bug level
	}

	private static CodedBug getBugCoder(SampleEntry sampleEntry, int i) throws Exception {

		String coder = i == 1 ? sampleEntry.getCoder1() : sampleEntry.getCoder2();
		TextInstance bugInstance = sampleEntry.getInstance();

		try {
			// -----------------------

			Set<String> noBugs = noBugsCoders.get(coder);
			if (noBugs != null) {
				if (noBugs.contains(bugInstance.getProject() + "-" + bugInstance.getBugId())) {
					return new CodedBug(false, new Labels("", "", ""));
				}
			}

			// -----------------------

			HashMap<TextInstance, Labels> codedBugs = codedBugsCoder.get(coder);
			if (codedBugs != null) {
				Labels labels = codedBugs.get(bugInstance);
				if (labels != null) {
					return new CodedBug(true, labels);
				}
			}

			// -----------------------

			File xmlFile = new File(
					codedDataPath + File.separator + coder + File.separator + "bugs_parsed" + File.separator
							+ bugInstance.getProject() + File.separator + bugInstance.getBugId() + ".parse.xml");
			if (xmlFile.exists()) {
				BugReport bugRep = XMLHelper.readXML(BugReport.class, xmlFile);
				return analyzeBugRep(bugRep);
			}

		} catch (Exception e) {
			System.err.println("Error for " + bugInstance + " - " + coder);
			e.printStackTrace();
		}

		throw new RuntimeException("No coded bug for " + sampleEntry.getInstance() + " - " + coder);
	}

	private static CodedBug analyzeBugRep(BugReport bugRep) {

		List<DescriptionSentence> allSentences = bugRep.getDescription().getAllSentences();
		List<DescriptionParagraph> paragraphs = bugRep.getDescription().getParagraphs();

		boolean isBug = bugRep.getNoBug().trim().isEmpty();
		if (paragraphs == null) {
			return new CodedBug(isBug, new Labels("", "", ""));
		}

		String isOB = allSentences.stream().anyMatch(p -> !p.getOb().trim().isEmpty()) ? "x" : "";
		if (isOB.isEmpty()) {
			isOB = paragraphs.stream().anyMatch(p -> !p.getOb().trim().isEmpty()) ? "x" : "";
		}

		String isEB = allSentences.stream().anyMatch(p -> !p.getEb().trim().isEmpty()) ? "x" : "";
		if (isEB.isEmpty()) {
			isEB = paragraphs.stream().anyMatch(p -> !p.getEb().trim().isEmpty()) ? "x" : "";
		}

		String isSR = paragraphs.stream().anyMatch(p -> !p.getSr().trim().isEmpty()) ? "x" : "";
		if (isSR.isEmpty()) {
			isSR = allSentences.stream().anyMatch(p -> !p.getSr().trim().isEmpty()) ? "x" : "";
		}

		Labels labels = new Labels(isOB, isEB, isSR);
		return new CodedBug(isBug, labels);
	}

	private static void readCSVfiles() throws Exception {

		for (String coder : allowedCoders) {
			readNoBugs(coder);
			readCodedBugs(coder);
		}

	}

	// static HashMap<String, HashMap<TextInstance, Labels>> codedBugsCoder =
	// new HashMap<>();
	private static void readCodedBugs(String coder) throws Exception {

		// ---------------------------------

		HashMap<TextInstance, Labels> bugCoding = codedBugsCoder.get(coder);
		if (bugCoding == null) {
			bugCoding = new LinkedHashMap<>();
			codedBugsCoder.put(coder, bugCoding);
		}

		// ----------------------------------

		File file = new File(
				codedDataPath + File.separator + coder + File.separator + "1_bug_coding_" + coder + ".csv");

		if (!file.exists()) {
			return;
		}

		System.out.println("Reading coding for " + coder);

		// ----------------------------------

		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(file), "Cp1252"),
				csvParser)) {

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

				// ----------------------------------

				String isOB = line.get(3);
				String isEB = line.get(4);
				String isSR = line.get(5);

				// ----------------------------------

				TextInstance instance = new TextInstance(project, bugId, "0");
				Labels labels = bugCoding.get(instance);
				if (labels == null) {
					labels = new Labels(isOB, isEB, isSR);
				} else {
					Labels label = new Labels(isOB, isEB, isSR);
					labels = mergeLabels(labels, label);

				}

				bugCoding.put(instance, labels);
			}

		}

	}

	private static void readNoBugs(String coder) throws Exception {

		// ---------------------------------

		Set<String> noBugs = noBugsCoders.get(coder);
		if (noBugs == null) {
			noBugs = new LinkedHashSet<>();
			noBugsCoders.put(coder, noBugs);
		}

		// ----------------------------------

		File file = new File(
				codedDataPath + File.separator + coder + File.separator + "0_list_of_bugs_" + coder + ".csv");

		if (!file.exists()) {
			return;
		}

		System.out.println("Reading no bugs for " + coder);

		// ----------------------------------

		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(file), "Cp1252"),
				csvParser)) {

			List<List<String>> allLines = csvReader.readAll();
			List<List<String>> subList = allLines.subList(1, allLines.size());

			// ----------------------------------

			for (List<String> line : subList) {

				String bugCoded = line.get(5);

				if (bugCoded.trim().isEmpty()) {
					String project = line.get(0);
					String bugId = line.get(1);
					noBugs.add(project + "-" + bugId);
				}

			}

		}

	}

	private static List<SampleEntry> readCompleteSample() throws Exception {
		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(
				new InputStreamReader(new FileInputStream(completeSamplePath), "Cp1252"), csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			List<SampleEntry> entries = new ArrayList<>();

			List<List<String>> subList = allLines.subList(1, allLines.size());

			System.out.println("Total # of entries in sample: " + subList.size());

			subList.forEach(line -> {

				String notCoded = line.get(5);

				if (!notCoded.trim().isEmpty()) {
					return;
				}

				String project = line.get(0);
				String bugId = line.get(1);
				String coder1 = line.get(2);
				String coder2 = line.get(3);

				if (allowedCoders.contains(coder1) && allowedCoders.contains(coder2)) {
					TextInstance instance = new TextInstance(project, bugId, "0");
					entries.add(new SampleEntry(instance, coder1, coder2));
				}

			});

			return entries;
		}
	}

	public static Labels mergeLabels(Labels labels, Labels label) {

		Labels labels2 = new Labels(labels);
		if (labels2.getIsOB().isEmpty()) {
			labels2.setIsOB(label.getIsOB());
		}
		if (labels2.getIsEB().isEmpty()) {
			labels2.setIsEB(label.getIsEB());
		}
		if (labels2.getIsSR().isEmpty()) {
			labels2.setIsSR(label.getIsSR());
		}
		return labels2;
	}

}
