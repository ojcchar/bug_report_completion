package seers.bugrepcompl.entity.patterncoding;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "desc")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatternLabeledBugReportDescription {

	@XmlElement(name = "parg")
	private List<PatternLabeledDescriptionParagraph> paragraphs;

	public PatternLabeledBugReportDescription() {
	}

	public PatternLabeledBugReportDescription(List<PatternLabeledDescriptionParagraph> paragraphs) {
		super();
		this.paragraphs = paragraphs;
	}

	public PatternLabeledBugReportDescription(PatternLabeledBugReportDescription description) {
		this.paragraphs = new ArrayList<>();

		for (PatternLabeledDescriptionParagraph descriptionParagraph : description.paragraphs) {
			this.paragraphs.add(new PatternLabeledDescriptionParagraph(descriptionParagraph));
		}
	}

	public List<PatternLabeledDescriptionParagraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<PatternLabeledDescriptionParagraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	@Override
	public String toString() {
		return "[paragraphs=\r\n" + getStrElements() + "]";
	}

	private String getStrElements() {
		StringBuffer buf = new StringBuffer();

		for (PatternLabeledDescriptionParagraph el : getParagraphs()) {
			buf.append(el);
			buf.append("\r\n");
		}
		return buf.toString();
	}

	public List<PatternLabeledDescriptionSentence> getAllSentences() {

		List<PatternLabeledDescriptionSentence> sentences = new ArrayList<>();

		if (paragraphs == null) {
			return sentences;
		}

		for (PatternLabeledDescriptionParagraph par : paragraphs) {
			List<PatternLabeledDescriptionSentence> sentences2 = par.getSentences();

			if (sentences2 != null) {
				sentences.addAll(sentences2);
			}

		}
		return sentences;
	}

	public seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportDescription toPatternCodingDescription() {
		seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportDescription desc = new seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportDescription();

		List<seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph> paragraphs2 = null;
		if (paragraphs != null) {
			paragraphs2 = new ArrayList<>();

			for (PatternLabeledDescriptionParagraph par : paragraphs) {
				if (par != null) {
					paragraphs2.add(par.toPatternCodingParagraph());
				}

			}
		}

		desc.setParagraphs(paragraphs2);

		return desc;
	}

}
