package seers.bugreppatterns.main.golset;

import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.appcore.utils.JavaUtils;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportDescription;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence;
import seers.bugrepcompl.utils.DataReader;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class SentenceGoldSetMain {

    //no preprocessed data
//	private static String codedDataFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data
// /final_data";

    //preprocessed data
    /*private static String codedDataFolder =
    "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\replication_package_fse17" +
            "\\1_data\\2_preprocessed_data\\4_content_tagging_preprop2_labels_fixed-10302018";

    private static String bugGoldSetFile = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\replication_package_fse17" +
            "\\1_data\\1_coded_data\\2_labeled_data_summary.csv";
    private static String outputFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\generated_goldsets_11082018";*/

    private static String codedDataFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\1_data" +
            "\\1_preprocessed_data\\1_content_tagging_prep-01102019";

    private static String bugGoldSetFile = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\1_data\\bug_list.csv";
    private static String outputFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\1_data\\2_gold_sets";

    static HashMap<TextInstance, Labels> goldSetSentences = new HashMap<>();


    private static Set<String> allowedSystems = JavaUtils.getSet(
            "argouml", "jedit",
            "openoffice",
            "eclipse",
            "facebook", "firefox",
            "libreoffice", "openmrs",
            "wordpress-android"
    );

    public static void main(String[] args) throws Exception {
        HashMap<TextInstance, Labels> goldSet = DataReader.readGoldSet(bugGoldSetFile);

        Set<TextInstance> bugInstances = goldSet.keySet();

        for (TextInstance bugInstance : bugInstances) {
            String project = bugInstance.getProject();
            if (!allowedSystems.contains(project)) {
                continue;
            }
            try {

                System.out.println("Processing " + bugInstance + " ");

                String bugId = bugInstance.getBugId();

                File xmlFile = new File(
                        codedDataFolder + File.separator + project + File.separator + bugId + ".parse.xml");
                ShortLabeledBugReport bugRep = XMLHelper.readXML(ShortLabeledBugReport.class, xmlFile);

                processBug(bugRep, bugInstance, false);

            } catch (Exception e) {
                System.err.println("Error for " + bugInstance);
                e.printStackTrace();
            }

        }

        final String outFile = outputFolder + File.separator + "gold-set-S.csv";
        writeGoldSet(outFile);
    }

    protected static void writeGoldSet(String outFile) throws Exception {

        try (CsvWriter writer = new CsvWriterBuilder(new FileWriter(outFile))
                .separator(';').build();) {

			/*writer.writeNext(Arrays.asList("system", "bug_id", "sys_bug", "instance_id", "sys_bug_instance", "is_ob",
					"is_eb", "is_sr", "used_for_patterns", "bug_type"));*/
            writer.writeNext(Arrays.asList("system", "bug_id", "instance_id", "is_ob",
                    "is_eb", "is_sr", "used_for_patterns"));

            Set<Entry<TextInstance, Labels>> entrySet = goldSetSentences.entrySet();

            for (Entry<TextInstance, Labels> entry : entrySet) {
                TextInstance sentenceInstance = entry.getKey();

				/*String bugType = typeOfIssues.get(new TextInstance(sentenceInstance.getProject(), sentenceInstance
				.getBugId(), "0"));
				if (bugType == null) {
					bugType = "";
				}
				*/

                Labels value2 = entry.getValue();
				/*String[] value = new String[] { sentenceInstance.getProject(), sentenceInstance.getBugId(),
						sentenceInstance.getProject() + ";" + sentenceInstance.getBugId(), sentenceInstance
						.getInstanceId(),
						sentenceInstance.getProject() + ";" + sentenceInstance.getBugId() + ";" + sentenceInstance
						.getInstanceId(), value2.getIsOB(),
						value2.getIsEB(), value2.getIsSR(), value2.getCodedBy(), bugType };*/
                String[] value = new String[]{sentenceInstance.getProject(), sentenceInstance.getBugId(),
                        sentenceInstance.getInstanceId(), value2.getIsOB(),
                        value2.getIsEB(), value2.getIsSR(), value2.getCodedBy()};
                writer.writeNext(Arrays.asList(value));
            }
        }
    }

    protected static void processBug(ShortLabeledBugReport bugRep, TextInstance bugInstance, boolean processTitle) {

        if (processTitle) {
            TextInstance sentenceInstance = new TextInstance(bugInstance.getProject(), bugInstance.getBugId(), "0");
            Labels sentenceLabels = goldSetSentences.get(sentenceInstance);
            if (sentenceLabels != null) {
                throw new RuntimeException("Sentence is repeated: " + "0");
            }
            sentenceLabels = new Labels( bugRep.getTitle().getOb().trim(),  bugRep.getTitle().getEb().trim(),  bugRep.getTitle().getSr().trim());
            goldSetSentences.put(sentenceInstance, sentenceLabels);
        }

        //-----------------------------

        ShortLabeledBugReportDescription description = bugRep.getDescription();

        if (description == null) {
            return;
        }

        List<ShortLabeledDescriptionParagraph> paragraphs = description.getParagraphs();

        if (paragraphs == null) return;

        for (ShortLabeledDescriptionParagraph paragraph : paragraphs) {
            processParagraph(paragraph, bugInstance);
        }

    }

    private static void processParagraph(ShortLabeledDescriptionParagraph paragraph, TextInstance bugInstance) {

        Labels paragraphLabels = new Labels(paragraph.getOb().trim(), paragraph.getEb().trim(),
                paragraph.getSr().trim());

        // --------------------------------------------

        processSentences(paragraph.getSentences(), bugInstance);

        if (!paragraphLabels.getIsOB().isEmpty()) {
            processSentencesOb(paragraph.getSentences(), bugInstance);
        }

        if (!paragraphLabels.getIsEB().isEmpty()) {
            processSentencesEb(paragraph.getSentences(), bugInstance);
        }

        if (!paragraphLabels.getIsSR().isEmpty()) {
            processSentencesSr(paragraph.getSentences(), bugInstance);
        }

    }

    private static void processSentences(List<ShortLabeledDescriptionSentence> sentences, TextInstance bugInstance) {
        for (ShortLabeledDescriptionSentence sentence : sentences) {
            TextInstance sentenceInstance = new TextInstance(bugInstance.getProject(), bugInstance.getBugId(),
                    sentence.getId());
            Labels sentenceLabels = goldSetSentences.get(sentenceInstance);
            if (sentenceLabels != null) {
                throw new RuntimeException("Sentence is repeated: " + sentence.getId());
            }
            sentenceLabels = new Labels(sentence.getOb().trim(), sentence.getEb().trim(), sentence.getSr().trim());
            goldSetSentences.put(sentenceInstance, sentenceLabels);
        }
    }

    private static void processSentencesEb(List<ShortLabeledDescriptionSentence> sentences, TextInstance bugInstance) {
        for (ShortLabeledDescriptionSentence sentence : sentences) {

            TextInstance sentenceInstance = new TextInstance(bugInstance.getProject(), bugInstance.getBugId(),
                    sentence.getId());
            Labels sentenceLabels = goldSetSentences.get(sentenceInstance);
            if (sentenceLabels == null) {
                throw new RuntimeException("Sentence was not read first: " + sentence.getId());
            }
            sentenceLabels.setIsEB("x");
            goldSetSentences.put(sentenceInstance, sentenceLabels);
        }
    }

    private static void processSentencesSr(List<ShortLabeledDescriptionSentence> sentences, TextInstance bugInstance) {
        for (ShortLabeledDescriptionSentence sentence : sentences) {

            TextInstance sentenceInstance = new TextInstance(bugInstance.getProject(), bugInstance.getBugId(),
                    sentence.getId());
            Labels sentenceLabels = goldSetSentences.get(sentenceInstance);
            if (sentenceLabels == null) {
                throw new RuntimeException("Sentence was not read first: " + sentence.getId());
            }
            sentenceLabels.setIsSR("x");
            goldSetSentences.put(sentenceInstance, sentenceLabels);
        }
    }

    private static void processSentencesOb(List<ShortLabeledDescriptionSentence> sentences, TextInstance bugInstance) {
        for (ShortLabeledDescriptionSentence sentence : sentences) {

            TextInstance sentenceInstance = new TextInstance(bugInstance.getProject(), bugInstance.getBugId(),
                    sentence.getId());
            Labels sentenceLabels = goldSetSentences.get(sentenceInstance);
            if (sentenceLabels == null) {
                throw new RuntimeException("Sentence was not read first: " + sentence.getId());
            }
            sentenceLabels.setIsOB("x");
            goldSetSentences.put(sentenceInstance, sentenceLabels);
        }
    }

}
