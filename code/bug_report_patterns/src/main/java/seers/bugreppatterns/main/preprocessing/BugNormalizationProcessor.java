package seers.bugreppatterns.main.preprocessing;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seers.appcore.utils.JavaUtils;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportDescription;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence;
import seers.bugrepcompl.utils.DataReader;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

import java.util.*;

/**
 * Pre-processing tasks:
 * 1. encode links, exceptions, and identifiers with special tags (see BugContentCategory)
 * 2. merge sentences labeled with special tags (see BugContentCategory)
 */
public class BugNormalizationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BugNormalizationProcessor.class);
    static String DUMMY_TOKEN = "℘";

  /*  private static String xmlBugDir = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\replication_package_fse17" +
            "\\1_data\\2_preprocessed_data\\0_content_tagging_preprop-10302018";
    private static String goldSetFile = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\generated_goldsets\\bug_list" +
            ".csv";
    private static String outputFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\replication_package_fse17" +
            "\\1_data\\2_preprocessed_data\\0_content_tagging_preprop2-10302018";*/
    private static String xmlBugDir = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\1_data\\1_preprocessed_data" +
          "\\0_content_tagging-01102019";
    private static String goldSetFile = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\1_data\\bug_list.csv";
    private static String outputFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\1_data\\1_preprocessed_data" +
            "\\1_content_tagging_prep-01102019";


    private static Set<String> allowedSystems = JavaUtils.getSet(
            "argouml", "jedit",
            "openoffice",
            //"docker",
             "eclipse",
            "facebook", "firefox",
//            "hibernate", "httpd",
            "libreoffice", "openmrs",
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

                ShortLabeledBugReport bugReport = BugCodePreprocessor.readBug(xmlBugDir, bugInstance);
                ShortLabeledBugReport bugPreprocessed = new ShortLabeledBugReport(bugReport);
                preProcessBug(bugInstance, bugPreprocessed);

                BugCodePreprocessor.writeBug(bugPreprocessed, outputFolder, bugInstance);

            } catch (Exception e) {
                System.err.println("Error for " + bugInstance);
                e.printStackTrace();
            }

        }

    }

    private static void preProcessBug(TextInstance bugInstance, ShortLabeledBugReport bugRep) {

        encodeSpecialTokens(bugInstance, bugRep);
        mergeSentences(bugInstance, bugRep);

    }

    private static void encodeSpecialTokens(TextInstance bugInstance, ShortLabeledBugReport bugRep) {

        final String titleTxt = bugRep.getTitle().getValue();
        final String encodeTitleTxt = encodeSpecialTokens(bugInstance, titleTxt);
        bugRep.getTitle().setValue(encodeTitleTxt);

        final ShortLabeledBugReportDescription description = bugRep.getDescription();

        final List<ShortLabeledDescriptionSentence> allSentences = description.getAllSentences();
        for (ShortLabeledDescriptionSentence sentence : allSentences) {
            final String text = sentence.getValue();
            final String encodedText = encodeSpecialTokens(bugInstance, text);

            if (!text.equals(encodedText)) {
                LOGGER.debug(String.format("%s: %s -> %s", bugInstance.toString(), text, encodedText));
                sentence.setValue(encodedText);
            }
        }
    }

    enum BugContentCategoryShort {
        ID, EX, UR;

        public String getTagText() {
            return "[" + this + "]";
        }

        public String getRealCategoryTag() {
            BugContentCategory cat = null;
            switch (this) {
                case ID:
                    cat = BugContentCategory.IDENTIFIER;
                    break;
                case EX:
                    cat = BugContentCategory.EXCEPTION;
                    break;
                case UR:
                    cat = BugContentCategory.URL;
                    break;
            }
            return cat.getTagText();
        }
    }

    private static String encodeSpecialTokens(TextInstance bugInstance, String text) {

        if (BugContentCategory.isValidTag(text))
            return text;

        final List<Sentence> sentences = TextProcessor.preprocessText(text, null, new String[]{});
        final List<Token> allTokens = TextProcessor.getAllTokens(sentences);

        final UrlValidator urlValidator =
                new UrlValidator(UrlValidator.ALLOW_ALL_SCHEMES + UrlValidator.ALLOW_2_SLASHES + UrlValidator.ALLOW_LOCAL_URLS);

        StringBuilder builder = new StringBuilder(text);
        boolean changed = false;
        for (Token token : allTokens) {
            final String word = token.getWord();

            if (urlValidator.isValid(word)) {
                changed = changed | replaceInString(builder, token, BugContentCategoryShort.UR);
            } else {

                if (!word.matches("[a-zA-Z]+") && !word.matches("[a-zA-Z]+(([_.])[a-zA-Z]+)+")) continue;

                final String[] split = StringUtils.splitByCharacterTypeCamelCase(word);

                if (split.length == 1) {
                    continue;
                }

                if (Arrays.asList(split).contains("Exception")) {
                    changed = changed | replaceInString(builder, token, BugContentCategoryShort.EX);
                } else {
                    changed = changed | replaceInString(builder, token, BugContentCategoryShort.ID);
                }
            }
        }

        if (!changed) return text;

        String newText = builder.toString().replace(DUMMY_TOKEN, "");
        for (BugContentCategoryShort cat : BugContentCategoryShort.values()) {
            newText = newText.replace(cat.getTagText(), cat.getRealCategoryTag());
        }
        return newText;
    }


    private static boolean replaceInString(StringBuilder builder, Token token, BugContentCategoryShort cat) {
        int length = token.getEndPosition() - token.getBeginPosition();
        String tagText = cat.getTagText();
        if (length > tagText.length()) {
            tagText = tagText + StringUtils.repeat(DUMMY_TOKEN, length - tagText.length());
        } else {
            LOGGER.debug("Token longer than tag: " + token.getWord() + " vs. " + tagText);
            return false;
        }
        builder.replace(token.getBeginPosition(), token.getEndPosition(), tagText);
        return true;
    }

    private static void mergeSentences(TextInstance bugInstance, ShortLabeledBugReport bugReport) {

        List<ShortLabeledDescriptionParagraph> paragraphsToRemove = new ArrayList<>();
        List<ShortLabeledDescriptionParagraph> paragraphs = bugReport.getDescription().getParagraphs();

        for (ShortLabeledDescriptionParagraph par : paragraphs) {

            if (par == null || par.getSentences() == null) {
                paragraphsToRemove.add(par);
                continue;
            }


            List<ShortLabeledDescriptionSentence> sentencesToRemove = new ArrayList<>();
            List<ShortLabeledDescriptionSentence> sentences = par.getSentences();

            ShortLabeledDescriptionSentence baseSent = sentences.get(0);
            BugContentCategory baseCat = BugContentCategory.getTag(baseSent.getValue());
            for (int i = 1; i < sentences.size(); i++) {

                final ShortLabeledDescriptionSentence currSent = sentences.get(i);
                BugContentCategory curCat = BugContentCategory.getTag(currSent.getValue());

                if (baseCat == null) {
                    if (curCat != null) {
                        baseCat = curCat;
                    }
                } else {
                    if (baseCat.equals(curCat)) {
                        sentencesToRemove.add(currSent);
                    }
                    baseCat = curCat;

                }


            }

            sentences.removeAll(sentencesToRemove);

            if (sentences.isEmpty()) {
                paragraphsToRemove.add(par);
            }
        }

        paragraphs.removeAll(paragraphsToRemove);
    }
}
