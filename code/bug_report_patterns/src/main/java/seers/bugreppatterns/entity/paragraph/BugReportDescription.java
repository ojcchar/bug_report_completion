package seers.bugreppatterns.entity.paragraph;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "description")
@XmlAccessorType(XmlAccessType.FIELD)
public class BugReportDescription {

	@XmlElement(name = "paragraph")
	private List<DescriptionParagraph> paragraphs;

	public List<DescriptionParagraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<DescriptionParagraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	@Override
	public String toString() {
		return "[paragraphs=\r\n" + getStrElements() + "]";
	}

	private String getStrElements() {
		StringBuffer buf = new StringBuffer();

		for (DescriptionParagraph el : getParagraphs()) {
			buf.append(el);
			buf.append("\r\n");
		}
		return buf.toString();
	}

	public List<DescriptionSentence> getAllSentences() {

		List<DescriptionSentence> sentences = new ArrayList<>();
		for (DescriptionParagraph par : paragraphs) {
			sentences.addAll(par.getSentences());
		}
		return sentences;
	}
}
