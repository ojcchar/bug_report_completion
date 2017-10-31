package seers.bugrepcompl.entity.codingparse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "title")
@XmlAccessorType(XmlAccessType.FIELD)
public class BugReportTitle {

	@XmlAttribute
	private String ob = "";
	@XmlAttribute
	private String eb = "";
	@XmlAttribute
	private String sr = "";

	@XmlValue
	private String value;

	public BugReportTitle() {
	}

	public BugReportTitle(String value) {
		super();
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

	public String getValue() {
		return value;
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

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "BugReportTitle [ob=" + ob + ", eb=" + eb + ", sr=" + sr + ", value=" + value + "]";
	}

	public seers.bugrepcompl.entity.shortcodingparse.BugReportTitle toTitle2() {
		return new seers.bugrepcompl.entity.shortcodingparse.BugReportTitle(ob, eb, sr, value);
	}

}
