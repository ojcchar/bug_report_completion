package seers.bugreppatterns.main.statistics;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportDescription;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DataExtractorForStats {

    private static final String BUGS_FOLDER = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\1_data" +
            "\\1_preprocessed_data\\1_content_tagging_prep-01102019";
    private static final boolean PERFORM_LABEL_PROPAGATION = false;

    public static void main(String[] args) throws Exception {

        StringBuilder overallBuilder = new StringBuilder();
        StringBuilder paragraphsBuilder = new StringBuilder();
        StringBuilder bugsBuilder = new StringBuilder();

        final File[] systems = new File(BUGS_FOLDER).listFiles(File::isDirectory);
        for (File system : systems) {

            final File[] bugFiles = system.listFiles(f -> !f.isDirectory() && f.getName().endsWith(".xml"));
            // System.out.println(system.getName() + ";" + bugFiles.length);
            for (File bugFile : bugFiles) {

//                System.out.println(bugFile);

                List<String> tagSequenceBug = new ArrayList<>();
                Set<String> uniqueTagsBug = new HashSet<>();

                final ShortLabeledBugReport br = XMLHelper.readXML(ShortLabeledBugReport.class, bugFile);
                final ShortLabeledBugReportDescription description = br.getDescription();

                if (description == null) continue;

                final List<ShortLabeledDescriptionParagraph> paragraphs = description.getParagraphs();

                if (paragraphs == null) continue;

                for (ShortLabeledDescriptionParagraph paragraph : paragraphs) {
                    final List<ShortLabeledDescriptionSentence> sentences = paragraph.getSentences();

                    List<String> tagSequenceParag = new ArrayList<>();
                    Set<String> uniqueTagsParag = new HashSet<>();

                    for (ShortLabeledDescriptionSentence sentence : sentences) {


                        final boolean isOBEmpty = StringUtils.isEmpty(sentence.getOb());
                        final boolean isEBEmpty = StringUtils.isEmpty(sentence.getEb());
                        final boolean isS2REmpty = StringUtils.isEmpty(sentence.getSr());

                        final String obTag;
                        final String ebTag;
                        final String s2rTag;

                        if (PERFORM_LABEL_PROPAGATION) {

                            //label propagation
                            //------------------------

                            boolean isSentenceNotTagged = isOBEmpty && isEBEmpty && isS2REmpty;

                            obTag = !isOBEmpty
                                    || (isSentenceNotTagged && !StringUtils.isEmpty(paragraph.getOb()))
                                    ? "ob" : "";
                            ebTag = !isEBEmpty
                                    || (isSentenceNotTagged && !StringUtils.isEmpty(paragraph.getEb()))
                                    ? "eb" : "";
                            s2rTag = !isS2REmpty
                                    || (isSentenceNotTagged && !StringUtils.isEmpty(paragraph.getSr()))
                                    ? "s2r" : "";
                        } else {
                            obTag = !isOBEmpty ? "ob" : "";
                            ebTag = !isEBEmpty ? "eb" : "";
                            s2rTag = !isS2REmpty ? "s2r" : "";
                        }

                        //--------------------------

                        final List<String> strings = new ArrayList<>(Arrays.asList(obTag, ebTag, s2rTag));
                        strings.removeAll(Collections.singleton(""));
                        LinkedHashSet<String> tagSet = new LinkedHashSet<>(strings);
                        final String tags;
                        if (tagSet.size() > 1) {
                            tags = String.join("-", tagSet);
                        } else {
                            tags = String.join("", tagSet);
                        }

                        if (!String.join("", obTag, ebTag, s2rTag).isEmpty()) {
                            tagSequenceParag.add(String.format("(%s)", tags));
                            uniqueTagsParag.add(obTag);
                            uniqueTagsParag.add(ebTag);
                            uniqueTagsParag.add(s2rTag);
                        } else {
                            tagSequenceParag.add("o");
                            uniqueTagsParag.add("o");
                        }

                        final List<String> fields = Arrays.asList(system.getName(), br.getId(),
                                String.join(",", system.getName(), br.getId()),
                                paragraph.getId(),
                                String.join(",", system.getName(), br.getId(), paragraph.getId()),
                                sentence.getId(), obTag, ebTag, s2rTag,
                                tags
                        );
                        final String line = String.join("\";\"", fields);
                        overallBuilder.append("\"");
                        overallBuilder.append(line);
                        overallBuilder.append("\"");
                        overallBuilder.append("\n");
                    }

                    //---------------

                    tagSequenceBug.addAll(tagSequenceParag);
                    uniqueTagsBug.addAll(uniqueTagsParag);

                    //-----------------------
                    final ArrayList<String> uniTagsList = new ArrayList<>(uniqueTagsParag);
                    uniTagsList.sort(String::compareTo);
                    uniTagsList.removeAll(Collections.singleton(""));

                    final List<String> fields = Arrays.asList(system.getName(), br.getId(),
                            String.join(",", system.getName(), br.getId()),
                            paragraph.getId(),
                            String.join(",", system.getName(), br.getId(), paragraph.getId()),
                            String.join("", tagSequenceParag),
                            String.join("", getTagRegex(tagSequenceParag)),
                            String.join("-", uniTagsList)
                    );

                    final String line = String.join("\";\"", fields);
                    paragraphsBuilder.append("\"");
                    paragraphsBuilder.append(line);
                    paragraphsBuilder.append("\"");
                    paragraphsBuilder.append("\n");
                }


                //-----------------------
                final ArrayList<String> uniTagsList = new ArrayList<>(uniqueTagsBug);
                uniTagsList.sort(String::compareTo);
                uniTagsList.removeAll(Collections.singleton(""));

                final List<String> fields = Arrays.asList(system.getName(), br.getId(),
                        String.join(",", system.getName(), br.getId()),
//                        String.join("", tagSequenceBug),
                        String.join("", getTagRegex(tagSequenceBug)),
                        String.join("-", uniTagsList)
                );

                final String line = String.join("\";\"", fields);
                bugsBuilder.append("\"");
                bugsBuilder.append(line);
                bugsBuilder.append("\"");
                bugsBuilder.append("\n");
            }

        }

        File outFile = Paths.get(BUGS_FOLDER, "all_par_sentences.csv").toFile();
        FileUtils.write(outFile, overallBuilder.toString(), Charset.defaultCharset());

        outFile = Paths.get(BUGS_FOLDER, "all_par_sentences_seq.csv").toFile();
        FileUtils.write(outFile, paragraphsBuilder.toString(), Charset.defaultCharset());

        outFile = Paths.get(BUGS_FOLDER, "all_par_sentences_seq_bugs.csv").toFile();
        FileUtils.write(outFile, bugsBuilder.toString(), Charset.defaultCharset());
    }

    private static List<String> getTagRegex(List<String> tagSequence) {

        if (tagSequence.isEmpty()) return tagSequence;

        int currentCount = 1;

        List<ImmutablePair<String, Integer>> counts = new ArrayList<>();

        for (int i = 0; i < tagSequence.size() - 1; i++) {

            final String currSeq = tagSequence.get(i);
            final String nextSeq = tagSequence.get(i + 1);

            if (nextSeq.equals(currSeq)) {
                currentCount++;
            } else {
                counts.add(new ImmutablePair<>(currSeq, currentCount));
                currentCount = 1;
            }
        }
        counts.add(new ImmutablePair<>(tagSequence.get(tagSequence.size() - 1), currentCount));


        return counts.stream().map(c -> {
            return c.left + "+";

           /* if ("o".equals(c.left))
                return c.left + "+";

            if (c.right == 1)
                return c.left;
            else
                return c.left + "2+";*/
        }).collect(Collectors.toList());
    }
}
