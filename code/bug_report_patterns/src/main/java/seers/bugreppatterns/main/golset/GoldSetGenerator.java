package seers.bugreppatterns.main.golset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.utils.DataReader;
import seers.bugreppatterns.goldset.BugListProcessor;
import seers.bugreppatterns.goldset.GoldSetProcessor;
import seers.bugreppatterns.main.validation.MainMatcher;

public class GoldSetGenerator {

	public static void main(String[] args) throws Exception {

		// command line arguments processing
		// if there are no arguments, the defaults values are used
		String codedDataFile = MainMatcher.fileCodedData;
		String outputFolderPrefix = "";
		String davisDataPath = "all_data_only_bugs_davies.csv";
		// only sentences and paragraphs
		boolean includeNonCodedInstances = false;
		String bugsDataFolder = "test_data" + File.separator + "data";
		boolean includeDaviesData = false;
		if (args.length > 0) {
			codedDataFile = args[0];
			outputFolderPrefix = args[1] + File.separator;
			davisDataPath = args[2];
			includeNonCodedInstances = "y".equalsIgnoreCase(args[3]);
			bugsDataFolder = args[4];
			includeDaviesData = "y".equalsIgnoreCase(args[5]);
		}

		try (CsvWriter goldSetWriterSentences = new CsvWriterBuilder(
				new FileWriter(outputFolderPrefix + "gold-set-S.csv")).separator(';').build();
				CsvWriter goldSetWriterParagraphs = new CsvWriterBuilder(
						new FileWriter(outputFolderPrefix + "gold-set-P.csv")).separator(';').build();
				CsvWriter goldSetWriterDocuments = new CsvWriterBuilder(
						new FileWriter(outputFolderPrefix + "gold-set-B.csv")).separator(';').build();
				CsvWriter davisOnlyBugsWriter = new CsvWriterBuilder(
						new FileWriter(outputFolderPrefix + "gold-set-B-only-davies.csv")).separator(';').build();) {

			// read the coded data
			List<CodedDataEntry> codedData = DataReader.readCodedData(codedDataFile);

			// read Davies' gold set
			HashMap<TextInstance, Labels> daviesGoldSetBugs = readDavisGoldSet(davisDataPath);

			System.out.println(codedData.size());

			// generate titles
			generateTitles(goldSetWriterSentences);
			generateTitles(goldSetWriterParagraphs);
			generateTitles(goldSetWriterDocuments);
			generateTitles(davisOnlyBugsWriter);

			// process the data to generate the gold sets
			Class<? extends ThreadProcessor> class1 = GoldSetProcessor.class;
			ThreadParameters params = new ThreadParameters();
			ThreadExecutor.executePaginated(codedData.subList(1, codedData.size()), class1, params, 5);

			if (includeNonCodedInstances) {

				ArrayList<TextInstance> bugs = new ArrayList<>(GoldSetProcessor.goldSetBugs.keySet());

				// process the data to generate the gold sets
				Class<? extends ThreadProcessor> class2 = BugListProcessor.class;
				ThreadParameters params2 = new ThreadParameters();
				params2.addParam("bugsDataFolder", bugsDataFolder);
				ThreadExecutor.executePaginated(bugs, class2, params2, 5);
			}

			if (includeDaviesData) {
				// merge both gold sets (Davies' and ours)
				updateBugsGoldSet(daviesGoldSetBugs, GoldSetProcessor.goldSetBugs);
			} else {
				// write only the bugs coded by Davies
				HashMap<TextInstance, Labels> onlyBugsBydavies = getOnlyBugsCodedByDavies(daviesGoldSetBugs);
				writeGoldSets(onlyBugsBydavies.entrySet(), davisOnlyBugsWriter);
			}

			// write the gold sets
			writeGoldSets(GoldSetProcessor.goldSetBugs.entrySet(), goldSetWriterDocuments);
			writeGoldSets(GoldSetProcessor.goldSetParagraphs.entrySet(), goldSetWriterParagraphs);
			writeGoldSets(GoldSetProcessor.goldSetSentences.entrySet(), goldSetWriterSentences);

		}

	}

	private static HashMap<TextInstance, Labels> getOnlyBugsCodedByDavies(
			HashMap<TextInstance, Labels> daviesGoldSetBugs) {

		HashMap<TextInstance, Labels> onlyBugsBydavies = new HashMap<>();
		daviesGoldSetBugs.forEach((e, v) -> {

			if (GoldSetProcessor.goldSetBugs.get(e) != null) {
				return;
			}

			v.setCodedBy(Labels.CODED_BY_DAVIES);
			onlyBugsBydavies.put(e, v);
		});

		return onlyBugsBydavies;
	}

	private static void updateBugsGoldSet(HashMap<TextInstance, Labels> daviesGoldSetBugs,
			ConcurrentHashMap<TextInstance, Labels> goldSetBugs) {

		for (Entry<TextInstance, Labels> davidEntry : daviesGoldSetBugs.entrySet()) {
			if (!goldSetBugs.containsKey(davidEntry.getKey())) {
				Labels daviesLabels = davidEntry.getValue();
				daviesLabels.setCodedBy(Labels.CODED_BY_DAVIES);
				goldSetBugs.put(davidEntry.getKey(), daviesLabels);
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

	private static void generateTitles(CsvWriter writer) {
		String[] title = new String[] { "system", "bug_id", "instance_id", "is_ob", "is_eb", "is_sr", "coded_by" };
		writer.writeNext(Arrays.asList(title));

	}

}
