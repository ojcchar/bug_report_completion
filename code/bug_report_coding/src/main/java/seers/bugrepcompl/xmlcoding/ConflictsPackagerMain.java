package seers.bugrepcompl.xmlcoding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;

public class ConflictsPackagerMain {

	static String conflictsFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/agreement/conflicts.csv";
	static String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/conflicts_data";
	static String codedDataFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/coded_data";

	public static void main(String[] args) throws Exception {

		List<List<String>> conflicts = readConflicts();
		HashMap<String, List<List<String>>> codersConflicts = new HashMap<>();

		for (List<String> conflict : conflicts) {

			String project = conflict.get(0);
			String bugId = conflict.get(1);
			String coder1 = conflict.get(10);
			String coder2 = conflict.get(11);
			String reviewer = conflict.get(12);

			File srcFile1 = new File(codedDataFolder + File.separator + coder1 + File.separator + "bugs_parsed"
					+ File.separator + project + File.separator + bugId + ".parse.xml");
			File destFile1 = new File(outputFolder + File.separator + reviewer + File.separator + project
					+ File.separator + bugId + "_1.parse.xml");
			FileUtils.copyFile(srcFile1, destFile1);

			File srcFile2 = new File(codedDataFolder + File.separator + coder2 + File.separator + "bugs_parsed"
					+ File.separator + project + File.separator + bugId + ".parse.xml");
			File destFile2 = new File(outputFolder + File.separator + reviewer + File.separator + project
					+ File.separator + bugId + "_2.parse.xml");
			FileUtils.copyFile(srcFile2, destFile2);

			// ----------------------

			List<List<String>> coderConflicts = codersConflicts.get(reviewer);
			if (coderConflicts == null) {
				coderConflicts = new ArrayList<>();
				codersConflicts.put(reviewer, coderConflicts);
			}

			List<String> newConflict = new ArrayList<>(conflict);
			newConflict.remove(10);
			newConflict.remove(10);
			newConflict.remove(10);
			newConflict.remove(10);

			coderConflicts.add(newConflict);

		}

		// ----------------------
		Set<String> reviewers = codersConflicts.keySet();
		for (String reviewer : reviewers) {
			try (CsvWriter wr = new CsvWriterBuilder(new FileWriter(
					outputFolder + File.separator + reviewer + File.separator + "conflicts_" + reviewer + ".csv"))
							.separator(';').quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();) {

				List<String> header = Arrays.asList(
						"project;bug_id;is_bug1;is_bug2;has_ob1;has_ob2;has_eb1;has_eb2;has_sr1;has_sr1;comments");
				wr.writeNext(header);
				
				//--
				List<List<String>> coderConflicts = codersConflicts.get(reviewer);
				
				
				Comparator<List<String>> comparator = Comparator.comparing(confl -> confl.get(0));
			    comparator = comparator.thenComparing(Comparator.comparing(confl -> confl.get(1)));
			    
			    List<List<String>> sortedConflicts = coderConflicts.stream().sorted(comparator).collect(Collectors.toList());
				
				
				//----
				wr.writeAll(sortedConflicts);
			}
		}

	}

	private static List<List<String>> readConflicts() throws Exception {
		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(conflictsFile), "Cp1252"),
				csvParser)) {
			List<List<String>> allLines = csvReader.readAll();
			return allLines.subList(1, allLines.size());
		}
	}

}
