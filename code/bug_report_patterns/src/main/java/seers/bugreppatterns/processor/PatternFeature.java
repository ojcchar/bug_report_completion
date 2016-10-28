package seers.bugreppatterns.processor;

public class PatternFeature {

	private String id;
	private String name;
	private Object value;

	public PatternFeature(String id, String name, Object value) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

}
