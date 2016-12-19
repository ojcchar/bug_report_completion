package seers.bugrepcompl.entity.parse;

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
			List<DescriptionSentence> sentences2 = par.getSentences();

			if (sentences2 == null) {
				throw new RuntimeException("Paragraph " + par.getId() + " has no sentences");
			}

			sentences.addAll(sentences2);
		}
		return sentences;
	}

	public seers.bugrepcompl.entity.parse2.BugReportDescription toDescription2() {
		List<seers.bugrepcompl.entity.parse2.DescriptionParagraph> paragraphs2 = toParagraphs2();
		return new seers.bugrepcompl.entity.parse2.BugReportDescription(paragraphs2);
	}

	private List<seers.bugrepcompl.entity.parse2.DescriptionParagraph> toParagraphs2() {
		if (this.paragraphs==null) {
			return null;
		}
		
		List<seers.bugrepcompl.entity.parse2.DescriptionParagraph> pars2 = new ArrayList<>();
		for (DescriptionParagraph par : this.paragraphs) {
			
			pars2.add(par.toParagraph2());
			
		}
		
		return pars2;
	}
}
