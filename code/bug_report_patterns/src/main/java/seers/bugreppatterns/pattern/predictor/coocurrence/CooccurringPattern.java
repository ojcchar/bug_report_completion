package seers.bugreppatterns.pattern.predictor.coocurrence;

import java.util.LinkedHashSet;
import java.util.concurrent.atomic.AtomicInteger;

public class CooccurringPattern {

	private static final AtomicInteger ID_SEQUENCE = new AtomicInteger(200);

	private int id;
	private String name;
	private LinkedHashSet<String> cooccurringPatterns;
	private boolean isIndividual;

	public CooccurringPattern(LinkedHashSet<String> cooccurringPatterns, boolean isIndividual) {
		this(ID_SEQUENCE.getAndIncrement(), cooccurringPatterns, isIndividual);
	}

	public CooccurringPattern(int id, LinkedHashSet<String> cooccurringPatterns, boolean isIndividual) {
		super();
		this.id = id;
		this.cooccurringPatterns = cooccurringPatterns;
		this.isIndividual = isIndividual;
		assignName(cooccurringPatterns);
	}

	private void assignName(LinkedHashSet<String> cooccurringPatterns) {

		StringBuffer buffer = new StringBuffer();
		for (String pattern : cooccurringPatterns) {
			buffer.append(pattern);
			buffer.append("-");
		}

		if (buffer.length() != 0) {
			buffer.delete(buffer.length() - 1, buffer.length());
		}

		this.name = buffer.toString();
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LinkedHashSet<String> getCooccurringPatterns() {
		return cooccurringPatterns;
	}

	public boolean isIndividual() {
		return isIndividual;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		CooccurringPattern other = (CooccurringPattern) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
