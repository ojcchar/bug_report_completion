package seers.bugrepcompl.entity.codeident;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CodeTaggedBugReportDescription {

    @XmlElement(name = "text")
    private List<TaggedText> taggedTexts;

    public CodeTaggedBugReportDescription() {
    }

    public CodeTaggedBugReportDescription(List<TaggedText> taggedTexts) {
        this.taggedTexts = taggedTexts;
    }

    public List<TaggedText> getTaggedTexts() {
        return taggedTexts;
    }

    public void setTaggedTexts(List<TaggedText> taggedTexts) {
        this.taggedTexts = taggedTexts;
    }
}
