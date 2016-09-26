package seers.bugreppatterns.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.bugreppatterns.goldset.GoldSetProcessor;
import seers.bugreppatterns.goldset.GoldSetProcessor.TextInstance;
import seers.bugreppatterns.pattern.predictor.Labels;

public class GoldSetGenerator {
	static String fileAssignment = "test_data" + File.separator + "matcher" + File.separator + "sentences_coding.csv";

	private static CsvWriter goldSetWriterSentences;
	private static CsvWriter goldSetWriterParagraphs;
	private static CsvWriter goldSetWriterDocuments;

	public static void main(String[] args) throws Exception {

		goldSetWriterSentences = new CsvWriterBuilder(new FileWriter("gold-set-S.csv")).separator(';').build();
		goldSetWriterParagraphs = new CsvWriterBuilder(new FileWriter("gold-set-P.csv")).separator(';').build();
		goldSetWriterDocuments = new CsvWriterBuilder(new FileWriter("gold-set-B.csv")).separator(';').build();

		List<List<String>> codedData = readData();

		try {

			System.out.println(codedData.size());

			generateTitles();

			Class<? extends ThreadProcessor> class1 = GoldSetProcessor.class;
			ThreadParameters params = new ThreadParameters();
			ThreadExecutor.executePaginated(codedData.subList(1, codedData.size()), class1, params, 5);

			generateGoldSets(GoldSetProcessor.goldSetBugs.entrySet(), goldSetWriterDocuments);
			generateGoldSets(GoldSetProcessor.goldSetParagraphs.entrySet(), goldSetWriterParagraphs);
			generateGoldSets(GoldSetProcessor.goldSetSentences.entrySet(), goldSetWriterSentences);
		} finally {
			goldSetWriterSentences.close();
			goldSetWriterParagraphs.close();
			goldSetWriterDocuments.close();
		}

	}

	private static void generateGoldSets(Set<Entry<TextInstance, Labels>> entrySet, CsvWriter writer) {

		for (Entry<TextInstance, Labels> entry : entrySet) {
			TextInstance key = entry.getKey();
			Labels value2 = entry.getValue();
			String[] value = new String[] { key.getProject(), key.getBugId(), key.getInstanceId(), value2.getIsOB(),
					value2.getIsEB(), value2.getIsSR() };
			writer.writeNext(Arrays.asList(value));
		}

	}

	private static void generateTitles() {

		String[] title = new String[] { "system", "bug_id", "instance_id", "is_ob", "is_eb", "is_sr" };
		goldSetWriterSentences.writeNext(Arrays.asList(title));
		goldSetWriterParagraphs.writeNext(Arrays.asList(title));
		goldSetWriterDocuments.writeNext(Arrays.asList(title));

	}

	private static List<List<String>> readData() throws IOException {
		CsvParser csvParser = new CsvParserBuilder().separator(';').multiLine(true).build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(fileAssignment), "Cp1252"), csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			return allLines;

		}
	}
}
