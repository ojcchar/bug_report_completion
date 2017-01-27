package seers.bugrepcompl.xmlcoding;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.shortcodingparse.BugReport;
import seers.bugrepcompl.utils.DataReader;

public class FinalDataMain {

	static String goldSetFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/final_bug_data/gold_set.csv";
	static String finalCodingFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/final_bug_data/final_data";
	static String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/final_bug_data/regular_parsed_data";

	public static void main(String[] args) throws Exception {

		HashMap<TextInstance, Labels> goldSet = DataReader.readGoldSet(goldSetFile);

		Set<TextInstance> bugInstances = goldSet.keySet();

		for (TextInstance bugInstance : bugInstances) {

			System.out.println("Processing " + bugInstance);

			String project = bugInstance.getProject();
			String bugId = bugInstance.getBugId();

			File bugFile = new File(
					finalCodingFolder + File.separator + project + File.separator + bugId + ".parse.xml");
			BugReport codedBug = XMLHelper.readXML(BugReport.class, bugFile);

			seers.bugrepcompl.entity.regularparse.BugReport regularBug = codedBug.toRegularParsedBug();

			File projectFolder = new File(outputFolder + File.separator + project+"_parse");
			if (!projectFolder.exists()) {
				projectFolder.mkdirs();
			}

			File outputFile = new File(projectFolder + File.separator + bugId + ".xml.parse");
			XMLHelper.writeXML(seers.bugrepcompl.entity.regularparse.BugReport.class, regularBug, outputFile);

		}

	}

}
