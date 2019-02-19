package seers.bugrepcompl.entity.codeident;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "all_text")
public class AllText {

    private List<TaggedText> taggedTexts;

    public List<TaggedText> getTaggedTexts() {
        return taggedTexts;
    }

    @XmlElements({
            @XmlElement(name = "java"),
            @XmlElement(name = "text")
    })
    public void setTaggedTexts(List<TaggedText> taggedTexts) {
        this.taggedTexts = taggedTexts;
    }

    @Override
    public String toString() {
        return "AllText{" +
                "txts=" + taggedTexts +
                '}';
    }
}
