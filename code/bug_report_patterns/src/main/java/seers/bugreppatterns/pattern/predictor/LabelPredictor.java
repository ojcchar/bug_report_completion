package seers.bugreppatterns.pattern.predictor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.coocurrence.CooccurringPattern;
import seers.bugreppatterns.processor.PatternFeature;

public abstract class LabelPredictor {
	
	protected String granularity;
	protected boolean includeIndivFeatures;
	protected List<PatternMatcher> patterns;

	public LabelPredictor(List<PatternMatcher> patterns, String granularity, boolean includeIndivFeatures) {
		super();
		this.granularity = granularity;
		this.includeIndivFeatures = includeIndivFeatures;
		this.patterns = patterns;
	}

	public abstract PredictionOutput predictLabels(String bugRepId, String instanceId,
			LinkedHashMap<PatternMatcher, Integer> patternMatches) throws Exception;

	public List<PatternFeature> getFeaturesMatched(LinkedHashMap<PatternMatcher, Integer> patternMatches) {
		List<PatternFeature> features = new ArrayList<>();

		patternMatches.forEach((pm, val) -> {
			features.add(new PatternFeature(pm.getCode().toString(), pm.getName(), val));
		});

		return features;
	}

	public abstract Set<CooccurringPattern> getCooccurringFeatures();
}
