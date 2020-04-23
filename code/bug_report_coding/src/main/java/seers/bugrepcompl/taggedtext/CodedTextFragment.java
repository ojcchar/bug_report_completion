package seers.bugrepcompl.taggedtext;

import java.util.Set;

public class CodedTextFragment {
    public int id;
    public boolean isTitle;
    public String text;
    public Set<CodedInfo> infoTypes;

    public CodedTextFragment(int id, boolean isTitle, String text, Set<CodedInfo> infoTypes) {
        this.id = id;
        this.isTitle = isTitle;
        this.text = text;
        this.infoTypes = infoTypes;
    }

    @Override
    public String toString() {
        return "frag{" +
                "i=" + id +
                ", it=" + isTitle +
                ", t='" + text + '\'' +
                ", ty=" + infoTypes +
                '}';
    }
}
