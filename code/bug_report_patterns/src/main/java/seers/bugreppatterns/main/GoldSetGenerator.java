package seers.bugreppatterns.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.bugrepcompl.entity.CodedDataEntry;
import seers.bugrepcompl.utils.DataReader;
import seers.bugreppatterns.goldset.GoldSetProcessor;
import seers.bugreppatterns.goldset.GoldSetProcessor.TextInstance;
import seers.bugreppatterns.pattern.predictor.Labels;

public class GoldSetGenerator {

	private static CsvWriter goldSetWriterSentences;
	private static CsvWriter goldSetWriterParagraphs;
	private static CsvWriter goldSetWriterDocuments;

	public static void main(String[] args) throws Exception {

		// command line arguments processing
		// if there are no arguments, the defaults values are used
		String codedDataFile = MainMatcher.fileAssignment;
		String outputFolderPrefix = "";
		String davisDataPath = "all_data_only_bugs_davies.csv";
		if (args.length > 0) {
			codedDataFile = args[0];
			outputFolderPrefix = args[1] + File.separator;
			davisDataPath = args[2];
		}

		goldSetWriterSentences = new CsvWriterBuilder(new FileWriter(outputFolderPrefix + "gold-set-S.csv"))
				.separator(';').build();
		goldSetWriterParagraphs = new CsvWriterBuilder(new FileWriter(outputFolderPrefix + "gold-set-P.csv"))
				.separator(';').build();
		goldSetWriterDocuments = new CsvWriterBuilder(new FileWriter(outputFolderPrefix + "gold-set-B.csv"))
				.separator(';').build();

		try {

			// read the coded data
			List<CodedDataEntry> codedData = DataReader.readCodedData(codedDataFile);

			// read Davis' gold set
			HashMap<TextInstance, Labels> davisGoldSetBugs = readDavisGoldSet(davisDataPath);

			System.out.println(codedData.size());

			// generate titles
			generateTitles();

			// process the data to generate the gold sets
			Class<? extends ThreadProcessor> class1 = GoldSetProcessor.class;
			ThreadParameters params = new ThreadParameters();
			ThreadExecutor.executePaginated(codedData.subList(1, codedData.size()), class1, params, 5);

			// merge both gold sets (Davis' and ours)
			updateBugsGoldSet(davisGoldSetBugs, GoldSetProcessor.goldSetBugs);

			// write the gold sets
			writeGoldSets(GoldSetProcessor.goldSetBugs.entrySet(), goldSetWriterDocuments);
			writeGoldSets(GoldSetProcessor.goldSetParagraphs.entrySet(), goldSetWriterParagraphs);
			writeGoldSets(GoldSetProcessor.goldSetSentences.entrySet(), goldSetWriterSentences);

		} finally {
			goldSetWriterSentences.close();
			goldSetWriterParagraphs.close();
			goldSetWriterDocuments.close();
		}

	}

	private static void updateBugsGoldSet(HashMap<TextInstance, Labels> davisGoldSetBugs,
			ConcurrentHashMap<TextInstance, Labels> goldSetBugs) {

		for (Entry<TextInstance, Labels> davidEntry : davisGoldSetBugs.entrySet()) {
			if (!goldSetBugs.containsKey(davidEntry.getKey())) {
				Labels davidLabels = davidEntry.getValue();
				davidLabels.setCodedBy(Labels.CODED_BY_DAVIS);
				goldSetBugs.put(davidEntry.getKey(), davidLabels);
			}

		}

	}

	private static HashMap<TextInstance, Labels> readDavisGoldSet(String davisDataPath) throws IOException {
		HashMap<TextInstance, Labels> davidsGoldSet = new HashMap<>();
		CsvParser csvParser = new CsvParserBuilder().separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(davisDataPath), "Cp1252"),
				csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			for (List<String> fields : allLines.subList(1, allLines.size())) {

				String project = fields.get(0);
				String bugId = fields.get(1);
				String instanceId = fields.get(2);
				String isOB = fields.get(3);
				String isEB = fields.get(4);
				String isSR = fields.get(5);

				TextInstance instance = new TextInstance(project, bugId, instanceId);
				Labels labels = new Labels(isOB, isEB, isSR);
				davidsGoldSet.put(instance, labels);
			}

		}
		return davidsGoldSet;
	}

	private static void writeGoldSets(Set<Entry<TextInstance, Labels>> entrySet, CsvWriter writer) {

		for (Entry<TextInstance, Labels> entry : entrySet) {
			TextInstance key = entry.getKey();
			Labels value2 = entry.getValue();
			String[] value = new String[] { key.getProject(), key.getBugId(), key.getInstanceId(), value2.getIsOB(),
					value2.getIsEB(), value2.getIsSR(), value2.getCodedBy() };
			writer.writeNext(Arrays.asList(value));
		}

	}

	private static void generateTitles() {

		String[] title = new String[] { "system", "bug_id", "instance_id", "is_ob", "is_eb", "is_sr", "coded_by" };
		goldSetWriterSentences.writeNext(Arrays.asList(title));
		goldSetWriterParagraphs.writeNext(Arrays.asList(title));
		goldSetWriterDocuments.writeNext(Arrays.asList(title));

	}

}
