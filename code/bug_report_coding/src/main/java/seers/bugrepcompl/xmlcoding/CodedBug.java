package seers.bugrepcompl.xmlcoding;

import seers.bugrepcompl.entity.Labels;

public class CodedBug {

	private boolean bug = true;
	private Labels labels;

	public CodedBug(boolean bug, Labels labels) {
		super();
		this.bug = bug;
		this.labels = labels;
	}

	public boolean isBug() {
		return bug;
	}

	public void setBug(boolean bug) {
		this.bug = bug;
	}

	public Labels getLabels() {
		return labels;
	}

	public void setLabels(Labels labels) {
		this.labels = labels;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (bug ? 1231 : 1237);
		result = prime * result + ((labels == null) ? 0 : labels.hashCode());
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
		CodedBug other = (CodedBug) obj;
		if (bug != other.bug)
			return false;
		if (labels == null) {
			if (other.labels != null)
				return false;
		} else if (!labels.equals(other.labels))
			return false;
		return true;
	}
	
	

}
