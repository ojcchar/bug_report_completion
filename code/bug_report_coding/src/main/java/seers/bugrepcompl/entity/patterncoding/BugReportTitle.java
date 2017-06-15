package seers.bugrepcompl.entity.patterncoding;

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
	@XmlAttribute
	private String patterns;

	@XmlValue
	private String value;

	public BugReportTitle() {
	}

	public BugReportTitle(String ob, String eb, String sr, String patterns, String value) {
		super();
		this.ob = ob;
		this.eb = eb;
		this.sr = sr;
		this.patterns = patterns;
		this.value = value;
	}

	public BugReportTitle(BugReportTitle bugReportTitle) {
		this.ob = bugReportTitle.ob;
		this.eb = bugReportTitle.eb;
		this.sr = bugReportTitle.sr;
		this.value = bugReportTitle.value;
		this.patterns = bugReportTitle.patterns;
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

	public String getPatterns() {
		return patterns;
	}

	public void setPatterns(String patterns) {
		this.patterns = patterns;
	}

	public seers.bugrepcompl.entity.shortcodingparse.BugReportTitle toPatternCodingTitle() {
		return new seers.bugrepcompl.entity.shortcodingparse.BugReportTitle(ob, eb, sr, value);
	}

	@Override
	public String toString() {
		return "{ob=" + ob + ", eb=" + eb + ", sr=" + sr + ", pt=" + patterns + ", v=" + value + "}";
	}

}
