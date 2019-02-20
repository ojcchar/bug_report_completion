package seers.bugrepcompl.entity.patterncoding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "sent")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatternLabeledDescriptionSentence {

	@XmlAttribute
	private String ob = "";
	@XmlAttribute
	private String eb = "";
	@XmlAttribute
	private String sr = "";
	@XmlAttribute
	private String patterns = null;

	@XmlAttribute(name = "id")
	private String id;
	@XmlValue
	private String value;

	public PatternLabeledDescriptionSentence() {
	}

	public PatternLabeledDescriptionSentence(String ob, String eb, String sr, String patterns, String id, String value) {
		super();
		this.ob = ob;
		this.eb = eb;
		this.sr = sr;
		this.patterns = patterns;
		this.id = id;
		this.value = value;
	}

	public PatternLabeledDescriptionSentence(PatternLabeledDescriptionSentence descriptionSentence) {

		this.ob = descriptionSentence.ob;
		this.eb = descriptionSentence.eb;
		this.sr = descriptionSentence.sr;
		this.id = descriptionSentence.id;
		this.value = descriptionSentence.value;
		this.patterns = descriptionSentence.patterns;
	}

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

	public String getPatterns() {
		return patterns;
	}

	public void setPatterns(String patterns) {
		this.patterns = patterns;
	}

	public seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence toPatternCodingSentence() {
		return new seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence(ob, eb, sr, id, value);
	}

	public boolean hasOB() {
		return ob.trim().equals("x");
	}

	public boolean hasEB() {
		return eb.trim().equals("x");
	}

	public boolean hasS2R() {
		return sr.trim().equals("x");
	}

	@Override
	public String toString() {
		return "{id=" + id + ", ob=" + ob + ", eb=" + eb + ", sr=" + sr + ", pt=" + patterns
				+ ", v=" + value + "}";
	}

}
