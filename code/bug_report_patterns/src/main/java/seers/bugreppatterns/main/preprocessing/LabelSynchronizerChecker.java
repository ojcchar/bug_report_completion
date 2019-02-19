package seers.bugreppatterns.main.preprocessing;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seers.appcore.utils.JavaUtils;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence;
import seers.bugrepcompl.utils.DataReader;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * It detects coding inconsistencies between paragraphs and sentences in the updated bug set
 */
public class LabelSynchronizerChecker {


    private static final Logger LOGGER = LoggerFactory.getLogger(LabelSynchronizerChecker.class);

    private static String goldSetFile = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\generated_goldsets\\bug_list" +
            ".csv";
    /*private static String labelCodingFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus" +
            "\\replication_package_fse17" +
            "\\1_data\\2_preprocessed_data\\0_content_tagging_preprop2-10302018";*/
    private static String labelCodingFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus" +
            "\\replication_package_fse17" +
            "\\1_data\\2_preprocessed_data\\4_content_tagging_preprop2_labels_fixed-10302018";


    private static Set<String> allowedSystems = JavaUtils.getSet(
            "docker"
            , "eclipse",
            "facebook", "firefox", "hibernate", "httpd", "libreoffice", "openmrs",
            "wordpress-android"
    );


    public static void main(String[] args) throws Exception {

        HashMap<TextInstance, Labels> goldSet = DataReader.readGoldSet(goldSetFile);

        Set<TextInstance> bugInstances = goldSet.keySet();

        for (TextInstance bugInstance : bugInstances) {

            String project = bugInstance.getProject();
            if (!allowedSystems.contains(project)) {
                continue;
            }

            try {

                //  LOGGER.debug("Processing " + bugInstance );

                ShortLabeledBugReport bugReport = BugCodePreprocessor.readBug(labelCodingFolder, bugInstance);
                ShortLabeledBugReport labeledBug = new ShortLabeledBugReport(bugReport);

                syncAndCheckBug(bugInstance, labeledBug);

            } catch (Exception e) {
                System.err.println("Error for " + bugInstance);
                e.printStackTrace();
            }

        }

    }

    private static void syncAndCheckBug(TextInstance bugInstance, ShortLabeledBugReport labeledBug) {
        checkLabelInconsistencies(bugInstance, labeledBug);
    }

    private static void checkLabelInconsistencies(TextInstance bugInstance, ShortLabeledBugReport labeledBug) {

        final List<ShortLabeledDescriptionParagraph> paragraphs = labeledBug.getDescription().getParagraphs();

        for (ShortLabeledDescriptionParagraph paragraph : paragraphs) {

            Labels inconsistentLabels = getInconsistentLabels(paragraph);
            if (inconsistentLabels != null) {
                TextInstance parInstance = new TextInstance(bugInstance.getProject(), bugInstance.getBugId(),
                        paragraph.getId());
                LOGGER.debug(String.format("%s: %s", parInstance, inconsistentLabels));
            }

            ///----------------


            //checkSentences(bugInstance, paragraph);
        }
    }

    private static Labels getInconsistentLabels(ShortLabeledDescriptionParagraph paragraph) {

        if (StringUtils.isEmpty(paragraph.getOb()) && StringUtils.isEmpty(paragraph.getEb()) && StringUtils.isEmpty(paragraph.getSr()))
            return null;

        boolean ob =
                (!StringUtils.isEmpty(paragraph.getOb()) &&
                        paragraph.getSentences().stream().anyMatch(s -> StringUtils.isEmpty(s.getOb())))
                        && paragraph.getSentences().stream().anyMatch(s -> !StringUtils.isEmpty(s.getOb()));
        boolean eb =
                (!StringUtils.isEmpty(paragraph.getEb()) &&
                        paragraph.getSentences().stream().anyMatch(s -> StringUtils.isEmpty(s.getEb())))
                        && paragraph.getSentences().stream().anyMatch(s -> !StringUtils.isEmpty(s.getEb()));
        boolean s2r =
                (!StringUtils.isEmpty(paragraph.getSr()) &&
                        paragraph.getSentences().stream().anyMatch(s -> StringUtils.isEmpty(s.getSr())))
                        && paragraph.getSentences().stream().anyMatch(s -> !StringUtils.isEmpty(s.getSr()));

        if (ob || eb || s2r)
            return new Labels(ob ? "x" : "", eb ? "x" : "", s2r ? "x" : "");

        return null;

    }

    private static void checkSentences(TextInstance bugInstance, ShortLabeledDescriptionParagraph paragraph) {
        final List<ShortLabeledDescriptionSentence> sentences = paragraph.getSentences();
        for (ShortLabeledDescriptionSentence sentence : sentences) {

            Labels inconsistentLabels = getInconsistentLabels(paragraph, sentence);
            if (inconsistentLabels != null) {
                TextInstance sentInstance = new TextInstance(bugInstance.getProject(), bugInstance.getBugId(),
                        sentence.getId());
                LOGGER.debug(String.format("%s: %s", sentInstance, inconsistentLabels));
            }

        }
    }

    private static Labels getInconsistentLabels(ShortLabeledDescriptionParagraph paragraph,
                                                ShortLabeledDescriptionSentence sentence) {

        if (StringUtils.isEmpty(paragraph.getOb()) && StringUtils.isEmpty(paragraph.getEb()) && StringUtils.isEmpty(paragraph.getSr()))
            return null;

        boolean ob = StringUtils.isEmpty(paragraph.getOb()) == StringUtils.isEmpty(sentence.getOb());
        boolean eb = StringUtils.isEmpty(paragraph.getEb()) == StringUtils.isEmpty(sentence.getEb());
        boolean s2r = StringUtils.isEmpty(paragraph.getSr()) == StringUtils.isEmpty(sentence.getSr());

        if (ob && eb && s2r) return null;

        return new Labels(ob ? "" : "x", eb ? "" : "x", s2r ? "" : "x");
    }

}
