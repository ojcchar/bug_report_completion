package seers.bugrepcompl.entity.shortcodingparse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "sent")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShortLabeledDescriptionSentence {

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

	public ShortLabeledDescriptionSentence() {
	}

	public ShortLabeledDescriptionSentence(String ob, String eb, String sr, String id, String value) {
		super();
		this.ob = ob;
		this.eb = eb;
		this.sr = sr;
		this.id = id;
		this.value = value;
	}

	public ShortLabeledDescriptionSentence(ShortLabeledDescriptionSentence descriptionSentence) {

		this.ob = descriptionSentence.ob;
		this.eb = descriptionSentence.eb;
		this.sr = descriptionSentence.sr;
		this.id = descriptionSentence.id;
		this.value = descriptionSentence.value;
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

	@Override
	public String toString() {
		return "stnc [ob=" + ob + ", eb=" + eb + ", sr=" + sr + ", id=" + id + ", val=" + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ShortLabeledDescriptionSentence other = (ShortLabeledDescriptionSentence) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public seers.bugrepcompl.entity.regularparse.ParsedDescriptionSentence toRegularParsedSentence() {
		seers.bugrepcompl.entity.regularparse.ParsedDescriptionSentence sent = new seers.bugrepcompl.entity.regularparse.ParsedDescriptionSentence();
		sent.setId(id);
		sent.setValue(this.value);
		return sent;
	}

	public seers.bugrepcompl.entity.patterncoding.PatternLabeledDescriptionSentence toPatternCodingSentence() {
		return new seers.bugrepcompl.entity.patterncoding.PatternLabeledDescriptionSentence(ob, eb, sr, null, id, value);
	}

	// @Override
	// public String toString() {
	// return "stnc [" + getId() + ", " + value + "]";
	// }

}
