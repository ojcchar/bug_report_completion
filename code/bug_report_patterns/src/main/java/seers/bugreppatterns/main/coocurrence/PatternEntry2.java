package seers.bugreppatterns.main.coocurrence;

import seers.bugrepcompl.entity.TextInstance;

public class PatternEntry2 {

	// sys, bug_id, instance_id, pattern, type of info, type of pattern,
	// type_of_issue
	public final TextInstance instance;
	public final int order;

	public PatternEntry2(TextInstance instance, int order) {
		super();
		this.instance = instance;
		this.order = order;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instance == null) ? 0 : instance.hashCode());
		result = prime * result + order;
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
		PatternEntry2 other = (PatternEntry2) obj;
		if (instance == null) {
			if (other.instance != null)
				return false;
		} else if (!instance.equals(other.instance))
			return false;
		if (order != other.order)
			return false;
		return true;
	}

}
