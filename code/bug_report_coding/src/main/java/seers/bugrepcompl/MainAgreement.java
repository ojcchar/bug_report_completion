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
import seers.bugrepcompl.entity.CodedDataEntry;
import seers.bugrepcompl.utils.DataReader;

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

		List<CodedDataEntry> data1 = DataReader.readCodedData(fileCoder1);
		List<CodedDataEntry> data2 = DataReader.readCodedData(fileCoder2);

		processData(assignment, data1, data2);

	}

	private static void processData(List<CodingAssigment> assignment, List<CodedDataEntry> data1,
			List<CodedDataEntry> data2) {

		Comparator<CodedDataEntry> comparator = Comparator.comparing(e -> e.project);
		comparator = comparator.thenComparing(Comparator.comparing(e -> e.bugId));
		comparator = comparator.thenComparing(Comparator.comparing(e -> e.entryId));

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
						e2 -> (e2.paragraphTxt + e2.sentenceTxt).trim().equalsIgnoreCase((e1.paragraphTxt + e1.sentenceTxt)))
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
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(fileAssignment), "Cp1252"),
				csvParser)) {

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
