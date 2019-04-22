package seers.bug_report_analysis.tools;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CodingFixerToSentencesOnly {

    public static void main(String[] args) {

        String dataFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\2_data_tse\\1_labeled_data";

        final Collection<File> xmlFiles = FileUtils.listFiles(new File(dataFolder), new String[]{"xml"}, true);

        for (File xmlFile : xmlFiles) {
            try {
                ShortLabeledBugReport br = XMLHelper.readXML(ShortLabeledBugReport.class, xmlFile);

                boolean b = processBR(br);
                if (b) {
                        XMLHelper.writeXML(br, xmlFile);
                    System.out.println(xmlFile.getParentFile().getName() + ";" + br.getId() + ";mod");
                } else {
                    //   System.out.println(xmlFile.getParentFile().getName() + ";" + br.getId() + ";no_mod");
                }
            } catch (Exception e) {
                System.err.println("Error for: " + xmlFile);
                e.printStackTrace();
            }
        }

    }

    private static boolean processBR(ShortLabeledBugReport br) {

        boolean modified = false;

        if (br.getDescription() == null) return modified;

        final List<ShortLabeledDescriptionParagraph> paragraphs = br.getDescription().getParagraphs();

        if (paragraphs == null) return modified;

        //-------------------------

        for (ShortLabeledDescriptionParagraph paragraph : paragraphs) {

            final String ob = paragraph.getOb();
            final String eb = paragraph.getEb();
            final String sr = paragraph.getSr();

            if (StringUtils.isEmpty(ob) && StringUtils.isEmpty(eb) && StringUtils.isEmpty(sr)) continue;

            //---------------------------

            final List<ShortLabeledDescriptionSentence> sentences = paragraph.getSentences();
            if (!StringUtils.isEmpty(ob)) {
                final List<ShortLabeledDescriptionSentence> coded =
                        sentences.stream().filter(s -> !StringUtils.isEmpty(s.getOb())).collect(Collectors.toList());
                if (coded.size() == sentences.size()) {
                    paragraph.setOb("");
                    modified = true;
                } else if (sentences.size() == 1 && coded.isEmpty()) {
                    paragraph.setOb("");
                    sentences.forEach(s -> s.setOb("x"));
                    modified = true;
                }
            }

            //---------------------------

            if (!StringUtils.isEmpty(eb)) {
                final List<ShortLabeledDescriptionSentence> coded =
                        sentences.stream().filter(s -> !StringUtils.isEmpty(s.getEb())).collect(Collectors.toList());
                if (coded.size() == sentences.size()) {
                    paragraph.setEb("");
                    modified = true;
                } else if (sentences.size() == 1 && coded.isEmpty()) {
                    paragraph.setEb("");
                    sentences.forEach(s -> s.setEb("x"));
                    modified = true;
                }
            }

            //---------------------------

            if (!StringUtils.isEmpty(sr)) {
                final List<ShortLabeledDescriptionSentence> coded =
                        sentences.stream().filter(s -> !StringUtils.isEmpty(s.getSr())).collect(Collectors.toList());
                if (coded.size() == sentences.size()) {
                    paragraph.setSr("");
                    modified = true;
                } else if (sentences.size() == 1 && coded.isEmpty()) {
                    paragraph.setSr("");
                    sentences.forEach(s -> s.setSr("x"));
                    modified = true;
                }
            }
        }

        return modified;
    }
}
