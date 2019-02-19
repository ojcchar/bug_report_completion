package seers.bugrepcompl.entity.regularparse;

import seers.bugrepcompl.entity.codingparse.LabeledDescriptionSentence;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "paragraph")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParsedDescriptionParagraph {

	@XmlAttribute
	private String id;

	@XmlElement(name = "sentence")
	private List<ParsedDescriptionSentence> sentences;

	public ParsedDescriptionParagraph() {
	}

	public ParsedDescriptionParagraph(ParsedDescriptionParagraph descriptionParagraph) {
		this.id = descriptionParagraph.id;

		if (descriptionParagraph.sentences != null) {

			this.sentences = new ArrayList<>();
			for (ParsedDescriptionSentence descriptionSentence : descriptionParagraph.sentences) {
				this.sentences.add(new ParsedDescriptionSentence(descriptionSentence));
			}
		}
	}

	public String getId() {
		return id;
	}

	public List<ParsedDescriptionSentence> getSentences() {
		return sentences;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSentences(List<ParsedDescriptionSentence> sentences) {
		this.sentences = sentences;
	}

	@Override
	public String toString() {
		return "\t[id= " + id + ", [sentences=\r\n" + getStrElements() + "\t]";
	}

	private String getStrElements() {
		StringBuffer buf = new StringBuffer();

		List<ParsedDescriptionSentence> sentences2 = getSentences();
		if (sentences2 != null) {

			for (ParsedDescriptionSentence el : sentences2) {
				buf.append("\t\t");
				buf.append(el);
				buf.append("\t\t\r\n");
			}
		}
		return buf.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((sentences == null) ? 0 : sentences.hashCode());
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
		ParsedDescriptionParagraph other = (ParsedDescriptionParagraph) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (sentences == null) {
			if (other.sentences != null)
				return false;
		} else if (!sentences.equals(other.sentences))
			return false;
		return true;
	}

	public ShortLabeledDescriptionParagraph toShortLabeledParagraph() {
		List<seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence> sentences2 = toSentences2();
		return new seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph(id, sentences2);
	}

	private List<seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence> toSentences2() {
		if (this.sentences == null) {
			return null;
		}

		List<seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence> sentences2 = new ArrayList<>();
		for (ParsedDescriptionSentence sent : this.sentences) {
			sentences2.add(sent.toShortLabeledSentence());
		}
		return sentences2;
	}
}
