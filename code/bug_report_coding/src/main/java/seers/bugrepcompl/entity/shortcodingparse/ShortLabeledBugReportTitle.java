package seers.bugrepcompl.entity.shortcodingparse;

import seers.bugrepcompl.entity.codingparse.LabeledBugReportTitle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "title")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShortLabeledBugReportTitle {

	@XmlAttribute
	private String ob = "";
	@XmlAttribute
	private String eb = "";
	@XmlAttribute
	private String sr = "";

	@XmlValue
	private String value;

	public ShortLabeledBugReportTitle() {
	}

	public ShortLabeledBugReportTitle(String value) {
		this.value = value;
	}

	public ShortLabeledBugReportTitle(ShortLabeledBugReportTitle bugReportTitle) {
		this.ob = bugReportTitle.ob;
		this.eb = bugReportTitle.eb;
		this.sr = bugReportTitle.sr;
		this.value = bugReportTitle.value;
	}

	public ShortLabeledBugReportTitle(String ob, String eb, String sr, String value) {
		super();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eb == null) ? 0 : eb.hashCode());
		result = prime * result + ((ob == null) ? 0 : ob.hashCode());
		result = prime * result + ((sr == null) ? 0 : sr.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShortLabeledBugReportTitle other = (ShortLabeledBugReportTitle) obj;
		if (eb == null) {
			if (other.eb != null)
				return false;
		} else if (!eb.equals(other.eb))
			return false;
		if (ob == null) {
			if (other.ob != null)
				return false;
		} else if (!ob.equals(other.ob))
			return false;
		if (sr == null) {
			if (other.sr != null)
				return false;
		} else if (!sr.equals(other.sr))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReportTitle toPatternCodingTitle() {
		return new seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReportTitle(ob, eb, sr, null, value);
	}

	public LabeledBugReportTitle toLabeledBugReportTitle() {
		return new LabeledBugReportTitle(ob, eb, sr, value);
	}
}
