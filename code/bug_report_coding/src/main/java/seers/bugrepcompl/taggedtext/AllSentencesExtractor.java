package seers.bugrepcompl.taggedtext;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class AllSentencesExtractor extends TaggedTextExtractor {

    @Override
    public List<CodedTextFragment> getCodedText(File bugAnnFile, File bugTextFile) throws Exception {
        return null;
    }

    public List<List<CodedTextFragment>> getCodedText2(File bugAnnFile, File bugTextFile) throws Exception {

        String bugText = FileUtils.readFileToString(bugTextFile, Charset.defaultCharset());
        List<List<Sentence>> paragraphs = getParagraphs(bugText);

        List<String> annLines = FileUtils.readLines(bugAnnFile, Charset.defaultCharset());
        List<String> codedLines = annLines.stream().filter(l -> !l.trim().isEmpty() && l.startsWith("T")).collect
                (Collectors.toList());
        List<TagIndexes> indexes = getTagIndexes(codedLines);

        indexes.sort(Comparator.comparingInt(i -> i.iniIdx));

        HashMap<Integer, List<CodedTextFragment>> codedText = new LinkedHashMap<>();
        addText(indexes, codedText, paragraphs);

        return new ArrayList<>(codedText.values());
    }

    public List<List<Sentence>> getParagraphs(String bugText) {
        List<List<Sentence>> paragraphs = new ArrayList<>();

        int startIdx = 0;
        final String newLine = "\n";
        int endIdx = bugText.indexOf(newLine, startIdx);
        if (endIdx == -1) {
            endIdx = bugText.length();
        }

        List<Sentence> sentences = new ArrayList<>();
        boolean isTitle = true;
        while (endIdx != -1) {
            String text = bugText.substring(startIdx, endIdx);
            if (!text.isEmpty()) {
                if ("BUG REPORT DESCRIPTION:".equals(text)) {
                    isTitle = false;
                } else if (!"BUG REPORT TITLE:".equals(text)) {
                    Sentence sentence = new Sentence(text, startIdx, endIdx, isTitle);
                    sentences.add(sentence);
                }
            } else {
                paragraphs.add(sentences);
                sentences = new ArrayList<>();
            }

            startIdx = endIdx + newLine.length();
            endIdx = bugText.indexOf(newLine, startIdx);
        }

        if (!sentences.isEmpty())
            paragraphs.add(sentences);

        return paragraphs;
    }

    public static class Sentence {
        String text;
        int startIdx;
        int endIdx;
        boolean isTitle;

        public Sentence(String text, int startIdx, int endIdx, boolean isTitle) {
            this.text = text;
            this.startIdx = startIdx;
            this.endIdx = endIdx;
            this.isTitle = isTitle;
        }
    }

    private void addText(List<TagIndexes> indexes, HashMap<Integer, List<CodedTextFragment>> codedText,
                         List<List<Sentence>> paragraphs) {

        int fragId = 1;
        for (int i = 0; i < paragraphs.size(); i++) {

            final List<Sentence> sentences = paragraphs.get(i);
            List<CodedTextFragment> codedTexts = new ArrayList<>();

            for (Sentence sentence : sentences) {
                Set<CodedInfo> infoTypes = getInfoTypes(sentence, indexes);
                if (infoTypes.isEmpty())
                    infoTypes.add(CodedInfo.OTHER);
                codedTexts.add(new CodedTextFragment(fragId++, sentence.isTitle, sentence.text, infoTypes));
            }

            codedText.put(i, codedTexts);
        }


    }

    private Set<CodedInfo> getInfoTypes(Sentence sentence, List<TagIndexes> indexes) {
        return indexes.stream().filter(i -> isValid(i.iniIdx, i.endIdx, sentence)).map(i ->
                {

                    String tag = i.tag;
                    CodedInfo infoType = null;
                    if ("Observed-Behavior".equalsIgnoreCase(tag)) {
                        infoType = CodedInfo.OB;
                    } else if ("Steps-to-Reproduce".equalsIgnoreCase(tag)) {
                        infoType = CodedInfo.S2R;
                    } else if ("Expected-Behavior".equalsIgnoreCase(tag)) {
                        infoType = CodedInfo.EB;
                    }
                    return infoType;
                }
        ).collect(Collectors.toSet());
    }

    private boolean isValid(int iniIdx, int endIdx, Sentence sentence) {
        return iniIdx >= sentence.startIdx && iniIdx <= sentence.endIdx || endIdx >= sentence.startIdx && endIdx <= sentence.endIdx;
    }

}
