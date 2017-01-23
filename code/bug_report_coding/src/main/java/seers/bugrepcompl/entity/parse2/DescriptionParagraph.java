package seers.bugrepcompl.entity.parse2;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "parg")
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

	@XmlElement(name = "st")
	private List<DescriptionSentence> sentences;
	
	public DescriptionParagraph() {
	}

	public DescriptionParagraph(String ob, String eb, String sr, String id, List<DescriptionSentence> sentences) {
		super();
		this.ob = ob;
		this.eb = eb;
		this.sr = sr;
		this.id = id;
		this.sentences = sentences;
	}

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
		DescriptionParagraph other = (DescriptionParagraph) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
