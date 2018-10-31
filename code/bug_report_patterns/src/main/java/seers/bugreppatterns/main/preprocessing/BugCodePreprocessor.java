package seers.bugreppatterns.main.preprocessing;

import org.apache.commons.lang3.tuple.ImmutablePair;
import seers.appcore.utils.JavaUtils;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence;
import seers.bugrepcompl.utils.DataReader;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

//import seers.bugrepcompl.entity.regularparse.BugReport;
//import seers.bugrepcompl.entity.regularparse.DescriptionParagraph;
//import seers.bugrepcompl.entity.regularparse.DescriptionSentence;

public class BugCodePreprocessor {

    //	//not coded data
//	static String xmlBugDir = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data
// /regular_parsed_data";
//	private static String goldSetFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/gold-set
// -B-all_data.csv";
//	static String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/data/code_preprocessing";

    //coded data (remember to change the imports!!)
    private static String xmlBugDir = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\replication_package_fse17" +
            "\\1_data\\1_coded_data\\0_labeled_data";
    private static String goldSetFile = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\generated_goldsets\\bug_list" +
            ".csv";
    private static String outputFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\replication_package_fse17" +
            "\\1_data\\2_preprocessed_data\\0_content_tagging-10302018";

    //if true, the text that matches the regexes is removed, otherwise the text is tagged with special tags
    private static boolean performRemoval = false;


    //-------------------------------------------------------


    private static Set<String> allowedSystems = JavaUtils.getSet("docker", "eclipse",
            "facebook", "firefox", "hibernate", "httpd", "libreoffice", "openmrs", "wordpress-android");

//      private static Set<String> allowedSystems = JavaUtils.getSet("docker");
    // static HashSet<String> allowedSystems = new
    // HashSet<String>(Arrays.asList(new String[] { "wordpress-android" }));
    //-------------------------------------------------------


    public static void main(String[] args) throws Exception {

        HashMap<TextInstance, Labels> goldSet = DataReader.readGoldSet(goldSetFile);

        Set<TextInstance> bugInstances = goldSet.keySet();

        for (TextInstance bugInstance : bugInstances) {

            String project = bugInstance.getProject();
            if (!allowedSystems.contains(project)) {
                continue;
            }

            try {

                System.out.print("Processing " + bugInstance + " ");

                ShortLabeledBugReport bugReport = readBug(xmlBugDir, bugInstance);
//				BugReport bugReport = POSBugProcessorMain.readBug(xmlBugDir, bugInstance);
                ShortLabeledBugReport bugPreprocessed = new ShortLabeledBugReport(bugReport);
                boolean changed = preprocessBug(bugInstance, bugPreprocessed);

//				POSBugProcessorMain.writeBug(bugPreprocessed, outputFolder, bugInstance);
                writeBug(bugPreprocessed, outputFolder, bugInstance);

                if (changed) {
                    System.out.println(" [changed]");
                } else {
                    System.out.println(" [not changed]");
                }
            } catch (Exception e) {
                System.err.println("Error for " + bugInstance);
                e.printStackTrace();
            }

        }

    }

    public static seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport readBug(String inputFolder,
                                                                                          TextInstance bug) throws Exception {
        String filepath = inputFolder + File.separator + bug.getProject() + File.separator + bug.getBugId()
                + ".parse.xml";
        return XMLHelper.readXML(ShortLabeledBugReport.class, filepath);
    }


    public static void writeBug(seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport bugPreprocessed,
                                String outputFolder, TextInstance bug) throws Exception {
        File projectFolder = new File(outputFolder + File.separator + bug.getProject());
        if (!projectFolder.exists()) {
            projectFolder.mkdirs();
        }

        File outputFile = new File(projectFolder.getAbsolutePath() + File.separator + bug.getBugId() + ".parse.xml");
        XMLHelper.writeXML(bugPreprocessed, outputFile);

    }


    private static boolean preprocessBug(TextInstance bugInstance, ShortLabeledBugReport bugReport) throws Exception {

        List<String> iniSeparators = BugCodeRegexes.systemIniSeparators.get(bugInstance.getProject());

        boolean changed = false;

        if (iniSeparators != null) {

            for (String iniSeparator : iniSeparators) {
                List<CodePair> pairs = findCodingPairs(bugReport, iniSeparator, bugInstance);
                boolean changed2 = removeCode(bugReport, pairs, iniSeparator);
                changed = changed || changed2;

            }
        }

        boolean removeOtherSentences = removeOtherSentences(bugReport, bugInstance);
        changed = changed || removeOtherSentences;

        return changed;

    }

    private static boolean removeOtherSentences(ShortLabeledBugReport bugReport, TextInstance bugInstance) {
        boolean changed = false;


        if (bugReport.getDescription() == null) {
            return false;
        }

        List<ImmutablePair<String, ContentCategory>> generalRegexes =
                BugCodeRegexes.systemRegexes.get(bugInstance.getProject());
        List<ImmutablePair<String, ContentCategory>> startRegexes =
                BugCodeRegexes.systemStartRegexes.get(bugInstance.getProject());
        List<ImmutablePair<String, ContentCategory>> endRegexes =
                BugCodeRegexes.systemEndRegexes.get(bugInstance.getProject());

        //---------------------------------------
        final Function<ImmutablePair<String, ContentCategory>, String> generalRxFn =
                regex -> "(?s).*" + regex.getLeft() + ".*";
        final Function<ImmutablePair<String, ContentCategory>, String> startRxFn =
                regex -> "(?s)^" + regex.getLeft() + ".*";
        final Function<ImmutablePair<String, ContentCategory>, String> endRxFn =
                regex -> "(?s).*" + regex.getLeft() + "$";

        //---------------------------------------

        List<ImmutablePair<ShortLabeledDescriptionParagraph, ContentCategory>> paragraphsToRemove = new ArrayList<>();
        List<ShortLabeledDescriptionParagraph> paragraphs = bugReport.getDescription().getParagraphs();

        for (ShortLabeledDescriptionParagraph par : paragraphs) {

            if (par == null || par.getSentences() == null) {
                paragraphsToRemove.add(new ImmutablePair<>(par, null));
                continue;
            }

            final ContentCategory contentCategory1 = checkParagraph2(par, bugInstance);
            if (contentCategory1 != null) {
                paragraphsToRemove.add(new ImmutablePair<>(par, contentCategory1));
            } else {

                List<ImmutablePair<ShortLabeledDescriptionSentence, ContentCategory>>
                        sentencesToRemove = new ArrayList<>();
                List<ShortLabeledDescriptionSentence> sentences = par.getSentences();
                for (ShortLabeledDescriptionSentence sent : sentences) {

                    ContentCategory contentCategory =
                            findRegexThatMatchesSentence(sent, generalRegexes,
                                    generalRxFn);

                    if (contentCategory != null) {
                        sentencesToRemove.add(new ImmutablePair<>(sent, contentCategory));
                    } else {
                        contentCategory =
                                findRegexThatMatchesSentence(sent, startRegexes,
                                        startRxFn);
                        if (contentCategory != null) {
                            sentencesToRemove.add(new ImmutablePair<>(sent, contentCategory));
                        } else {

                            contentCategory =
                                    findRegexThatMatchesSentence(sent, endRegexes,
                                            endRxFn);
                            if (contentCategory != null) {
                                sentencesToRemove.add(new ImmutablePair<>(sent, contentCategory));
                            }
                        }
                    }
                }

                changed = removeSentences(changed, sentencesToRemove, sentences);

                if (sentences.isEmpty()) {
                    paragraphsToRemove.add(new ImmutablePair<>(par, null));
                }
            }
        }

        changed = removeParagraph(changed, paragraphsToRemove, paragraphs);

        // -----------------------------

        if (paragraphs.isEmpty()) {
            bugReport.setDescription(null);
        }

        return changed;
    }

    private static boolean removeSentences(boolean changed,
                                           List<ImmutablePair<ShortLabeledDescriptionSentence, ContentCategory>> sentencesToRemove,
                                           List<ShortLabeledDescriptionSentence> sentences) {
        if (performRemoval) {
            boolean removeAll = sentences.removeAll(sentencesToRemove.stream()
                    .map(ImmutablePair::getLeft).collect(Collectors.toList()));
            changed = changed || removeAll;
        } else {
            for (ImmutablePair<ShortLabeledDescriptionSentence, ContentCategory> pair : sentencesToRemove) {
                pair.getLeft().setValue(getTagText(pair.getRight()));
                changed = true;
            }
        }
        return changed;
    }

    private static ContentCategory findRegexThatMatchesSentence(ShortLabeledDescriptionSentence sent,
                                                                List<ImmutablePair<String, ContentCategory>> regexes,
                                                                Function<ImmutablePair<String, ContentCategory>,
                                                                        String> regexFunction) {
        if (regexes == null) {
            return null;
        }

        String value = sent.getValue();
        final Optional<ImmutablePair<String, ContentCategory>> matchedRegex =
                regexes.stream().filter(regex -> value.matches(regexFunction.apply(regex))).findFirst();
        return matchedRegex.map(ImmutablePair::getRight).orElse(null);
    }


    private static boolean regexMatchSentence(List<ShortLabeledDescriptionSentence> sents,
                                              String regex,
                                              Function<String, String> regexFunction) {
        return sents.stream().anyMatch(sent -> sent.getValue().matches(regexFunction.apply(regex)));
    }

    private static ContentCategory checkParagraph2(ShortLabeledDescriptionParagraph par, TextInstance bugInstance) {

        List<ImmutablePair<List<String>, ContentCategory>> groupRegexes =
                BugCodeRegexes.systemGroupRegexes.get(bugInstance.getProject());

        //--------------------------------


        for (ImmutablePair<List<String>, ContentCategory> pair : groupRegexes) {
            int numRegx = 0;
            for (String reg : pair.getLeft()) {
                boolean matches = regexMatchSentence(par.getSentences(), reg, regex -> "(?s).*" + regex + ".*");
                if (matches) {
                    numRegx++;
                    if (numRegx > 1) {
                        return pair.getRight();
                    }
                }
            }
        }
        return null;
    }

    private static boolean removeCode(ShortLabeledBugReport bugReport, List<CodePair> pairs, String separator) {

        boolean changed = false;

        List<ImmutablePair<ShortLabeledDescriptionParagraph, ContentCategory>> paragraphsToRemove = new ArrayList<>();
        List<ShortLabeledDescriptionParagraph> paragraphs = bugReport.getDescription().getParagraphs();
        for (ShortLabeledDescriptionParagraph par : paragraphs) {

            if (par == null || par.getSentences() == null) {
                paragraphsToRemove.add(new ImmutablePair<>(par, null));
                continue;
            }

            final ContentCategory contentCategory = checkParagraph(par, pairs);
            if (contentCategory != null) {
                paragraphsToRemove.add(new ImmutablePair<>(par, contentCategory));
            } else {

                List<ImmutablePair<ShortLabeledDescriptionSentence, ContentCategory>> sentencesToRemove =
                        new ArrayList<>();
                List<ShortLabeledDescriptionSentence> sentences = par.getSentences();
                for (ShortLabeledDescriptionSentence sent : sentences) {
                    CodePair pair = checkSentence(sent, pairs);

                    if (pair == null) {
                        continue;
                    }

                    if (!pair.regex) {
                        sentencesToRemove.add(new ImmutablePair<>(sent, pair.tag));
                    } else {
                        //-----------------------------------

                        int indexOf = sent.getValue().indexOf(separator);
                        String newVal = sent.getValue().substring(0, indexOf);
                        if (newVal.trim().isEmpty()) {
                            sentencesToRemove.add(new ImmutablePair<>(sent, pair.tag));
                        } else {
                            sent.setValue(newVal);
                            changed = true;
                        }
                    }
                }

                changed = removeSentences(changed, sentencesToRemove, sentences);

                if (sentences.isEmpty()) {
                    paragraphsToRemove.add(new ImmutablePair<>(par, null));
                }
            }
        }

        changed = removeParagraph(changed, paragraphsToRemove, paragraphs);

        // -----------------------------

        if (paragraphs.isEmpty()) {
            bugReport.setDescription(null);
        }

        return changed;

    }

    private static boolean removeParagraph(boolean changed, List<ImmutablePair<ShortLabeledDescriptionParagraph,
            ContentCategory>> paragraphsToRemove, List<ShortLabeledDescriptionParagraph> paragraphs) {
        if (performRemoval) {
            boolean removeAll = paragraphs.removeAll(paragraphsToRemove.stream()
                    .map(ImmutablePair::getLeft).collect(Collectors.toList()));
            changed = changed || removeAll;
        } else {
            final List<ShortLabeledDescriptionParagraph> parsToDelete =
                    paragraphsToRemove.stream().filter(p -> p.getRight() == null)
                            .map(ImmutablePair::getLeft).collect(Collectors.toList());

            boolean removeAll = paragraphs.removeAll(parsToDelete);
            changed = changed || removeAll;

            //-------------------------------------

            List<ImmutablePair<ShortLabeledDescriptionParagraph, ContentCategory>> parsToChange =
                    paragraphsToRemove.stream().filter(p -> p.getRight() != null)
                            .collect(Collectors.toList());

            for (ImmutablePair<ShortLabeledDescriptionParagraph, ContentCategory> pair : parsToChange) {
                for (ShortLabeledDescriptionSentence sentence : pair.getLeft().getSentences()) {
                    sentence.setValue(getTagText(pair.getRight()));
                    changed = true;
                }
            }
        }
        return changed;
    }

    private static String getTagText(ContentCategory category) {
        return "[" + category + "]";
    }

    private static CodePair checkSentence(ShortLabeledDescriptionSentence sent, List<CodePair> pairs) {
        Integer parId = Integer.valueOf(sent.getId().split("\\.")[0]);
        Integer sentId = Integer.valueOf(sent.getId().split("\\.")[1]);

        for (CodePair pair : pairs) {

            Integer parIniId = Integer.valueOf(pair.iniId.split("\\.")[0]);
            Integer parEndId = Integer.valueOf(pair.endId.split("\\.")[0]);

            if (parIniId < parId && parId < parEndId) {
                return pair;
            } else {
                Integer sentIniId = Integer.valueOf(pair.iniId.split("\\.")[1]);
                Integer sentEndId = Integer.valueOf(pair.endId.split("\\.")[1]);
                if (parIniId.equals(parId) && parIniId.equals(parEndId)) {
                    if (sentIniId <= sentId && sentId <= sentEndId) {
                        return pair;
                    }
                } else {
                    if (parIniId.equals(parId)) {
                        if (sentIniId <= sentId) {
                            return pair;
                        }
                    } else if (parEndId.equals(parId)) {
                        if (sentId <= sentEndId) {
                            return pair;
                        }
                    }
                }
            }

        }

        return null;
    }

    private static ContentCategory checkParagraph(ShortLabeledDescriptionParagraph par, List<CodePair> pairs) {
        Integer parId = Integer.valueOf(par.getId());

        for (CodePair pair : pairs) {
            Integer iniId = Integer.valueOf(pair.iniId.split("\\.")[0]);
            Integer endId = Integer.valueOf(pair.endId.split("\\.")[0]);

            if (iniId > endId) {
                throw new RuntimeException("Incorrect pair: " + pair);
            }

            if (iniId < parId && parId < endId) {
                return pair.tag;
            } else {
                List<ShortLabeledDescriptionSentence> sentences = par.getSentences();
                if (sentences.get(0).getId().equals(iniId.toString())
                        && sentences.get(sentences.size() - 1).getId().equals(iniId.toString())) {
                    return pair.tag;
                }
            }
        }

        return null;
    }

    private static List<CodePair> findCodingPairs(ShortLabeledBugReport bugReport, String separator,
                                                  TextInstance bugInstance) {

        List<ShortLabeledDescriptionSentence> sentences = bugReport.getDescription().getAllSentences();
        List<CodePair> pairs = new ArrayList<>();

        // code to remove the docker template at the beginning of the bug
        if (bugInstance.getProject().equals("docker") && sentences.size() > 10) {
            ShortLabeledDescriptionSentence firstSent = sentences.get(0);
            if (firstSent.getValue().equals("<!")) {

                for (int i = 1; i < sentences.size(); i++) {
                    ShortLabeledDescriptionSentence sent = sentences.get(i);
                    if (sent.getValue().equals("BUG REPORT INFORMATION")) {
                        if (i + 4 <= sentences.size() - 1) {
                            if (sentences.get(i + 4).getValue().equals("-->")) {
                                pairs.add(new CodePair(firstSent.getId(), sentences.get(i + 4).getId(),
                                        false, ContentCategory.PROJ_TEMPLATE));
                                break;
                            }
                        }
                    }
                }

            }

        }

        // -------------------------------

        String iniId = null;
        String endId = null;
        for (ShortLabeledDescriptionSentence sent : sentences) {

            if (sent.getValue().equals(separator)) {

                if (iniId == null) {
                    iniId = sent.getId();
                } else {
                    endId = sent.getId();
                    pairs.add(new CodePair(iniId, endId, false, ContentCategory.SRC_CODE));
                    iniId = null;
                    endId = null;
                }
            } else {
                String separator2 = separator.replace("{", "\\{").replace("}", "\\}");
                if (sent.getValue().matches("(?s).+" + separator2 + ".+" + separator2)) {
                    if (iniId == null) {
                        pairs.add(new CodePair(sent.getId(), sent.getId(), true, ContentCategory.SRC_CODE));
                        iniId = null;
                        endId = null;
                    }
                } else if (sent.getValue().startsWith(separator) && sent.getValue().endsWith(separator)) {
                    pairs.add(new CodePair(sent.getId(), sent.getId(), false, ContentCategory.SRC_CODE));
                    iniId = null;
                    endId = null;
                } else if (sent.getValue().startsWith(separator)) {
                    if (iniId == null) {
                        iniId = sent.getId();
                    }
                } else if (sent.getValue().endsWith(separator)) {
                    if (iniId != null) {
                        endId = sent.getId();
                        pairs.add(new CodePair(iniId, endId, false, ContentCategory.SRC_CODE));
                        iniId = null;
                        endId = null;
                    }
                }
            }
        }

        if (iniId != null || endId != null) {
            System.err.println("Could not find pair: " + iniId + ", " + endId);
        }

        return pairs;
    }

    public static class CodePair {
        String iniId;
        String endId;
        ContentCategory tag;

        boolean regex;

       /* CodePair(String iniId, String endId, boolean regex) {
            this(iniId, endId, regex, null);
        }*/

        CodePair(String iniId, String endId, boolean regex, ContentCategory tag) {
            super();
            if (tag == null)
                throw new RuntimeException("Incorrect tag: " + tag);
            this.iniId = iniId;
            this.endId = endId;
            this.regex = regex;
            this.tag = tag;
        }

        @Override
        public String toString() {
            return "[i=" + iniId + ", e=" + endId + ", t=" + tag + "]";
        }

    }

}
