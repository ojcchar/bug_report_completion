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
public class DescriptionParagraph {

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
	private List<DescriptionSentence> sentences;

	public DescriptionParagraph() {
	}

	public DescriptionParagraph(DescriptionParagraph descriptionParagraph) {

		this.ob = descriptionParagraph.ob;
		this.eb = descriptionParagraph.eb;
		this.sr = descriptionParagraph.sr;
		this.id = descriptionParagraph.id;
		this.patterns = descriptionParagraph.patterns;

		if (descriptionParagraph.sentences != null) {

			this.sentences = new ArrayList<>();
			for (DescriptionSentence descriptionSentence : descriptionParagraph.sentences) {
				this.sentences.add(new DescriptionSentence(descriptionSentence));
			}
		}

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

	public String getPatterns() {
		return patterns;
	}

	public void setPatterns(String patterns) {
		this.patterns = patterns;
	}

	public seers.bugrepcompl.entity.shortcodingparse.DescriptionParagraph toPatternCodingParagraph() {

		seers.bugrepcompl.entity.shortcodingparse.DescriptionParagraph par = new seers.bugrepcompl.entity.shortcodingparse.DescriptionParagraph();
		par.setId(id);
		par.setOb(ob);
		par.setEb(eb);
		par.setSr(sr);
		List<seers.bugrepcompl.entity.shortcodingparse.DescriptionSentence> sentences2 = null;
		if (sentences != null) {
			sentences2 = new ArrayList<>();

			for (DescriptionSentence sent : sentences) {
				if (sent != null) {
					sentences2.add(sent.toPatternCodingSentence());
				}

			}
		}
		par.setSentences(sentences2);
		return par;
	}

}
