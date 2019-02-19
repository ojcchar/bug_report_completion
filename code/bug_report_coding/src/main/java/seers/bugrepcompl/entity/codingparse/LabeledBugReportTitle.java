package seers.bugrepcompl.entity.codingparse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "title")
@XmlAccessorType(XmlAccessType.FIELD)
public class LabeledBugReportTitle {

	@XmlAttribute
	private String ob = "";
	@XmlAttribute
	private String eb = "";
	@XmlAttribute
	private String sr = "";

	@XmlValue
	private String value;

	public LabeledBugReportTitle() {
	}

	public LabeledBugReportTitle(String value) {
		super();
		this.value = value;
	}

	public LabeledBugReportTitle(String ob, String eb, String sr, String value) {
		this.ob = ob;
		this.eb = eb;
		this.sr = sr;
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

	public seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportTitle toTitle2() {
		return new seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportTitle(ob, eb, sr, value);
	}

	public boolean isObLabeled() {
		return !getOb().trim().isEmpty();
	}

	public boolean isSrLabeled() {
		return !getSr().trim().isEmpty();
	}

}
