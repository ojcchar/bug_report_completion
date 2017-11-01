package seers.bugrepcompl.entity.regularparse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bug")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParsedBugReport {

	@XmlElement(name = "id")
	private String id;
	@XmlElement(name = "title")
	private String title;
	@XmlElement(name = "description")
	private ParsedBugReportDescription description;

	public ParsedBugReport() {
	}

	public ParsedBugReport(ParsedBugReport bugReport) {
		this.id = bugReport.id;
		this.title = bugReport.title;
		this.description = new ParsedBugReportDescription(bugReport.description);
	}

	public ParsedBugReport(String id, String title, ParsedBugReportDescription description) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public ParsedBugReportDescription getDescription() {
		return description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(ParsedBugReportDescription description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "BugReport [id=" + id + ", title=" + title + ", description=\r\n" + description + "]";
	}

}
