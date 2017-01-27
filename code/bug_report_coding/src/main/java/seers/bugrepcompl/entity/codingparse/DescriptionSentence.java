package seers.bugrepcompl.entity.codingparse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "sentence")
@XmlAccessorType(XmlAccessType.FIELD)
public class DescriptionSentence {

	@XmlAttribute
	private String ob = "";
	@XmlAttribute
	private String eb = "";
	@XmlAttribute
	private String sr = "";

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

	public String getOb() {
		return ob;
	}

	public String getEb() {
		return eb;
	}

	public String getSr() {
		return sr;
	}

	public void setOb(String ob) {
		this.ob = ob;
	}

	public void setEb(String eb) {
		this.eb = eb;
	}

	public void setSr(String sr) {
		this.sr = sr;
	}

	@Override
	public String toString() {
		return "stnc [ob=" + ob + ", eb=" + eb + ", sr=" + sr + ", id=" + id + ", val=" + value + "]";
	}

	public seers.bugrepcompl.entity.shortcodingparse.DescriptionSentence toSentence2() {
		return new seers.bugrepcompl.entity.shortcodingparse.DescriptionSentence(ob, eb, sr, id, value);
	}

	// @Override
	// public String toString() {
	// return "stnc [" + getId() + ", " + value + "]";
	// }

}
