package seers.bugreppatterns.pattern.predictor.coocurrence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.LabelPredictor;
import seers.bugreppatterns.pattern.predictor.Labels;
import seers.bugreppatterns.pattern.predictor.PredictionOutput;
import seers.bugreppatterns.processor.PatternFeature;

public class CoocurrencePredictor extends LabelPredictor {

	protected static CooccurringPatternsData cooccurringPatterns;
	protected static Set<CooccurringPattern> allCooccurringPatterns;

	public CoocurrencePredictor(List<PatternMatcher> patterns, String granularity, boolean includeIndivFeatures, String configFolder)
			throws IOException {
		super(patterns, granularity, includeIndivFeatures);
		loadCooccurringPatterns(configFolder);
	}

	private synchronized void loadCooccurringPatterns(String configFolder) throws IOException {

		// patterns already loaded
		if (cooccurringPatterns != null) {
			return;
		}

		String dataFilePath = configFolder + File.separator + "coocurrence-" + granularity + ".csv";
		cooccurringPatterns = new CooccurringPatternsData(dataFilePath, patterns);

		allCooccurringPatterns = new LinkedHashSet<>();
		allCooccurringPatterns.addAll(cooccurringPatterns.cooccurringPatternsOB);
		allCooccurringPatterns.addAll(cooccurringPatterns.cooccurringPatternsEB);
		allCooccurringPatterns.addAll(cooccurringPatterns.cooccurringPatternsSR);
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

		List<PatternFeature> features = new ArrayList<>();
		if (includeIndivFeatures) {
			features = new ArrayList<>(getFeaturesMatched(patternMatches));
		}

		List<CooccurringPattern> cooccurMatchesOB = getCooccurringMatches(cooccurringPatternsData.cooccurringPatternsOB,
				patternMatches);
		if (!cooccurMatchesOB.isEmpty()) {
			isOB = "x";
			features.addAll(getCooccurringFeatures(cooccurMatchesOB));
		}

		List<CooccurringPattern> cooccurMatchesEB = getCooccurringMatches(cooccurringPatternsData.cooccurringPatternsEB,
				patternMatches);
		if (!cooccurMatchesEB.isEmpty()) {
			isEB = "x";
			features.addAll(getCooccurringFeatures(cooccurMatchesEB));
		}

		List<CooccurringPattern> cooccurMatchesSR = getCooccurringMatches(cooccurringPatternsData.cooccurringPatternsSR,
				patternMatches);
		if (!cooccurMatchesSR.isEmpty()) {
			features.addAll(getCooccurringFeatures(cooccurMatchesSR));
			isSR = "x";
		}

		Labels labels = new Labels(isOB, isEB, isSR);
		PredictionOutput output = new PredictionOutput(labels, features);
		return output;
	}

	protected List<PatternFeature> getCooccurringFeatures(List<CooccurringPattern> cooccurMatches) {
		List<PatternFeature> features = new ArrayList<>();

		cooccurMatches.forEach(pattern -> {

			if (includeIndivFeatures) {
				if (!pattern.isIndividual()) {
					PatternFeature feature = new PatternFeature(pattern.getId().toString(), pattern.getName(), "1");
					features.add(feature);
				}
			} else {
				PatternFeature feature = new PatternFeature(pattern.getId().toString(), pattern.getName(), "1");
				features.add(feature);
			}
		});

		return features;
	}

	private List<CooccurringPattern> getCooccurringMatches(Set<CooccurringPattern> cooccurringPatterns,
			LinkedHashMap<PatternMatcher, Integer> patternMatches) {

		List<CooccurringPattern> matches = new ArrayList<>();

		Set<PatternMatcher> patternMatchesSet = patternMatches.keySet();

		for (CooccurringPattern pattern : cooccurringPatterns) {

			boolean aPatternDoesNotMatch = pattern.getCooccurringPatterns().stream()
					.anyMatch(indivPatterns -> !containsPattern(indivPatterns, patternMatchesSet));
			boolean allIndivPatternsMatch = !aPatternDoesNotMatch;
			if (allIndivPatternsMatch) {
				matches.add(pattern);
			}

		}

		return matches;
	}

	protected boolean containsPattern(String pattern, Set<PatternMatcher> patternMatches) {
		PatternMatcher patt = PatternMatcher.createFakePattern(pattern);
		return patternMatches.contains(patt);
	}

	@Override
	public Set<CooccurringPattern> getCooccurringFeatures() {
		return allCooccurringPatterns;
	}

}
