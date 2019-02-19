package seers.bugrepcompl.entity.shortcodingparse;

import seers.bugrepcompl.entity.codingparse.LabeledBugReport;
import seers.bugrepcompl.entity.codingparse.LabeledBugReportDescription;
import seers.bugrepcompl.entity.codingparse.LabeledBugReportTitle;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "bug")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShortLabeledBugReport {

    @XmlElement(name = "id")
    private String id;
    @XmlElement(name = "title")
    private ShortLabeledBugReportTitle title;
    @XmlElement(name = "desc")
    private ShortLabeledBugReportDescription description;

    @XmlAttribute(name = "no-bug")
    private String noBug = "";
    @XmlAttribute(name = "comments")
    private String comments = "";

    public ShortLabeledBugReport() {
    }

    public ShortLabeledBugReport(ShortLabeledBugReport bugReport) {
        this.id = bugReport.getId();
        this.title = new ShortLabeledBugReportTitle(bugReport.getTitle());
        this.description = new ShortLabeledBugReportDescription(bugReport.getDescription());
        this.noBug = bugReport.noBug;
        this.comments = bugReport.comments;
    }

    public ShortLabeledBugReport(String id, ShortLabeledBugReportTitle title,
                                 ShortLabeledBugReportDescription description) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public ShortLabeledBugReport(String id, ShortLabeledBugReportTitle title,
                                 ShortLabeledBugReportDescription description, String noBug, String comments) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.noBug = noBug;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public ShortLabeledBugReportTitle getTitle() {
        return title;
    }

    public ShortLabeledBugReportDescription getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(ShortLabeledBugReportTitle title) {
        this.title = title;
    }

    public void setDescription(ShortLabeledBugReportDescription description) {
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

    @Override
    public String toString() {
        return "BR [id=" + id + ", tit=" + title + ", desc=" + description + ", noBug=" + noBug + ", com=" + comments
                + "]";
    }

    public seers.bugrepcompl.entity.regularparse.ParsedBugReport toRegularParsedBug() {
        seers.bugrepcompl.entity.regularparse.ParsedBugReport bug =
                new seers.bugrepcompl.entity.regularparse.ParsedBugReport();
        bug.setId(id);
        bug.setTitle(this.getTitle().getValue());
        if (this.description != null) {
            bug.setDescription(this.description.toRegularParsedDescription());
        }
        return bug;
    }

    public seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReport toPatternCodingBug() {

        seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReportTitle title2 = title.toPatternCodingTitle();
        seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReportDescription description2 = description
                .toPatternCodingDescription();
        seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReport bug =
                new seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReport(id,
                        title2, description2, noBug, comments);
        return bug;
    }


    public LabeledBugReport toLabeledBugReport() {
        LabeledBugReportTitle title2 = title.toLabeledBugReportTitle();
        LabeledBugReportDescription description2 = null;
        if (description != null) {
            description2 = description.toLabeledBugReportDescription();
        }
        return new LabeledBugReport(id, title2, description2);
    }
}
