package seers.bugreppatterns.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bug")
@XmlAccessorType(XmlAccessType.FIELD)
public class BugReport {

	@XmlElement(name = "id")
	private String id;
	@XmlElement(name = "title")
	private String title;
	@XmlElement(name = "description")
	private BugReportDescription description;
	
	public BugReport() {
	}

	public BugReport(BugReport bugReport) {
		this.id = bugReport.id;
		this.title = bugReport.title;
		this.description = new BugReportDescription(bugReport.description);
	}

	public BugReport(String id, String title, BugReportDescription description) {
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

	public BugReportDescription getDescription() {
		return description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(BugReportDescription description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "BugReport [id=" + id + ", title=" + title + ", description=\r\n" + description + "]";
	}

}
