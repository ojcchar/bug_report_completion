package seers.bugrepcompl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;

public class MainTestCSVCR {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		String fileIn = "C:\\Users\\ojcch\\Downloads\\Bug report coding.csv";

		List<List<String>> lines = readLines(fileIn);

		for (List<String> list : lines) {
			System.out.println(list);
		}

	}

	private static List<List<String>> readLines(String fileIn) throws FileNotFoundException, IOException {
		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(fileIn), "Cp1252"),
				csvParser)) {
			return csvReader.readAll();
		}
	}
}
