package seers.bugrepcompl.entity.patterncoding;

import org.xml.sax.SAXException;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.codeident.CodeTaggedBugReport;
import seers.bugrepcompl.entity.codeident.TaggedText;
import seers.bugrepcompl.xmlcoding.AgreementMain;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@XmlRootElement(name = "bug")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatternLabeledBugReport {

    List<TaggedText> codeSegments;
    @XmlElement(name = "id")
    private String id;
    @XmlElement(name = "title")
    private PatternLabeledBugReportTitle title;
    @XmlElement(name = "desc")
    private PatternLabeledBugReportDescription description;
    @XmlAttribute(name = "no-bug")
    private String noBug = "";
    @XmlAttribute(name = "comments")
    private String comments = "";

    private boolean hasCode = false;
    private boolean hasOB = false;
    private boolean hasEB = false;
    private boolean hasS2R = false;

    public PatternLabeledBugReport() {
    }

    public PatternLabeledBugReport(String id, PatternLabeledBugReportTitle title, PatternLabeledBugReportDescription description, String noBug, String comments) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.noBug = noBug;
        this.comments = comments;
    }

    public PatternLabeledBugReport(PatternLabeledBugReport bugReport) {
        this.id = bugReport.getId();
        this.title = new PatternLabeledBugReportTitle(bugReport.getTitle());
        this.description = new PatternLabeledBugReportDescription(bugReport.getDescription());
        this.noBug = bugReport.noBug;
        this.comments = bugReport.comments;
    }

    public PatternLabeledBugReport(String id, PatternLabeledBugReportTitle title, PatternLabeledBugReportDescription description) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public static PatternLabeledBugReport buildFromComponents(String codedFilePath, File codeTaggedFile, Set<String> codeTypesToDiscard)
            throws IOException, ParserConfigurationException, SAXException, JAXBException {

        PatternLabeledBugReport codedBug = XMLHelper.readXML(PatternLabeledBugReport.class, codedFilePath);

        codedBug.codeSegments = readCodeSegments(codeTaggedFile, codeTypesToDiscard);
        codedBug.propagateParagraphLabels();

        return codedBug;
    }

    private static List<TaggedText> readCodeSegments(File codeTaggedFile, Set<String> codeTypesToDiscard) throws JAXBException, IOException, SAXException, ParserConfigurationException {
        CodeTaggedBugReport codeTaggedBR = XMLHelper.readXML(CodeTaggedBugReport.class, codeTaggedFile);

        List<TaggedText> descTaggedTexts = new ArrayList<>();
        if (codeTaggedBR.getDescription() != null) {
            descTaggedTexts = codeTaggedBR.getDescription().getTaggedTexts();
            if (descTaggedTexts == null)
                descTaggedTexts = new ArrayList<>();
        }

        return descTaggedTexts.stream().filter(taggedText -> {
            final String type = taggedText.getType();
            return !codeTypesToDiscard.contains(type);
        }).collect(Collectors.toList());
    }

    public boolean hasCode() {
        return !codeSegments.isEmpty();
    }

    private void propagateParagraphLabels() {
        if (description == null) {
            return;
        }

        List<PatternLabeledDescriptionParagraph> paragraphs = description.getParagraphs();

        if (paragraphs == null)
            return;

        for (PatternLabeledDescriptionParagraph paragraph : paragraphs) {

            Labels paragraphLabels = new Labels(paragraph.getOb().trim(), paragraph.getEb().trim(),
                    paragraph.getSr().trim());

            List<PatternLabeledDescriptionSentence> sentences = paragraph.getSentences();
            for (PatternLabeledDescriptionSentence sent : sentences) {
                Labels sentLabels = new Labels(sent.getOb().trim(), sent.getEb().trim(), sent.getSr().trim());
                Labels mergedLabels = AgreementMain.mergeLabels(sentLabels, paragraphLabels);

                sent.setOb(mergedLabels.getIsOB());
                sent.setEb(mergedLabels.getIsEB());
                sent.setSr(mergedLabels.getIsSR());

            }
        }
    }

    public seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport toShortCodedBug() {

        seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportTitle title2 = title.toPatternCodingTitle();
        seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportDescription description2 = description
                .toPatternCodingDescription();
        seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport bug = new seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport(
                id, title2, description2, noBug, comments);
        return bug;
    }

    @Override
    public String toString() {
        return "BR [id=" + id + ", noBug=" + noBug + ", com=" + comments + ", tit=" + title + ", desc=\r\n" + description
                + "]";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PatternLabeledBugReportTitle getTitle() {
        return title;
    }

    public void setTitle(PatternLabeledBugReportTitle title) {
        this.title = title;
    }

    public PatternLabeledBugReportDescription getDescription() {
        return description;
    }

    public void setDescription(PatternLabeledBugReportDescription description) {
        this.description = description;
    }

    public String getNoBug() {
        return noBug;
    }

    public void setNoBug(String noBug) {
        this.noBug = noBug;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCode() {
        return codeSegments.stream()
                .map(TaggedText::getValue)
                .collect(Collectors.joining(" "));
    }
}
