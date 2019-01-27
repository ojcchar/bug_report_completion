package seers.bug_report_analysis.tools.nimbus;

import org.xml.sax.SAXException;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.regularparse.ParsedBugReport;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport;
import seers.bugreppatterns.main.preprocessing.BugCodePreprocessor;
import seers.bugreppatterns.main.preprocessing.BugNormalizationProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class MainBugPreprocessor extends BugNormalizationProcessor {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, JAXBException {

        String inputDir = "C:\\Users\\ojcch\\Documents\\Repositories\\Git\\Android-Bug-Report-Reproduction" +
                "\\EulerEvaluation\\Data\\1_parsed_bug_reports_format2";
        String outputDir = "C:\\Users\\ojcch\\Documents\\Repositories\\Git\\Android-Bug-Report-Reproduction" +
                "\\EulerEvaluation\\Data\\1_parsed_bug_reports_format2_prep";
        final File[] files = new File(inputDir).listFiles(f -> f.getName().endsWith(".xml"));
        for (File file : files) {
            ShortLabeledBugReport bugReport = XMLHelper.readXML(ShortLabeledBugReport.class, file);
            ShortLabeledBugReport bugPreprocessed = new ShortLabeledBugReport(bugReport);

            preProcessBug(new TextInstance("dummy", "bug_id", "0"), bugPreprocessed);

            File outFile  = Paths.get(outputDir, file.getName()).toFile();
            XMLHelper.writeXML(bugPreprocessed, outFile);
        }
    }
}
