package seers.bugrepcompl.entity.codeident;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bug_report")
@XmlAccessorType(XmlAccessType.FIELD)
public class CodeTaggedBugReport {

    @XmlElement(name = "bug_id")
    private String bugId;

    @XmlElement(name = "title")
    private CodeTaggedBugReportTitle title;

    @XmlElement(name = "description")
    private CodeTaggedBugReportDescription description;

    public CodeTaggedBugReport() {
    }

    public CodeTaggedBugReport(String bugId, CodeTaggedBugReportTitle title, CodeTaggedBugReportDescription
            description) {
        this.bugId = bugId;
        this.title = title;
        this.description = description;
    }

    public CodeTaggedBugReportTitle getTitle() {
        return title;
    }

    public void setTitle(CodeTaggedBugReportTitle title) {
        this.title = title;
    }

    public CodeTaggedBugReportDescription getDescription() {
        return description;
    }

    public void setDescription(CodeTaggedBugReportDescription description) {
        this.description = description;
    }

    public String getBugId() {
        return bugId;
    }

    public void setBugId(String bugId) {
        this.bugId = bugId;
    }

}
