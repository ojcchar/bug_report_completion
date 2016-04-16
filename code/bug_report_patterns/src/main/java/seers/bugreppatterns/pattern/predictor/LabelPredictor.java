package seers.bugreppatterns.pattern.predictor;

import java.util.LinkedHashMap;

import seers.bugreppatterns.pattern.PatternMatcher;

public abstract class LabelPredictor {

	public abstract Labels predictLabels(String bugRepId, String instanceId,
			LinkedHashMap<PatternMatcher, Integer> patternMatches) throws Exception;
}
