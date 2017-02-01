package seers.bugrepcompl.entity;

import java.util.Arrays;

public class CodedDataEntry {

	public final String project;
	public final String bugId;
	public final String entryId;
	public final String paragraphTxt;
	public final String sentenceTxt;
	public final boolean isObsBehavior;
	public final boolean isExpecBehavior;
	public final boolean isStepsToRepro;
	public final String pattern1;
	public final String pattern2;
	public final String pattern3;
	public final String pattern4;
	public final String mainCoder;
	public final String instanceId;
	public final String[] patternsNoTesting;

	public CodedDataEntry(String project, String bugId) {
		this(project, bugId, null, null, null, false, false, false, null, null, null, null, null, null, null);
	}

	public CodedDataEntry(String project, String bugId, String entryId, String paragraphTxt, String sentenceTxt,
			boolean isObsBehavior, boolean isExpecBehavior, boolean isStepsToRepro, String pattern1, String pattern2,
			String pattern3, String pattern4, String mainCoder, String instanceId, String[] patternsNoTesting) {
		super();
		this.project = project;
		this.bugId = bugId;
		this.entryId = entryId;
		this.paragraphTxt = paragraphTxt;
		this.sentenceTxt = sentenceTxt;
		this.isObsBehavior = isObsBehavior;
		this.isExpecBehavior = isExpecBehavior;
		this.isStepsToRepro = isStepsToRepro;
		this.pattern1 = pattern1;
		this.pattern2 = pattern2;
		this.pattern3 = pattern3;
		this.pattern4 = pattern4;
		this.mainCoder = mainCoder;
		this.instanceId = instanceId;
		this.patternsNoTesting = patternsNoTesting;
	}

	@Override
	public String toString() {
		return "CodedDataEntry [project=" + project + ", bugId=" + bugId + ", entryId=" + entryId + ", paragraphTxt="
				+ paragraphTxt + ", sentenceTxt=" + sentenceTxt + ", isObsBehavior=" + isObsBehavior
				+ ", isExpecBehavior=" + isExpecBehavior + ", isStepsToRepro=" + isStepsToRepro + ", pattern1="
				+ pattern1 + ", pattern2=" + pattern2 + ", pattern3=" + pattern3 + ", pattern4=" + pattern4
				+ ", mainCoder=" + mainCoder + ", instanceId=" + instanceId + ", patternsNoTesting="
				+ Arrays.toString(patternsNoTesting) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bugId == null) ? 0 : bugId.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
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
		CodedDataEntry other = (CodedDataEntry) obj;
		if (bugId == null) {
			if (other.bugId != null)
				return false;
		} else if (!bugId.equals(other.bugId))
			return false;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		return true;
	}

}
