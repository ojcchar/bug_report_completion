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

}
