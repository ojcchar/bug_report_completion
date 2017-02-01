package seers.bugrepcompl.entity.patterncoding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bug")
@XmlAccessorType(XmlAccessType.FIELD)
public class BugReport {

	@XmlElement(name = "id")
	private String id;
	@XmlElement(name = "title")
	private BugReportTitle title;
	@XmlElement(name = "desc")
	private BugReportDescription description;

	@XmlAttribute(name = "no-bug")
	private String noBug = "";
	@XmlAttribute(name = "comments")
	private String comments = "";

	public BugReport() {
	}

	public BugReport(String id, BugReportTitle title, BugReportDescription description, String noBug, String comments) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.noBug = noBug;
		this.comments = comments;
	}

	public BugReport(BugReport bugReport) {
		this.id = bugReport.getId();
		this.title = new BugReportTitle(bugReport.getTitle());
		this.description = new BugReportDescription(bugReport.getDescription());
		this.noBug = bugReport.noBug;
		this.comments = bugReport.comments;
	}

	public BugReport(String id, BugReportTitle title, BugReportDescription description) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public BugReportTitle getTitle() {
		return title;
	}

	public BugReportDescription getDescription() {
		return description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(BugReportTitle title) {
		this.title = title;
	}

	public void setDescription(BugReportDescription description) {
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

	public seers.bugrepcompl.entity.shortcodingparse.BugReport toShortCodedBug() {


		seers.bugrepcompl.entity.shortcodingparse.BugReportTitle title2 = title.toPatternCodingTitle();
		seers.bugrepcompl.entity.shortcodingparse.BugReportDescription description2 = description
				.toPatternCodingDescription();
		seers.bugrepcompl.entity.shortcodingparse.BugReport bug = new seers.bugrepcompl.entity.shortcodingparse.BugReport(id,
				title2, description2, noBug, comments);
		return bug;
	}

}
