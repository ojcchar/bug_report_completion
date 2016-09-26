package seers.bugrepcompl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;

public class MainAgreement {

	private static int numMissing;
	private static int numSentences;
	private static int numSentencesOneMatch;
	private static int numMatches;
	private static int numMismatchesSize;
	private static int numSentencesMax;

	public static void main(String[] args) throws Exception {

		String baseFolder = args[0];
		String coder1 = args[1];
		String coder2 = args[2];

		String fileAssignment = baseFolder + File.separator + "data_sample.csv";
		String fileCoder1 = baseFolder + File.separator + "data_" + coder1 + ".csv";
		String fileCoder2 = baseFolder + File.separator + "data_" + coder2 + ".csv";

		List<CodingAssigment> assignment = readDataAssignment(fileAssignment, coder1, coder2);

		List<CodedDataEntry> data1 = readDataCoder(fileCoder1);
		List<CodedDataEntry> data2 = readDataCoder(fileCoder2);

		processData(assignment, data1, data2);

	}

	private static void processData(List<CodingAssigment> assignment, List<CodedDataEntry> data1,
			List<CodedDataEntry> data2) {

		Comparator<CodedDataEntry> comparator = Comparator.comparing(e -> e.project);
		comparator = comparator.thenComparing(Comparator.comparing(e -> e.bugId));
		comparator = comparator.thenComparing(Comparator.comparing(e -> e.sentenceId));

		data1.sort(comparator);
		data2.sort(comparator);

		// -----------------------------

		Comparator<CodingAssigment> comparator2 = Comparator.comparing(e -> e.project);
		comparator2 = comparator2.thenComparing(Comparator.comparing(e -> e.bugId));

		assignment.sort(comparator2);

		// -----------------------------

		assignment.stream().forEachOrdered(a -> {
			// System.out.println("---------" + a);

			List<CodedDataEntry> st1 = data1.stream()
					.filter(e -> a.bugId.equals(e.bugId) && a.project.equalsIgnoreCase(e.project))
					.collect(Collectors.toList());

			List<CodedDataEntry> st2 = data2.stream()
					.filter(e -> a.bugId.equals(e.bugId) && a.project.equalsIgnoreCase(e.project))
					.collect(Collectors.toList());

			if (st1.size() == 0 && st2.size() == 0) {
				numMissing++;
				System.out.println("Missing :" + a);
				return;
			}

			if (st1.size() != st2.size()) {
				numMismatchesSize++;
			}

			numSentences += Math.min(st1.size(), st2.size());
			numSentencesMax += Math.max(st1.size(), st2.size());

			int b = 0;

			for (CodedDataEntry e1 : st1) {

				Optional<CodedDataEntry> entry = st2.stream().filter(
						e2 -> (e2.paragraph + e2.sentence).trim().equalsIgnoreCase((e1.paragraph + e1.sentence)))
						.findAny();

				if (!entry.isPresent()) {
					continue;
				}

				b++;

				CodedDataEntry e2 = entry.get();

				if (e1.isObsBehavior != e2.isObsBehavior) {
					System.out.println("Check (ob):" + a);
					return;
				}

				if (e1.isExpecBehavior != e2.isExpecBehavior) {
					System.out.println("Check (eb):" + a);
					return;
				}

				if (e1.isStepsToRepro != e2.isStepsToRepro) {
					System.out.println("Check (sr):" + a);
					return;
				}

			}

			if (b == 0) {
				System.out.println("Check (no match):" + a);
			} else {
				if (b == st1.size()) {
					numMatches++;
				}
				numSentencesOneMatch += b;
				System.out.println("Good (" + b + "/" + st1.size() + " matches):" + a);
			}

			// for (CodedDataEntry e1 : st1) {
			//
			// Optional<CodedDataEntry> entry = st2.stream().filter(e2 ->
			// e2.sentenceId.equals(e1.sentenceId))
			// .findAny();
			//
			// if (!entry.isPresent()) {
			// System.out.println("Check (not found):" + a);
			// return;
			// }
			//
			// CodedDataEntry e2 = entry.get();
			//
			// if (e1.isObsBehavior != e2.isObsBehavior) {
			// System.out.println("Check (ob):" + a);
			// return;
			// }
			//
			// if (e1.isExpecBehavior != e2.isExpecBehavior) {
			// System.out.println("Check (eb):" + a);
			// return;
			// }
			//
			// if (e1.isStepsToRepro != e2.isStepsToRepro) {
			// System.out.println("Check (sr):" + a);
			// return;
			// }
			//
			// }

		});

		System.out.println("-----------------");
		System.out.println(assignment.size());
		System.out.println("# missing: " + numMissing);
		System.out.println("# mismatch by size: " + numMismatchesSize);
		System.out.println("# matches: " + numMatches);
		System.out.println("# min sentences: " + numSentences);
		System.out.println("# max sentences: " + numSentencesMax);
		System.out.println("# sentences match: " + numSentencesOneMatch);
	}

	private static List<CodingAssigment> readDataAssignment(String fileAssignment, String coder1, String coder2)
			throws IOException {

		CsvParser csvParser = new CsvParserBuilder().separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(fileAssignment), "Cp1252"), csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			List<CodingAssigment> assign = new ArrayList<>();

			allLines.subList(1, allLines.size()).stream()
					.filter(line -> coder1.equalsIgnoreCase(line.get(3)) && coder2.equalsIgnoreCase(line.get(4)))
					.forEach(l -> {
						CodingAssigment asg = new CodingAssigment();
						asg.project = l.get(0);
						asg.bugId = Integer.valueOf(l.get(1));
						asg.coder1 = l.get(3);
						asg.coder2 = l.get(4);

						assign.add(asg);
					});

			return assign;

		}
	}

	private static List<CodedDataEntry> readDataCoder(String fileCoder1) throws IOException {
		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(fileCoder1), "Cp1252"), csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			List<CodedDataEntry> entries = new ArrayList<>();

			allLines.subList(1, allLines.size()).forEach(line -> {
				CodedDataEntry entry = new CodedDataEntry();

				if (line.get(0).isEmpty() || line.get(1).isEmpty() || line.get(2).isEmpty()) {
					return;
				}

				// System.out.println(line);

				entry.project = line.get(0);
				entry.bugId = Integer.valueOf(line.get(1));
				entry.sentenceId = Integer.valueOf(line.get(2));
				entry.paragraph = line.get(3);
				entry.sentence = line.get(4);
				entry.isObsBehavior = line.get(5).equalsIgnoreCase("x");
				entry.isExpecBehavior = line.get(6).equalsIgnoreCase("x");
				entry.isStepsToRepro = line.get(7).equalsIgnoreCase("x");
				entry.pattern1 = line.get(8);
				entry.pattern2 = line.get(9);
				entry.pattern3 = line.get(10);

				entries.add(entry);

			});

			return entries;
		}
	}

	public static class CodedDataEntry {

		public String project;
		public Integer bugId;
		public Integer sentenceId;
		public String paragraph;
		public String sentence;
		public boolean isObsBehavior;
		public boolean isExpecBehavior;
		public boolean isStepsToRepro;
		public String pattern1;
		public String pattern2;
		public String pattern3;

		@Override
		public String toString() {
			return "[project=" + project + ", bugId=" + bugId + ", sentenceId=" + sentenceId + ", paragraph="
					+ paragraph + ", sentence=" + sentence + ", isObsBehavior=" + isObsBehavior + ", isExpecBehavior="
					+ isExpecBehavior + ", isStepsToRepro=" + isStepsToRepro + ", pattern1=" + pattern1 + ", pattern2="
					+ pattern2 + ", pattern3=" + pattern3 + "]";
		}

	}

	public static class CodingAssigment {

		public String project;
		public Integer bugId;
		public String coder1;
		public String coder2;

		@Override
		public String toString() {
			return "[project=" + project + ", bugId=" + bugId + ", coder1=" + coder1 + ", coder2=" + coder2 + "]";
		}

	}
}
