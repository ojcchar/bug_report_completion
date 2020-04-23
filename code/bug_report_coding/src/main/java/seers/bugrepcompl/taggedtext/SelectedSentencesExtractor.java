package seers.bugrepcompl.taggedtext;

import org.apache.commons.io.FileUtils;
import seers.appcore.utils.JavaUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class SelectedSentencesExtractor extends TaggedTextExtractor {

    private List<TagIndexes> indexes;

    @Override
    public List<CodedTextFragment> getCodedText(File bugAnnFile, File bugTextFile) throws Exception {

        String bugText = FileUtils.readFileToString(bugTextFile, Charset.defaultCharset());
        List<Sentence> sentences = getSentences(bugText);

        List<String> annLines = FileUtils.readLines(bugAnnFile, Charset.defaultCharset());
        List<String> codedLines = annLines.stream().filter(l -> !l.trim().isEmpty() && l.startsWith("T")).collect
                (Collectors.toList());

        List<TagIndexes> indexes = getTagIndexes(codedLines);

        indexes.sort(Comparator.comparingInt(i -> i.iniIdx));


        HashMap<Integer, CodedTextFragment> codedText = new LinkedHashMap<>();
        addText(indexes, codedText, sentences);

        return new ArrayList<>(codedText.values());
    }

    public List<Sentence> getSentences(String bugText) {
        List<Sentence> sentences = new ArrayList<>();

        int startIdx = 0;
        final String newLine = "\n";
        int endIdx = bugText.indexOf(newLine, startIdx);
        if (endIdx == -1) {
            endIdx = bugText.length();
        }

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
            }

            startIdx = endIdx + newLine.length();
            endIdx = bugText.indexOf(newLine, startIdx);
        }

        return sentences;
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

    private void addText( List<TagIndexes> indexes, HashMap<Integer, CodedTextFragment> codedText,
                         List<Sentence> sentences) {

        int fragmentId = 1;
        for (TagIndexes indices : indexes) {

            String tag = indices.tag;
            int iniIdx = indices.iniIdx;
            int endIdx = indices.endIdx;

            int sentIdx = indexOfSentence(sentences, iniIdx, endIdx);
            if (sentIdx == -1)
                continue;

            CodedInfo infoType = null;
            if ("Observed-Behavior".equalsIgnoreCase(tag)) {
                infoType = CodedInfo.OB;
            } else if ("Steps-to-Reproduce".equalsIgnoreCase(tag)) {
                infoType = CodedInfo.S2R;
            } else if ("Expected-Behavior".equalsIgnoreCase(tag)) {
                infoType = CodedInfo.EB;
            }

            if (infoType == null)
                throw new RuntimeException("Could not map the tag: " + tag);

            Sentence sentence = sentences.get(sentIdx);

            CodedTextFragment fragment = codedText.get(sentIdx);
            if (fragment == null) {
                fragment = new CodedTextFragment(fragmentId++, sentence.isTitle, sentence.text, JavaUtils.getSet());
                codedText.put(sentIdx, fragment);
            }
            fragment.infoTypes.add(infoType);


        }
    }

    private int indexOfSentence(List<Sentence> sentences, int iniIdx, int endIdx) {
        for (int i = 0; i < sentences.size(); i++) {
            Sentence sentence = sentences.get(i);
            if ((iniIdx >= sentence.startIdx && iniIdx <= sentence.endIdx))
                return i;

        }
        return -1;
    }

}
