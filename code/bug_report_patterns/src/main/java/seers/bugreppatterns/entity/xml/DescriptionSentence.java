package seers.bugreppatterns.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "sentence")
@XmlAccessorType(XmlAccessType.FIELD)
public class DescriptionSentence {

	@XmlAttribute(name = "id")
	private String id;
	@XmlValue
	private String value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "stnc [" + getId() + ", " + value + "]";
	}

}
