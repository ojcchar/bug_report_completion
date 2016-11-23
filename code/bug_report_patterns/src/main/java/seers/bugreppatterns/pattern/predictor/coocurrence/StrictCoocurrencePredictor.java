package seers.bugreppatterns.pattern.predictor.coocurrence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seers.bugrepcompl.entity.Labels;
import seers.bugreppatterns.main.prediction.HeuristicsClassifier.CooccurringFeaturesOption;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.PredictionOutput;
import seers.bugreppatterns.processor.PatternFeature;

public class StrictCoocurrencePredictor extends CoocurrencePredictor {

	private boolean strict;

	public StrictCoocurrencePredictor(List<PatternMatcher> patterns, String granularity,
			CooccurringFeaturesOption coocurrOption, String configFolder, boolean strict, boolean addCooccuringPatternsForPrediction, String cooccurFileSuffix) throws IOException {
		super(patterns, granularity, coocurrOption, configFolder, addCooccuringPatternsForPrediction, cooccurFileSuffix);
		this.strict = strict;
	}

	@Override
	public PredictionOutput predictLabels(String bugRepId, String instanceId,
			LinkedHashMap<PatternMatcher, Integer> patternMatches) throws Exception {

		return predictLabels(patternMatches, cooccurringPatterns);

	}

	private PredictionOutput predictLabels(LinkedHashMap<PatternMatcher, Integer> patternMatches,
			CooccurringPatternsData cooccurringPatternsData) {
		String isOB = "";
		String isEB = "";
		String isSR = "";

		List<PatternFeature> features = createListOfFeatures(patternMatches);

		Set<PatternMatcher> obPatterns = getPatterns(patternMatches, PatternMatcher.OB);
		List<CooccurringPattern> cooccurMatchesOB = getCooccurringMatches(cooccurringPatternsData.cooccurringPatternsOB,
				obPatterns);
		if (!cooccurMatchesOB.isEmpty()) {
			isOB = "x";
			features.addAll(getCooccurringFeatures(cooccurMatchesOB));
		}

		Set<PatternMatcher> ebPatterns = getPatterns(patternMatches, PatternMatcher.EB);
		List<CooccurringPattern> cooccurMatchesEB = getCooccurringMatches(cooccurringPatternsData.cooccurringPatternsEB,
				ebPatterns);
		if (!cooccurMatchesEB.isEmpty()) {
			isEB = "x";
			features.addAll(getCooccurringFeatures(cooccurMatchesEB));
		}

		Set<PatternMatcher> srPatterns = getPatterns(patternMatches, PatternMatcher.SR);
		List<CooccurringPattern> cooccurMatchesSR = getCooccurringMatches(cooccurringPatternsData.cooccurringPatternsSR,
				srPatterns);
		if (!cooccurMatchesSR.isEmpty()) {
			isSR = "x";
			features.addAll(getCooccurringFeatures(cooccurMatchesSR));
		}

		Labels labels = new Labels(isOB, isEB, isSR);
		PredictionOutput output = new PredictionOutput(labels, features);
		return output;
	}

	private Set<PatternMatcher> getPatterns(LinkedHashMap<PatternMatcher, Integer> patternMatches,
			String typeOfPattern) {
		return new HashSet<>(patternMatches.keySet().stream().filter(p -> typeOfPattern.equals(p.getType()))
				.collect(Collectors.toList()));
	}

	/**
	 * If strict is true:
	 * 
	 * Return the elements in cooccurringPatterns which all their components
	 * exist in patternMatches and there is no extra patterns in patternMatches.
	 * 
	 * For example, the element {pattern1-pattern2} will be returned if and only
	 * if patternMatches contain {pattern1, pattern2}
	 * 
	 * -----------------------------
	 * 
	 * If strict is false:
	 * 
	 * Return the elements in cooccurringPatterns which their components match
	 * all the patterns in patternMatches
	 * 
	 * For example, the elements {pattern1-pattern2} and
	 * {pattern1-pattern2-pattern3} will be returned if patternMatches contain
	 * {pattern1, pattern2}
	 * 
	 * @param cooccurringPatterns
	 * @param patternMatches
	 * @return
	 */
	private List<CooccurringPattern> getCooccurringMatches(Set<CooccurringPattern> cooccurringPatterns,
			Set<PatternMatcher> patternMatches) {

		List<CooccurringPattern> matches = new ArrayList<>();

		if (patternMatches.isEmpty()) {
			return matches;
		}

		for (CooccurringPattern pattern : cooccurringPatterns) {

			boolean matchesAllPM = true;
			for (PatternMatcher pm : patternMatches) {
				if (!pattern.getCooccurringPatterns().contains(pm.getName())) {
					matchesAllPM = false;
					break;
				}
			}

			if (strict) {
				if (matchesAllPM && pattern.getCooccurringPatterns().size() == patternMatches.size()) {
					matches.add(pattern);
				}
			} else {
				if (matchesAllPM) {
					matches.add(pattern);
				}
			}

		}

		return matches;
	}

}
