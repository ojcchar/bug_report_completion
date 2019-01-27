package seers.bugrepcompl.entity.regularparse;

import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "sentence")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParsedDescriptionSentence {

	@XmlAttribute(name = "id")
	private String id;
	@XmlValue
	private String value;

	public ParsedDescriptionSentence() {
	}

	public ParsedDescriptionSentence(ParsedDescriptionSentence descriptionSentence) {
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

	@Override
	public String toString() {
		return "stnc [" + getId() + ", " + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ParsedDescriptionSentence other = (ParsedDescriptionSentence) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public ShortLabeledDescriptionSentence toShortLabeledSentence() {
		return new seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence( id, value);
	}
}
