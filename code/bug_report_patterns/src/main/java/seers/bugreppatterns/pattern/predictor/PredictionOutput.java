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

}
