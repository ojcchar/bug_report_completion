package seers.bugreppatterns.pattern.predictor;

public class Labels {

	private String isOB = "";
	private String isEB = "";
	private String isSR = "";

	public Labels(String isOB, String isEB, String isSR) {
		super();
		this.isOB = isOB;
		this.isEB = isEB;
		this.isSR = isSR;
	}

	public String getIsOB() {
		return isOB;
	}

	public String getIsEB() {
		return isEB;
	}

	public String getIsSR() {
		return isSR;
	}

	public void setIsOB(String isOB) {
		this.isOB = isOB;
	}

	public void setIsEB(String isEB) {
		this.isEB = isEB;
	}

	public void setIsSR(String isSR) {
		this.isSR = isSR;
	}

}
