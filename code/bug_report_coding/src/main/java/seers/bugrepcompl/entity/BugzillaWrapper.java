package seers.bugrepcompl.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bugzilla")
@XmlAccessorType(XmlAccessType.FIELD)
public class BugzillaWrapper {

	@XmlElement(name = "bug")
	private BugzillaBugReport bug;

	public BugzillaBugReport getBug() {
		return bug;
	}

	public void setBug(BugzillaBugReport bug) {
		this.bug = bug;
	}

}
