package seers.bugrepcompl.entity;

public class CodedDataEntry {

	public final String project;
	public final String bugId;
	public final String instanceId;
	public final String paragraphTxt;
	public final String sentenceTxt;
	public final boolean isObsBehavior;
	public final boolean isExpecBehavior;
	public final boolean isStepsToRepro;
	public final String pattern1;
	public final String pattern2;
	public final String pattern3;
	public final String pattern4;
	public final String[] patternsNoTesting;

	public CodedDataEntry(String project, String bugId, String instanceId, String paragraph, String sentence,
			boolean isObsBehavior, boolean isExpecBehavior, boolean isStepsToRepro, String pattern1, String pattern2,
			String pattern3, String pattern4, String[] patternsNoTesting2) {
		super();
		this.project = project;
		this.bugId = bugId;
		this.instanceId = instanceId;
		this.paragraphTxt = paragraph;
		this.sentenceTxt = sentence;
		this.isObsBehavior = isObsBehavior;
		this.isExpecBehavior = isExpecBehavior;
		this.isStepsToRepro = isStepsToRepro;
		this.pattern1 = pattern1;
		this.pattern2 = pattern2;
		this.pattern3 = pattern3;
		this.pattern4 = pattern4;
		this.patternsNoTesting = patternsNoTesting2;
	}

	@Override
	public String toString() {
		return "[project=" + project + ", bugId=" + bugId + ", instanceId=" + instanceId + ", paragraph=" + paragraphTxt
				+ ", sentence=" + sentenceTxt + ", isObsBehavior=" + isObsBehavior + ", isExpecBehavior=" + isExpecBehavior
				+ ", isStepsToRepro=" + isStepsToRepro + ", pattern1=" + pattern1 + ", pattern2=" + pattern2
				+ ", pattern3=" + pattern3 + ", pattern4=" + pattern4 + "]";
	}

}
