package seers.bugreppatterns.entity.xml;

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

}
