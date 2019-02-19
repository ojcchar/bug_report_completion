package seers.bugrepcompl.entity.codingparse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "paragraph")
@XmlAccessorType(XmlAccessType.FIELD)
public class LabeledDescriptionParagraph {

	@XmlAttribute
	private String ob = "";
	@XmlAttribute
	private String eb = "";
	@XmlAttribute
	private String sr = "";

	@XmlAttribute
	private String id;

	@XmlElement(name = "sentence")
	private List<LabeledDescriptionSentence> sentences;

	public LabeledDescriptionParagraph() {
	}

	public LabeledDescriptionParagraph(String ob, String eb, String sr, String id) {
		this.ob = ob;
		this.eb = eb;
		this.sr = sr;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public List<LabeledDescriptionSentence> getSentences() {
		return sentences;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSentences(List<LabeledDescriptionSentence> sentences) {
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

		List<LabeledDescriptionSentence> sentences2 = getSentences();
		if (sentences2 != null) {

			for (LabeledDescriptionSentence el : sentences2) {
				buf.append("\t\t");
				buf.append(el);
				buf.append("\t\t\r\n");
			}
		}
		return buf.toString();
	}

	public seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph toParagraph2() {
		List<seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence> sentences2 = toSentences2();
		return new seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionParagraph(ob, eb, sr, id, sentences2);
	}

	private List<seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence> toSentences2() {
		if (this.sentences == null) {
			return null;
		}

		List<seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence> sentences2 = new ArrayList<>();
		for (LabeledDescriptionSentence sent : this.sentences) {
			sentences2.add(sent.toSentence2());
		}
		return sentences2;
	}


	public boolean isObLabeled() {
		return !getOb().trim().isEmpty();
	}

	public boolean isSrLabeled() {
		return !getSr().trim().isEmpty();
	}

}
