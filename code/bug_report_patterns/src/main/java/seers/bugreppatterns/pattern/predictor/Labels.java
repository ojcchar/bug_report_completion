package seers.bugreppatterns.pattern.predictor;

public class Labels {

	private String isOB = "";
	private String isEB = "";
	private String isSR = "";

	private String codedBy = "seers";

	public static final String CODED_BY_DAVIES = "davies";

	public Labels(){
	}

	public Labels(String isOB, String isEB, String isSR) {
		super();
		this.isOB = new String(isOB);
		this.isEB = new String(isEB);
		this.isSR = new String(isSR);
	}

	public Labels(Labels labels) {
		this.isOB = new String(labels.isOB);
		this.isEB = new String(labels.isEB);
		this.isSR = new String(labels.isSR);
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

	public String getCodedBy() {
		return codedBy;
	}

	public void setCodedBy(String codedBy) {
		this.codedBy = codedBy;
	}

}
