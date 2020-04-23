package seers.bugrepcompl.entity.patterncoding;

import org.xml.sax.SAXException;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.codeident.CodeTaggedBugReport;
import seers.bugrepcompl.entity.codeident.TaggedText;
import seers.bugrepcompl.xmlcoding.AgreementMain;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @SuppressWarnings("unused")
    public static PatternLabeledBugReport buildFromComponents(String codedFilePath, File codeTaggedFile)
            throws IOException, ParserConfigurationException, SAXException, JAXBException {

        PatternLabeledBugReport codedBug = XMLHelper.readXML(PatternLabeledBugReport.class, codedFilePath);

        codedBug.codeSegments = readCodeSegments(codeTaggedFile);
        codedBug.propagateParagraphLabels();

        return codedBug;
    }

    private static List<TaggedText> readCodeSegments(File codeTaggedFile) throws JAXBException, IOException, SAXException, ParserConfigurationException {
        CodeTaggedBugReport codeTaggedBR = XMLHelper.readXML(CodeTaggedBugReport.class, codeTaggedFile);

        return Optional.ofNullable(codeTaggedBR.getDescription())
                .map(d ->
                        Optional.ofNullable(d.getTaggedTexts())
                                .orElse(Collections.emptyList())
                )
                .orElse(Collections.emptyList());
    }

    @SuppressWarnings("unused")
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
        return new seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport(
                id, title2, description2, noBug, comments);
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

    @SuppressWarnings("unused")
    public List<TaggedText> getCode() {
        return codeSegments;
    }
}
