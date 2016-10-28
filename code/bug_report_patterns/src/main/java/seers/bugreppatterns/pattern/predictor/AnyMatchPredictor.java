package seers.bugreppatterns.pattern.predictor;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.coocurrence.CooccurringPattern;

public class AnyMatchPredictor extends LabelPredictor {

	public AnyMatchPredictor(String granularity) {
		super(granularity);
	}

	@Override
	public PredictionOutput predictLabels(String bugRepId, String instanceId,
			LinkedHashMap<PatternMatcher, Integer> patternMatches) throws Exception {

		String isOB = "";
		String isEB = "";
		String isSR = "";

		// if there is any OB pattern, then the instance is classified as OB
		Set<PatternMatcher> keySet = patternMatches.keySet();
		if (keySet.stream().anyMatch(p -> PatternMatcher.OB.equals(p.getType()))) {
			isOB = "x";
		}

		// if there is any EB pattern, then the instance is classified as EB
		if (keySet.stream().anyMatch(p -> PatternMatcher.EB.equals(p.getType()))) {
			isEB = "x";
		}

		// if there is any SR pattern, then the instance is classified as SR
		if (keySet.stream().anyMatch(p -> PatternMatcher.SR.equals(p.getType()))) {
			isSR = "x";
		}

		PredictionOutput output = new PredictionOutput(new Labels(isOB, isEB, isSR),
				getFeaturesMatched(patternMatches));

		return output;
	}

	@Override
	public Set<CooccurringPattern> getCooccurringFeatures() {
		return new HashSet<>();
	}

}
