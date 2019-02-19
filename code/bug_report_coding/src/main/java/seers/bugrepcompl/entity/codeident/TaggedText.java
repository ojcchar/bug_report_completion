package seers.bugrepcompl.entity.codeident;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaggedText {

    @XmlAttribute
    private String type;
    @XmlValue
    private String value;

    public TaggedText() {
    }

    public TaggedText(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "txt{" +
                "t='" + type + '\'' +
                ", v='" + value + '\'' +
                '}';
    }
}
