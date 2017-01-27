package seers.bugrepcompl.entity.regularparse;

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
	
	public BugReportDescription() {
	}

	public BugReportDescription(BugReportDescription description) {
		this.paragraphs = new ArrayList<>();
		
		for (DescriptionParagraph descriptionParagraph : description.paragraphs) {
			this.paragraphs.add(new DescriptionParagraph(descriptionParagraph)); 
		}
	}

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
			List<DescriptionSentence> sentences2 = par.getSentences();

			if (sentences2 == null) {
				throw new RuntimeException("Paragraph " + par.getId() + " has no sentences");
			}

			sentences.addAll(sentences2);
		}
		return sentences;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((paragraphs == null) ? 0 : paragraphs.hashCode());
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
		BugReportDescription other = (BugReportDescription) obj;
		if (paragraphs == null) {
			if (other.paragraphs != null)
				return false;
		} else if (!paragraphs.equals(other.paragraphs))
			return false;
		return true;
	}
	
	
}
