package seers.bugrepcompl.entity.shortcodingparse;

import seers.bugrepcompl.entity.codingparse.LabeledDescriptionParagraph;
import seers.bugrepcompl.entity.codingparse.LabeledDescriptionSentence;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "parg")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShortLabeledDescriptionParagraph {

	@XmlAttribute
	private String ob = "";
	@XmlAttribute
	private String eb = "";
	@XmlAttribute
	private String sr = "";

	@XmlAttribute
	private String id;

	@XmlElement(name = "st")
	private List<ShortLabeledDescriptionSentence> sentences;

	public ShortLabeledDescriptionParagraph() {
	}

	public ShortLabeledDescriptionParagraph(String id, List<ShortLabeledDescriptionSentence> sentences) {
		this.id = id;
		this.sentences = sentences;
	}

	public ShortLabeledDescriptionParagraph(String ob, String eb, String sr, String id, List<ShortLabeledDescriptionSentence> sentences) {
		super();
		this.ob = ob;
		this.eb = eb;
		this.sr = sr;
		this.id = id;
		this.sentences = sentences;
	}

	public ShortLabeledDescriptionParagraph(ShortLabeledDescriptionParagraph descriptionParagraph) {

		this.ob = descriptionParagraph.ob;
		this.eb = descriptionParagraph.eb;
		this.sr = descriptionParagraph.sr;
		this.id = descriptionParagraph.id;

		if (descriptionParagraph.sentences != null) {

			this.sentences = new ArrayList<>();
			for (ShortLabeledDescriptionSentence descriptionSentence : descriptionParagraph.sentences) {
				this.sentences.add(new ShortLabeledDescriptionSentence(descriptionSentence));
			}
		}

	}

	public String getId() {
		return id;
	}

	public List<ShortLabeledDescriptionSentence> getSentences() {
		return sentences;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSentences(List<ShortLabeledDescriptionSentence> sentences) {
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

	@Override
	public String toString() {
		return "\t[id= " + id + ", ob=" + ob + ", eb=" + eb + ", sr=" + sr + ", [sentences=\r\n" + getStrElements()
				+ "\t]";
	}

	private String getStrElements() {
		StringBuffer buf = new StringBuffer();

		List<ShortLabeledDescriptionSentence> sentences2 = getSentences();
		if (sentences2 != null) {

			for (ShortLabeledDescriptionSentence el : sentences2) {
				buf.append("\t\t");
				buf.append(el);
				buf.append("\t\t\r\n");
			}
		}
		return buf.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ShortLabeledDescriptionParagraph other = (ShortLabeledDescriptionParagraph) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public seers.bugrepcompl.entity.regularparse.ParsedDescriptionParagraph toRegularParsedParagraph() {

		seers.bugrepcompl.entity.regularparse.ParsedDescriptionParagraph par = new seers.bugrepcompl.entity.regularparse.ParsedDescriptionParagraph();
		par.setId(id);
		List<seers.bugrepcompl.entity.regularparse.ParsedDescriptionSentence> sentences2 = null;
		if (sentences != null) {
			sentences2 = new ArrayList<>();

			for (ShortLabeledDescriptionSentence sent : sentences) {
				if (sent != null) {
					sentences2.add(sent.toRegularParsedSentence());
				}

			}
		}
		par.setSentences(sentences2);
		return par;
	}

	public seers.bugrepcompl.entity.patterncoding.PatternLabeledDescriptionParagraph toPatternCodingParagraph() {

		seers.bugrepcompl.entity.patterncoding.PatternLabeledDescriptionParagraph par = new seers.bugrepcompl.entity.patterncoding.PatternLabeledDescriptionParagraph();
		par.setId(id);
		par.setOb(ob);
		par.setEb(eb);
		par.setSr(sr);
		List<seers.bugrepcompl.entity.patterncoding.PatternLabeledDescriptionSentence> sentences2 = null;
		if (sentences != null) {
			sentences2 = new ArrayList<>();

			for (ShortLabeledDescriptionSentence sent : sentences) {
				if (sent != null) {
					sentences2.add(sent.toPatternCodingSentence());
				}

			}
		}
		par.setSentences(sentences2);
		return par;
	}

	public LabeledDescriptionParagraph toLabeledDescriptionParagraph() {

		LabeledDescriptionParagraph par = new LabeledDescriptionParagraph(ob, eb, sr, id);
		List<LabeledDescriptionSentence> sentences2 = null;
		if (sentences != null) {
			sentences2 = new ArrayList<>();

			for (ShortLabeledDescriptionSentence sent : sentences) {
				if (sent != null) {
					sentences2.add(sent.toLabeledDescriptionSentence());
				}

			}
		}
		par.setSentences(sentences2);
		return par;
	}
}
