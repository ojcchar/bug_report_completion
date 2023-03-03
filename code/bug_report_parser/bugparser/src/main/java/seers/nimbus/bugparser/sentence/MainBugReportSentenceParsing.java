package seers.nimbus.bugparser.sentence;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.BugReport;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport;

public class MainBugReportSentenceParsing {

	// private static String baseFolder =
	// "C:/Users/ojcch/Documents/Repositories/Git/Android-Bug-Report-Reproduction/Data/BugReports-Data/xml";
	private static String baseFolder = "C:/Users/ojcch/Documents/Repositories/Git/Android-Bug-Report-Reproduction/Internal_Evaluation/Data/Perfect_bug_scenarios";
	private static String[] projects = { "selected" };
	// private static String[] projects = { "aarddict", "adsdroid", "AnagramSolver",
	// "android-mileage", "ATimeTracker",
	// "BMI_Calculator", "cardgamescores", "car-report", "document-viewer",
	// "droid-comic-viewer", "droidweight",
	// "eyeCam", "gnucash-android", "netmbuddy", "notepad_banderlabs", "Olam",
	// "openintents", "openintents2",
	// "schedule-campfahrplan" };
	// private static String[] projects = { "ATimeTracker", "AnagramSolver",
	// "BMI_Calculator", "Olam", "adsdroid",
	// "cardgamescores", "document-viewer", "droid-comic-viewer", "droidweight",
	// "eyeCam", "gnucash-android",
	// "netmbuddy", "notepad_banderlabs", "openintents" };

	static boolean codingParsing = true;

	public static void main(String[] args) throws Exception {

		List<Integer> bugsToParse = Arrays.asList(22, 45, 54, 76, 91, 92, 101, 106, 110, 158, 160, 162, 168, 178, 192, 198, 199, 200, 228, 248, 1033, 1150, 1153, 1197, 1198, 1201, 1228, 1389, 1425, 1446, 1563, 1568, 1641);

		for (Integer bugId : bugsToParse) {
			
			String inFolder = Paths.get("/Users/ojcchar/repositories/Projects/FaultLocalizationCode/data/BugReportsMarked/OriginalBugReports", "Bug" + bugId).toString();

			//create output folder
			String outFolder = Paths.get("/Users/ojcchar/repositories/Projects/FaultLocalizationCode/data/BugReportsMarked/OriginalBugReports_parsed", "Bug" + bugId + "").toString();
			File outputFolder = new File(outFolder);
			if (!outputFolder.exists()) {
				FileUtils.forceMkdir(outputFolder);
			}
			parseBugReports(inFolder, outFolder);
		}

		// for (String project : projects) {
		// 	System.out.println("Processing project: " + project);

		// 	String inFolder = baseFolder + File.separator + "data_" + project + File.separator + "/short";
		// 	String outFolder = baseFolder + File.separator + "data_" + project + File.separator + "/short_parsed";

		// 	parseBugReports(inFolder, outFolder);
		// 	System.out.println("Done project: " + project);

		// }

	}

	private static void parseBugReports(String inFolder, String outFolder) throws Exception {

		Collection<File> files = FileUtils.listFiles(new File(inFolder), new String[] { "xml" }, false);

		File outputFolder = new File(outFolder);
		if (!outputFolder.exists()) {
			FileUtils.forceMkdir(outputFolder);
		}

		for (File file : files) {
			try {
				BugReport bugRep = XMLHelper.readXML(BugReport.class, file);
				if (codingParsing) {
					seers.bugrepcompl.entity.codingparse.LabeledBugReport parsedBugReport = BugReportSentenceParser
							.parseBugReportForCoding(bugRep);
					File outputFile = new File(outFolder + File.separator + file.getName());
					ShortLabeledBugReport shortparsedBugReport = parsedBugReport.toBugReport2();
					XMLHelper.writeXML(ShortLabeledBugReport.class, shortparsedBugReport,
							outputFile);
				} else {
					seers.bugrepcompl.entity.regularparse.ParsedBugReport parsedBugReport = BugReportSentenceParser
							.parseBugReport(bugRep);
					File outputFile = new File(outFolder + File.separator + file.getName());
					XMLHelper.writeXML(seers.bugrepcompl.entity.regularparse.ParsedBugReport.class, parsedBugReport,
							outputFile);
				}
			} catch (Exception e) {
				System.err.println("Error for: " + file);
				e.printStackTrace();
			}
		}
	}
}
