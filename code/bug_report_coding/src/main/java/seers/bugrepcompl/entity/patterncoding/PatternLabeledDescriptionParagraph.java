package seers.bugrepcompl.entity.patterncoding;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "parg")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatternLabeledDescriptionParagraph {

	@XmlAttribute
	private String ob = "";
	@XmlAttribute
	private String eb = "";
	@XmlAttribute
	private String sr = "";
	@XmlAttribute
	private String patterns = null;

	@XmlAttribute
	private String id;

	@XmlElement(name = "st")
	private List<PatternLabeledDescriptionSentence> sentences;

	public PatternLabeledDescriptionParagraph() {
	}

	public PatternLabeledDescriptionParagraph(PatternLabeledDescriptionParagraph descriptionParagraph) {

		this.ob = descriptionParagraph.ob;
		this.eb = descriptionParagraph.eb;
		this.sr = descriptionParagraph.sr;
		this.id = descriptionParagraph.id;
		this.patterns = descriptionParagraph.patterns;

		if (descriptionParagraph.sentences != null) {

			this.sentences = new ArrayList<>();
			for (PatternLabeledDescriptionSentence descriptionSentence : descriptionParagraph.sentences) {
				this.sentences.add(new PatternLabeledDescriptionSentence(descriptionSentence));
			}
		}

	}

	public String getId() {
		return id;
	}

	public List<PatternLabeledDescriptionSentence> getSentences() {
		return sentences;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSentences(List<PatternLabeledDescriptionSentence> sentences) {
		this.sentences = sentences;
	}

	public String getOb() {
		return ob;
	}

	public String getEb() {
		return eb;
	}

	public String getSr() {
		return sr;
	}

	public void setOb(String ob) {
		this.ob = ob;
	}

	public void setEb(String eb) {
		this.eb = eb;
	}

	public void setSr(String sr) {
		this.sr = sr;
	}

	public String getPatterns() {
		return patterns;
	}

	public void setPatterns(String patterns) {
		this.patterns = patterns;
	}

	public seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph toPatternCodingParagraph() {

		seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph par = new seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph();
		par.setId(id);
		par.setOb(ob);
		par.setEb(eb);
		par.setSr(sr);
		List<seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence> sentences2 = null;
		if (sentences != null) {
			sentences2 = new ArrayList<>();

			for (PatternLabeledDescriptionSentence sent : sentences) {
				if (sent != null) {
					sentences2.add(sent.toPatternCodingSentence());
				}

			}
		}
		par.setSentences(sentences2);
		return par;
	}

	@Override
	public String toString() {
		return "\tpar{id=" + id + ", ob=" + ob + ", eb=" + eb + ", sr=" + sr + ", pt=" + patterns + ", sts=\n"
				+ getStrElements() + "\t]";
	}

	private String getStrElements() {
		StringBuffer buf = new StringBuffer();

		List<PatternLabeledDescriptionSentence> sentences2 = getSentences();
		if (sentences2 != null) {

			for (PatternLabeledDescriptionSentence el : sentences2) {
				buf.append("\t\t");
				buf.append(el);
				buf.append("\t\t\r\n");
			}
		}
		return buf.toString();
	}

}
