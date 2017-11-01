package seers.nimbus.bugparser.sentence;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.BugReport;

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

		for (String project : projects) {
			System.out.println("Processing project: " + project);

			String inFolder = baseFolder + File.separator + "data_" + project + File.separator + "/short";
			String outFolder = baseFolder + File.separator + "data_" + project + File.separator + "/short_parsed";

			parseBugReports(inFolder, outFolder);
			System.out.println("Done project: " + project);

		}

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
					XMLHelper.writeXML(seers.bugrepcompl.entity.codingparse.LabeledBugReport.class, parsedBugReport,
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
