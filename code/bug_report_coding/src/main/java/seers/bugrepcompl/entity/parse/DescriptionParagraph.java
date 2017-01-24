package seers.bugrepcompl.entity.parse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "paragraph")
@XmlAccessorType(XmlAccessType.FIELD)
public class DescriptionParagraph {

	@XmlAttribute
	private String ob = "";
	@XmlAttribute
	private String eb = "";
	@XmlAttribute
	private String sr = "";

	@XmlAttribute
	private String id;

	@XmlElement(name = "sentence")
	private List<DescriptionSentence> sentences;

	 public String getId() {
		return id;
	}

	public List<DescriptionSentence> getSentences() {
		return sentences;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSentences(List<DescriptionSentence> sentences) {
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

		List<DescriptionSentence> sentences2 = getSentences();
		if (sentences2 != null) {

			for (DescriptionSentence el : sentences2) {
				buf.append("\t\t");
				buf.append(el);
				buf.append("\t\t\r\n");
			}
		}
		return buf.toString();
	}

	public seers.bugrepcompl.entity.parse2.DescriptionParagraph toParagraph2() {
		List<seers.bugrepcompl.entity.parse2.DescriptionSentence> sentences2 = toSentences2();
		return new seers.bugrepcompl.entity.parse2.DescriptionParagraph(ob, eb, sr, id, sentences2);
	}

	private List<seers.bugrepcompl.entity.parse2.DescriptionSentence> toSentences2() {
		if (this.sentences == null) {
			return null;
		}

		List<seers.bugrepcompl.entity.parse2.DescriptionSentence> sentences2 = new ArrayList<>();
		for (DescriptionSentence sent : this.sentences) {
			sentences2.add(sent.toSentence2());
		}
		return sentences2;
	}

}
