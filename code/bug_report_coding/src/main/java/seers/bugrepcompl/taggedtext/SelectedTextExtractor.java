package seers.bugrepcompl.taggedtext;

import org.apache.commons.io.FileUtils;
import seers.appcore.utils.JavaUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectedTextExtractor  extends  TaggedTextExtractor{

    @Override
    public List<CodedTextFragment> getCodedText(File bugAnnFile, File bugTextFile) throws Exception {

        String bugText = FileUtils.readFileToString(bugTextFile, Charset.defaultCharset());
        List<String> annLines = FileUtils.readLines(bugAnnFile, Charset.defaultCharset());

        List<String> codedLines = annLines.stream().filter(l -> !l.trim().isEmpty() && l.startsWith("T")).collect
                (Collectors.toList());

        //FIXME: the codedLines are not sorted by line of text
        List<CodedTextFragment> codedText = new LinkedList<>();
        for (String codedLine : codedLines) {

            String[] lineSections = codedLine.split("\t");
            int i = lineSections[1].indexOf(" ");
            String tag = lineSections[1].substring(0, i);
            String[] indices = lineSections[1].substring(i + 1).split(";");

            addText(bugText, tag, indices, codedText);

        }

        return codedText;
    }

    private void addText(String bugText, String tag, String[] indicesPairs, List<CodedTextFragment> codedText) {

        String descriptionLabel = "BUG REPORT DESCRIPTION:";
        int descIniIdx = bugText.indexOf(descriptionLabel);
        int descEndIdx = descIniIdx + descriptionLabel.length();

        int fragmentId = 1;
        for (String indices : indicesPairs) {

            String[] split = indices.split(" ");
            int iniIdx = Integer.valueOf(split[0]);
            int endIdx = Integer.valueOf(split[1]);

            if (invalidIndices(iniIdx, endIdx, descIniIdx, descEndIdx))
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

            boolean isTitle = iniIdx > 17 && iniIdx < descIniIdx && endIdx > 17 && endIdx < descIniIdx;
            String text = bugText.substring(iniIdx, endIdx);
            CodedTextFragment fragment = new CodedTextFragment(fragmentId++, isTitle, text, JavaUtils.getSet(infoType));
            codedText.add(fragment);

        }
    }

    private boolean invalidIndices(int iniIdx, int endIdx, int descIniIdx, int descEndIdx) {
        boolean iniCheck = (iniIdx >= 0 && iniIdx <= 17) || ((iniIdx >= descIniIdx && iniIdx <= descEndIdx));
        boolean endCheck = (endIdx >= 0 && endIdx <= 17) || ((endIdx >= descIniIdx && endIdx <= descEndIdx));
        return iniCheck || endCheck;
    }

}
