package seers.bugrepcompl.entity.regularparse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "description")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParsedBugReportDescription {

	@XmlElement(name = "paragraph")
	private List<ParsedDescriptionParagraph> paragraphs;

	public ParsedBugReportDescription() {
	}

	public ParsedBugReportDescription(ParsedBugReportDescription description) {
		this.paragraphs = new ArrayList<>();

		if (description != null) {

			for (ParsedDescriptionParagraph descriptionParagraph : description.paragraphs) {
				this.paragraphs.add(new ParsedDescriptionParagraph(descriptionParagraph));
			}
		}
	}

	public List<ParsedDescriptionParagraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<ParsedDescriptionParagraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	@Override
	public String toString() {
		return "[paragraphs=\r\n" + getStrElements() + "]";
	}

	private String getStrElements() {
		StringBuffer buf = new StringBuffer();

		for (ParsedDescriptionParagraph el : getParagraphs()) {
			buf.append(el);
			buf.append("\r\n");
		}
		return buf.toString();
	}

	public List<ParsedDescriptionSentence> getAllSentences() {

		List<ParsedDescriptionSentence> sentences = new ArrayList<>();
		for (ParsedDescriptionParagraph par : paragraphs) {
			List<ParsedDescriptionSentence> sentences2 = par.getSentences();

			if (sentences2 != null) {
				sentences.addAll(sentences2);
			} else {
				// throw new RuntimeException("Paragraph " + par.getId() + " has
				// no
				// sentences");
			}

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
		ParsedBugReportDescription other = (ParsedBugReportDescription) obj;
		if (paragraphs == null) {
			if (other.paragraphs != null)
				return false;
		} else if (!paragraphs.equals(other.paragraphs))
			return false;
		return true;
	}

}
