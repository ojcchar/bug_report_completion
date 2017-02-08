package seers.bugrepcompl.entity;

public class PatternEntry {

	// sys, bug_id, instance_id, pattern, type of info, type of pattern,
	// type_of_issue
	public final TextInstance instance;
	public final String pattern;
	public final String infoType;
	public final String patternType;
	public final String bugType;
	public final int order;

	public PatternEntry(TextInstance instance, String pattern, String infoType, String patternType, String bugType,
			int order) {
		super();
		this.instance = instance;
		this.pattern = pattern;
		this.infoType = infoType;
		this.patternType = patternType;
		this.bugType = bugType;
		this.order = order;
	}

	@Override
	public String toString() {
		return "PatternEntry [instance=" + instance + ", pattern=" + pattern + ", infoType=" + infoType
				+ ", patternType=" + patternType + ", bugType=" + bugType + ", order=" + order + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bugType == null) ? 0 : bugType.hashCode());
		result = prime * result + ((infoType == null) ? 0 : infoType.hashCode());
		result = prime * result + ((instance == null) ? 0 : instance.hashCode());
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		result = prime * result + ((patternType == null) ? 0 : patternType.hashCode());
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
		PatternEntry other = (PatternEntry) obj;
		if (bugType == null) {
			if (other.bugType != null)
				return false;
		} else if (!bugType.equals(other.bugType))
			return false;
		if (infoType == null) {
			if (other.infoType != null)
				return false;
		} else if (!infoType.equals(other.infoType))
			return false;
		if (instance == null) {
			if (other.instance != null)
				return false;
		} else if (!instance.equals(other.instance))
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		if (patternType == null) {
			if (other.patternType != null)
				return false;
		} else if (!patternType.equals(other.patternType))
			return false;
		return true;
	}

}
