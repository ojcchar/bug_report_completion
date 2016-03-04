package seers.bugrepcompl.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bug")
@XmlAccessorType(XmlAccessType.FIELD)
public class BugzillaBugReport {

	@XmlElement(name = "bug_id")
	private String id;
	@XmlElement(name = "short_desc")
	private String title;
	@XmlElement(name = "long_desc")
	private List<BugLongDescription> descriptions;
	@XmlElement(name = "reporter")
	private String reporter;

	public BugzillaBugReport() {
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public List<BugLongDescription> getDescriptions() {
		return descriptions;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescriptions(List<BugLongDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

}
