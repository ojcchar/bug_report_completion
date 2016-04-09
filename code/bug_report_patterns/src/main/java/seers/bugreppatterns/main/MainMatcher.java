package seers.bugreppatterns.main;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.bugreppatterns.matcher.SentenceMatcherProcessor;

public class MainMatcher {

	private static String fileAssignment = "test_data\\matcher\\sentences_coding.csv";

	public static void main(String[] args) throws Exception {
		List<List<String>> codedData = readData();

		Class<? extends ThreadProcessor> class1 = SentenceMatcherProcessor.class;
		ThreadParameters params = new ThreadParameters();
		ThreadExecutor.executePaginated(codedData.subList(1, codedData.size()), class1, params, 5);
	}

	private static List<List<String>> readData() throws IOException {
		CsvParser csvParser = new CsvParserBuilder().separator(';').multiLine(true).build();
		try (CsvReader csvReader = new CsvReader(new FileReader(fileAssignment), csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			return allLines;

		}
	}

}
