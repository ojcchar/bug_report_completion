package seers.bugrepcompl.taggedtext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class TaggedTextExtractor {

    public abstract List<CodedTextFragment> getCodedText(File bugAnnFile, File bugTextFile) throws
            Exception;


    protected List<TagIndexes> getTagIndexes(List<String> codedLines) {
        List<AllSentencesExtractor.TagIndexes> indexes = new ArrayList<>();

        codedLines.forEach(codedLine -> {

            String[] lineSections = codedLine.split("\t");
            int i = lineSections[1].indexOf(" ");
            String tag = lineSections[1].substring(0, i);
            String[] indicesPairs = lineSections[1].substring(i + 1).split(";");

            for (String indices : indicesPairs) {

                String[] split = indices.split(" ");
                int iniIdx = Integer.valueOf(split[0]);
                int endIdx = Integer.valueOf(split[1]);

                indexes.add(new AllSentencesExtractor.TagIndexes(tag, iniIdx, endIdx));
            }

        });
        return indexes;
    }

    public static class TagIndexes {
        String tag;
        int iniIdx;
        int endIdx;

        public TagIndexes(String tag, int iniIdx, int endIdx) {
            this.tag = tag;
            this.iniIdx = iniIdx;
            this.endIdx = endIdx;
        }
    }
}
