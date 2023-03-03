package seers.bugrepcompl.entity.codingparse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bug")
@XmlAccessorType(XmlAccessType.FIELD)
public class LabeledBugReport {

	@XmlElement(name = "id")
	private String id;
	@XmlElement(name = "title")
	private LabeledBugReportTitle title;
	@XmlElement(name = "description")
	private LabeledBugReportDescription description;

	@XmlAttribute(name = "no-bug")
	private String noBug;
	@XmlAttribute(name = "comments")
	private String comments;

	public LabeledBugReport() {
	}

	public LabeledBugReport(String id, LabeledBugReportTitle title, LabeledBugReportDescription description) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public LabeledBugReportTitle getTitle() {
		return title;
	}

	public LabeledBugReportDescription getDescription() {
		return description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(LabeledBugReportTitle title) {
		this.title = title;
	}

	public void setDescription(LabeledBugReportDescription description) {
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

	public seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport toBugReport2() {
		seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport bug2 = new seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport(
				id, title.toTitle2(), description.toDescription2());
		return bug2;
	}

}