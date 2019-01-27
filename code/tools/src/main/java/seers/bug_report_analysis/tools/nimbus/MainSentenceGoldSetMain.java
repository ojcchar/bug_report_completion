package seers.bug_report_analysis.tools.nimbus;

import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport;
import seers.bugreppatterns.main.golset.SentenceGoldSetMain;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class MainSentenceGoldSetMain extends SentenceGoldSetMain {

    public static void main(String[] args) throws Exception {

        String inputDir = "C:\\Users\\ojcch\\Documents\\Repositories\\Git\\Android-Bug-Report-Reproduction" +
                "\\EulerEvaluation\\Data\\1_parsed_bug_reports_format2_prep";
        String outputDir = "C:\\Users\\ojcch\\Documents\\Repositories\\Git\\Android-Bug-Report-Reproduction" +
                "\\EulerEvaluation\\Data\\2_nimbus_pattern_features";

        final File[] files = new File(inputDir).listFiles(f -> f.getName().endsWith(".xml"));
        for (File file : files) {


            String baseFileName = FilenameUtils.getBaseName(file.getName());
            int i = baseFileName.lastIndexOf("_");
            String bugId = baseFileName.substring(i + 1);
            String appNameVersion = baseFileName.substring(0, i);

            int j = appNameVersion.lastIndexOf("#");

            String appName = appNameVersion.substring(0, j);

            //---------------------------

            TextInstance bugInstance = new TextInstance(appName, bugId, "0");
            System.out.println("Processing " + bugInstance + " ");

            ShortLabeledBugReport bugRep = XMLHelper.readXML(ShortLabeledBugReport.class, file);

            processBug(bugRep, bugInstance, true);
        }

        String outFile = Paths.get(outputDir, "gold-set-SP-F.csv").toString();
        writeGoldSet(outFile);
    }
}
