package seers.bugreppatterns.pattern.predictor;

import java.util.List;

import seers.bugrepcompl.entity.Labels;
import seers.bugreppatterns.processor.PatternFeature;

public class PredictionOutput {

	private Labels labels;
	private List<PatternFeature> features;
	
	public PredictionOutput(Labels labels, List<PatternFeature> features) {
		super();
		this.labels = labels;
		this.features = features;
	}

	public Labels getLabels() {
		return labels;
	}

	public List<PatternFeature> getFeatures() {
		return features;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((features == null) ? 0 : features.hashCode());
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
		PredictionOutput other = (PredictionOutput) obj;
		if (features == null) {
			if (other.features != null)
				return false;
		} else if (!features.equals(other.features))
			return false;
		if (labels == null) {
			if (other.labels != null)
				return false;
		} else if (!labels.equals(other.labels))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PredictionOutput [labels=" + labels + ", features=" + features + "]";
	}
	
	
	

}
