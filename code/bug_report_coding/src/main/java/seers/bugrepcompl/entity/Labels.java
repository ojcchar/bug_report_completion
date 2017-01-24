package seers.bugrepcompl.entity;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codedBy == null) ? 0 : codedBy.hashCode());
		result = prime * result + ((isEB == null) ? 0 : isEB.hashCode());
		result = prime * result + ((isOB == null) ? 0 : isOB.hashCode());
		result = prime * result + ((isSR == null) ? 0 : isSR.hashCode());
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
		Labels other = (Labels) obj;
		if (codedBy == null) {
			if (other.codedBy != null)
				return false;
		} else if (!codedBy.equals(other.codedBy))
			return false;
		if (isEB == null) {
			if (other.isEB != null)
				return false;
		} else if (!isEB.equals(other.isEB))
			return false;
		if (isOB == null) {
			if (other.isOB != null)
				return false;
		} else if (!isOB.equals(other.isOB))
			return false;
		if (isSR == null) {
			if (other.isSR != null)
				return false;
		} else if (!isSR.equals(other.isSR))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Labels [isOB=" + isOB + ", isEB=" + isEB + ", isSR=" + isSR + ", codedBy=" + codedBy + "]";
	}
	
	

}
