package seers.bugreppatterns.main.golset;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport;
import seers.bugrepcompl.utils.DataReader;
import seers.bugrepcompl.xmlcoding.AgreementMain;
import seers.bugrepcompl.xmlcoding.CodedBug;

public class BugGoldSetUpdaterMain {

	private static String codedDataFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/final_data";
	private static String bugGoldSetFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/gold-set-B-all_data_old.csv";
	static String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data";

	static HashMap<TextInstance, Labels> goldSetBugs = new HashMap<>();

	public static void main(String[] args) throws Exception {
		HashMap<TextInstance, Labels> goldSet = DataReader.readGoldSet(bugGoldSetFile);

		Set<Entry<TextInstance, Labels>> bugInstances = goldSet.entrySet();

		try (CsvWriter gsWr = new CsvWriterBuilder(
				new FileWriter(outputFolder + File.separator + "gold-set-B-all_data.csv")).separator(';')
						.quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
				CsvWriter gsCodedByUsWr = new CsvWriterBuilder(
						new FileWriter(outputFolder + File.separator + "gold-set-B-coded_by_us.csv")).separator(';')
								.quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
				CsvWriter gsNotCodedByUsWr = new CsvWriterBuilder(
						new FileWriter(outputFolder + File.separator + "gold-set-B-not_coded_by_us.csv")).separator(';')
								.quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build()) {

			List<String> header = Arrays.asList("system", "bug_id", "instance_id", "is_ob", "is_eb", "is_sr",
					"used_for_patterns");
			gsWr.writeNext(header);
			gsCodedByUsWr.writeNext(header);
			gsNotCodedByUsWr.writeNext(header);

			for (Entry<TextInstance, Labels> bugEntry : bugInstances) {

				TextInstance bugInstance = bugEntry.getKey();
				try {

					System.out.println("Processing " + bugInstance + " ");

					String project = bugInstance.getProject();
					String bugId = bugInstance.getBugId();

					File xmlFile = new File(
							codedDataFolder + File.separator + project + File.separator + bugId + ".parse.xml");
					ShortLabeledBugReport bugRep = XMLHelper.readXML(ShortLabeledBugReport.class, xmlFile);

					CodedBug codedBug = AgreementMain.analyzeBugRep(bugRep);
					Labels newLabels = codedBug.getLabels();

					List<String> entry = Arrays.asList(bugInstance.getProject(), bugInstance.getBugId(),
							bugInstance.getInstanceId(), newLabels.getIsOB().toLowerCase(),
							newLabels.getIsEB().toLowerCase(), newLabels.getIsSR().toLowerCase(),
							bugEntry.getValue().getCodedBy());
					gsWr.writeNext(entry);
					
					if ("yes".equals(bugEntry.getValue().getCodedBy())) {
						gsCodedByUsWr.writeNext(entry);
					}else{
						gsNotCodedByUsWr.writeNext(entry);
					}

				} catch (Exception e) {
					System.err.println("Error for " + bugInstance);
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
