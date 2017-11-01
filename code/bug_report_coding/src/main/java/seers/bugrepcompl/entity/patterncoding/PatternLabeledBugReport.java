package seers.bugrepcompl.entity.patterncoding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bug")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatternLabeledBugReport {

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

	public String getId() {
		return id;
	}

	public PatternLabeledBugReportTitle getTitle() {
		return title;
	}

	public PatternLabeledBugReportDescription getDescription() {
		return description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(PatternLabeledBugReportTitle title) {
		this.title = title;
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

	@Override
	public String toString() {
		return "BR [id=" + id + ", noBug=" + noBug + ", com=" + comments + ", tit=" + title + ", desc=\r\n" + description
				+ "]";
	}

	public seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport toShortCodedBug() {

		seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportTitle title2 = title.toPatternCodingTitle();
		seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportDescription description2 = description
				.toPatternCodingDescription();
		seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport bug = new seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport(
				id, title2, description2, noBug, comments);
		return bug;
	}

}
