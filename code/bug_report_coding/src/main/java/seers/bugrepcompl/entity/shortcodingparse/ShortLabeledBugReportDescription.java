package seers.bugrepcompl.entity.shortcodingparse;

import seers.bugrepcompl.entity.codingparse.LabeledBugReportDescription;
import seers.bugrepcompl.entity.codingparse.LabeledDescriptionParagraph;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "desc")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShortLabeledBugReportDescription {

	@XmlElement(name = "parg")
	private List<ShortLabeledDescriptionParagraph> paragraphs;

	public ShortLabeledBugReportDescription() {
	}

	public ShortLabeledBugReportDescription(List<ShortLabeledDescriptionParagraph> paragraphs) {
		super();
		this.paragraphs = paragraphs;
	}

	public ShortLabeledBugReportDescription(ShortLabeledBugReportDescription description) {
		this.paragraphs = new ArrayList<>();

		if (description == null ||  description.paragraphs == null) return;

		for (ShortLabeledDescriptionParagraph descriptionParagraph : description.paragraphs) {
			this.paragraphs.add(new ShortLabeledDescriptionParagraph(descriptionParagraph));
		}
	}

	public List<ShortLabeledDescriptionParagraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<ShortLabeledDescriptionParagraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	@Override
	public String toString() {
		return "[paragraphs=\r\n" + getStrElements() + "]";
	}

	private String getStrElements() {
		StringBuffer buf = new StringBuffer();

		for (ShortLabeledDescriptionParagraph el : getParagraphs()) {
			buf.append(el);
			buf.append("\r\n");
		}
		return buf.toString();
	}

	public List<ShortLabeledDescriptionSentence> getAllSentences() {

		List<ShortLabeledDescriptionSentence> sentences = new ArrayList<>();

		if (paragraphs == null) {
			return sentences;
		}

		for (ShortLabeledDescriptionParagraph par : paragraphs) {
			List<ShortLabeledDescriptionSentence> sentences2 = par.getSentences();

			if (sentences2 != null) {
				sentences.addAll(sentences2);
			}

		}
		return sentences;
	}

	public seers.bugrepcompl.entity.regularparse.ParsedBugReportDescription toRegularParsedDescription() {

		seers.bugrepcompl.entity.regularparse.ParsedBugReportDescription desc = new seers.bugrepcompl.entity.regularparse.ParsedBugReportDescription();

		List<seers.bugrepcompl.entity.regularparse.ParsedDescriptionParagraph> paragraphs2 = null;
		if (paragraphs != null) {
			paragraphs2 = new ArrayList<>();

			for (ShortLabeledDescriptionParagraph par : paragraphs) {
				if (par != null) {
					paragraphs2.add(par.toRegularParsedParagraph());
				}

			}
		}

		desc.setParagraphs(paragraphs2);

		return desc;
	}

	public seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReportDescription toPatternCodingDescription() {
		seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReportDescription desc = new seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReportDescription();

		List<seers.bugrepcompl.entity.patterncoding.PatternLabeledDescriptionParagraph> paragraphs2 = null;
		if (paragraphs != null) {
			paragraphs2 = new ArrayList<>();

			for (ShortLabeledDescriptionParagraph par : paragraphs) {
				if (par != null) {
					paragraphs2.add(par.toPatternCodingParagraph());
				}

			}
		}

		desc.setParagraphs(paragraphs2);

		return desc;
	}

	public LabeledBugReportDescription toLabeledBugReportDescription() {
		LabeledBugReportDescription desc = new LabeledBugReportDescription();

		List<LabeledDescriptionParagraph> paragraphs2 = null;
		if (paragraphs != null) {
			paragraphs2 = new ArrayList<>();

			for (ShortLabeledDescriptionParagraph par : paragraphs) {
				if (par != null) {
					paragraphs2.add(par.toLabeledDescriptionParagraph());
				}

			}
		}

		desc.setParagraphs(paragraphs2);

		return desc;
	}
}
