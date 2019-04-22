package seers.bugrepcompl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.codingparse.*;
import seers.bugrepcompl.entity.shortcodingparse.*;
import seers.bugrepcompl.taggedtext.AllSentencesExtractor;
import seers.bugrepcompl.taggedtext.CodedInfo;
import seers.bugrepcompl.taggedtext.CodedTextFragment;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

public class MainBratToXMLCodingConverter {


    public static void main(String[] args) throws Exception {
        String baseFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\2_data_tse\\0_selected_data_test";
        String outFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\2_data_tse\\1_labeled_data_test";


        Collection<File> codingFiles = FileUtils.listFiles(new File(baseFolder), new String[]{"ann", "xml"}, true);
        final List<File> filesList = new LinkedList<>(codingFiles);

        //sort so that we have xml files first
     /*   filesList.sort((f1, f2) -> {
            final String e1 = FilenameUtils.getExtension(f1.getName());
            final String e2 = FilenameUtils.getExtension(f2.getName());

            if (e1.equalsIgnoreCase("xml") && e2.equalsIgnoreCase("ann"))
                return -1;
            if (e1.equalsIgnoreCase("ann") && e2.equalsIgnoreCase("xml"))
                return 1;
            return 0;
        });*/

        //-----------------------------------------

        AllSentencesExtractor extractor = new AllSentencesExtractor();

        for (File codingFile : codingFiles) {

            System.out.println(codingFile);

            final String extension = FilenameUtils.getExtension(codingFile.getName());
            final String system = codingFile.getParentFile().getName();
            final String bugId = FilenameUtils.getBaseName(codingFile.getName());
            if (extension.equalsIgnoreCase("ann")) {

                File bugTextFile = Paths.get(baseFolder, system, bugId + ".txt").toFile();
                List<List<CodedTextFragment>> codedText = extractor.getCodedText2(codingFile, bugTextFile);
/*
                for (List<CodedTextFragment> codedTextFragment : codedText) {
                    for (CodedTextFragment textFragment : codedTextFragment) {

                        System.out.println(textFragment);
                    }
                    System.out.println();
                }*/

                final ShortLabeledBugReport bugReport = getXMLCodedBugReport(bugId, codedText);

                final File sysFolder = Paths.get(outFolder, system).toFile();
                sysFolder.mkdirs();
                File outFile = Paths.get(outFolder, system, bugId + ".parse.xml").toFile();
                XMLHelper.writeXML(bugReport, outFile);
            }

            if (extension.equalsIgnoreCase("xml")) {

                File outFile = Paths.get(outFolder, system, bugId + ".parse.xml").toFile();
                FileUtils.copyFile(codingFile, outFile);
            }

        }

    }


    private static ShortLabeledBugReport getXMLCodedBugReport(String bugId, List<List<CodedTextFragment>> codedText) {
        // bug report paragraph
        List<ShortLabeledDescriptionParagraph> paragraphs = new ArrayList<>();
        ShortLabeledBugReportTitle title = null;

        int paragId = 1;
        for (List<CodedTextFragment> codedTextFragment : codedText) {
            int sentId = 1;

            List<ShortLabeledDescriptionSentence> sentences = new ArrayList<>();

            for (CodedTextFragment textFragment : codedTextFragment) {


                Set<CodedInfo> infoTypes = textFragment.infoTypes;
                String ob = "";
                String eb = "";
                String sr = "";
                if (infoTypes.contains(CodedInfo.OB))
                    ob = "x";
                if (infoTypes.contains(CodedInfo.EB))
                    eb = "x";
                if (infoTypes.contains(CodedInfo.S2R))
                    sr = "x";

                if (textFragment.isTitle) {
                    if (title != null)
                        throw new RuntimeException();
                    title = new ShortLabeledBugReportTitle(ob, eb, sr, textFragment.text);
                } else {
                    sentences.add(new ShortLabeledDescriptionSentence(ob, eb, sr, paragId + "." + (sentId++),
                            textFragment.text));
                }
            }

            if (sentences.isEmpty()) continue;

            ShortLabeledDescriptionParagraph paragraph = new ShortLabeledDescriptionParagraph();
            paragraph.setId(String.valueOf(paragId));
            paragraph.setSentences(sentences);

            paragraphs.add(paragraph);
            paragId++;


        }

        if (title == null)
            throw new RuntimeException();

        // complete the creation of the bug report
        ShortLabeledBugReportDescription description = new ShortLabeledBugReportDescription();
        description.setParagraphs(paragraphs);

        return new ShortLabeledBugReport(bugId, title, description);
    }

}
