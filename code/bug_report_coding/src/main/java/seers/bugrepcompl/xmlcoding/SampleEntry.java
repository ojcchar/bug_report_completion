package seers.bugrepcompl.xmlcoding;

import seers.bugrepcompl.entity.TextInstance;

public class SampleEntry {

	private TextInstance instance;
	private String coder1;
	private String coder2;

	public SampleEntry(TextInstance instance, String coder1, String coder2) {
		super();
		this.instance = instance;
		this.coder1 = coder1;
		this.coder2 = coder2;
	}

	public TextInstance getInstance() {
		return instance;
	}

	public String getCoder1() {
		return coder1;
	}

	public String getCoder2() {
		return coder2;
	}

	public void setInstance(TextInstance instance) {
		this.instance = instance;
	}

	public void setCoder1(String coder1) {
		this.coder1 = coder1;
	}

	public void setCoder2(String coder2) {
		this.coder2 = coder2;
	}

	@Override
	public String toString() {
		return instance.getProject() + ";" + instance.getBugId() + ";" + coder1 + ";" + coder2;
	}

}
