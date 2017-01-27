package seers.bugrepcompl.entity.shortcodingparse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "desc")
@XmlAccessorType(XmlAccessType.FIELD)
public class BugReportDescription {

	@XmlElement(name = "parg")
	private List<DescriptionParagraph> paragraphs;

	public BugReportDescription() {
	}

	public BugReportDescription(List<DescriptionParagraph> paragraphs) {
		super();
		this.paragraphs = paragraphs;
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

		if (paragraphs == null) {
			return sentences;
		}

		for (DescriptionParagraph par : paragraphs) {
			List<DescriptionSentence> sentences2 = par.getSentences();

			if (sentences2 != null) {
				sentences.addAll(sentences2);
			}

		}
		return sentences;
	}

	public seers.bugrepcompl.entity.regularparse.BugReportDescription toRegularParsedDescription() {

		seers.bugrepcompl.entity.regularparse.BugReportDescription desc = new seers.bugrepcompl.entity.regularparse.BugReportDescription();

		List<seers.bugrepcompl.entity.regularparse.DescriptionParagraph> paragraphs2 = null;
		if (paragraphs != null) {
			paragraphs2 = new ArrayList<>();

			for (DescriptionParagraph par : paragraphs) {
				if (par != null) {
					paragraphs2.add(par.toRegularParsedParagraph());
				}

			}
		}

		desc.setParagraphs(paragraphs2);

		return desc;
	}
}
