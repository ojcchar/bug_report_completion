package seers.bug_report_analysis.tools.euler.nimbus;

import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.regularparse.ParsedBugReport;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport;

import java.io.File;
import java.nio.file.Paths;

public class MainBugReportConverter {

    public static void main(String[] args) throws Exception {
        String inputDir ="C:\\Users\\ojcch\\Documents\\Repositories\\Git\\Android-Bug-Report-Reproduction" +
                "\\EulerEvaluation\\Data\\1_parsed_bug_reports";
        String outputDir = "C:\\Users\\ojcch\\Documents\\Repositories\\Git\\Android-Bug-Report-Reproduction" +
                "\\EulerEvaluation\\Data\\1_parsed_bug_reports_format2";

        final File[] files = new File(inputDir).listFiles(f -> f.getName().endsWith(".xml"));
        for (File file : files) {
            final ParsedBugReport pBR = XMLHelper.readXML(ParsedBugReport.class, file);
            ShortLabeledBugReport shBR = pBR.toShortLabeledBugReport();

            File outFile  = Paths.get(outputDir, file.getName()).toFile();
            XMLHelper.writeXML(shBR, outFile);
        }
    }
}
