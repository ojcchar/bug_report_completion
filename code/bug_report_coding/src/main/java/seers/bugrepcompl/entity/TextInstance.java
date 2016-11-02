package seers.bugrepcompl.entity;

public class TextInstance {
	String project;
	String bugId;
	String instanceId;

	public TextInstance(String project, String bugId, String instanceId) {
		super();
		this.project = project;
		this.bugId = bugId;
		this.instanceId = instanceId;
	}

	public String getProject() {
		return project;
	}

	public String getBugId() {
		return bugId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public void setBugId(String bugId) {
		this.bugId = bugId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bugId == null) ? 0 : bugId.hashCode());
		result = prime * result + ((instanceId == null) ? 0 : instanceId.hashCode());
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
		TextInstance other = (TextInstance) obj;
		if (bugId == null) {
			if (other.bugId != null)
				return false;
		} else if (!bugId.equals(other.bugId))
			return false;
		if (instanceId == null) {
			if (other.instanceId != null)
				return false;
		} else if (!instanceId.equals(other.instanceId))
			return false;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		return true;
	}

}
