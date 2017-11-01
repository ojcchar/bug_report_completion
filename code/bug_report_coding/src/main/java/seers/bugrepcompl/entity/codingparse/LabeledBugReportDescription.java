package seers.bugrepcompl.entity.codingparse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "description")
@XmlAccessorType(XmlAccessType.FIELD)
public class LabeledBugReportDescription {

	@XmlElement(name = "paragraph")
	private List<LabeledDescriptionParagraph> paragraphs;

	public List<LabeledDescriptionParagraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<LabeledDescriptionParagraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	@Override
	public String toString() {
		return "[paragraphs=\r\n" + getStrElements() + "]";
	}

	private String getStrElements() {
		StringBuffer buf = new StringBuffer();

		for (LabeledDescriptionParagraph el : getParagraphs()) {
			buf.append(el);
			buf.append("\r\n");
		}
		return buf.toString();
	}

	public List<LabeledDescriptionSentence> getAllSentences() {

		List<LabeledDescriptionSentence> sentences = new ArrayList<>();
		for (LabeledDescriptionParagraph par : paragraphs) {
			List<LabeledDescriptionSentence> sentences2 = par.getSentences();

			if (sentences2 == null) {
				throw new RuntimeException("Paragraph " + par.getId() + " has no sentences");
			}

			sentences.addAll(sentences2);
		}
		return sentences;
	}

	public seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportDescription toDescription2() {
		List<seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph> paragraphs2 = toParagraphs2();
		return new seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportDescription(paragraphs2);
	}

	private List<seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph> toParagraphs2() {
		if (this.paragraphs == null) {
			return null;
		}

		List<seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph> pars2 = new ArrayList<>();
		for (LabeledDescriptionParagraph par : this.paragraphs) {

			pars2.add(par.toParagraph2());

		}

		return pars2;
	}
}
