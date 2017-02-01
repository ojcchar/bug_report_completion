package seers.bugrepcompl.entity.regularparse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "paragraph")
@XmlAccessorType(XmlAccessType.FIELD)
public class DescriptionParagraph {

	@XmlAttribute
	private String id;

	@XmlElement(name = "sentence")
	private List<DescriptionSentence> sentences;

	public DescriptionParagraph() {
	}

	public DescriptionParagraph(DescriptionParagraph descriptionParagraph) {
		this.id = descriptionParagraph.id;

		if (descriptionParagraph.sentences != null) {

			this.sentences = new ArrayList<>();
			for (DescriptionSentence descriptionSentence : descriptionParagraph.sentences) {
				this.sentences.add(new DescriptionSentence(descriptionSentence));
			}
		}
	}

	public String getId() {
		return id;
	}

	public List<DescriptionSentence> getSentences() {
		return sentences;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSentences(List<DescriptionSentence> sentences) {
		this.sentences = sentences;
	}

	@Override
	public String toString() {
		return "\t[id= " + id + ", [sentences=\r\n" + getStrElements() + "\t]";
	}

	private String getStrElements() {
		StringBuffer buf = new StringBuffer();

		List<DescriptionSentence> sentences2 = getSentences();
		if (sentences2 != null) {

			for (DescriptionSentence el : sentences2) {
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
		DescriptionParagraph other = (DescriptionParagraph) obj;
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

}
