package seers.bugrepcompl.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "long_desc")
@XmlAccessorType(XmlAccessType.FIELD)
public class BugLongDescription {

	@XmlElement(name = "thetext")
	private String text;
	@XmlElement(name = "who")
	private String who;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

}
