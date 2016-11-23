package seers.bugreppatterns.pattern.predictor.coocurrence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seers.bugrepcompl.entity.Labels;
import seers.bugreppatterns.main.prediction.HeuristicsClassifier.CooccurringFeaturesOption;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.LabelPredictor;
import seers.bugreppatterns.pattern.predictor.PredictionOutput;
import seers.bugreppatterns.processor.PatternFeature;

public class CoocurrencePredictor extends LabelPredictor {

	protected static CooccurringPatternsData cooccurringPatterns;
	protected static Set<CooccurringPattern> allCooccurringPatterns;
	private String cooccurFileSuffix;

	public CoocurrencePredictor(List<PatternMatcher> patterns, String granularity,
			CooccurringFeaturesOption coocurrOption, String configFolder, boolean addCooccuringPatternsForPrediction,
			String cooccurFileSuffix) throws IOException {
		super(patterns, granularity, coocurrOption, addCooccuringPatternsForPrediction);
		this.cooccurFileSuffix = cooccurFileSuffix;
		loadCooccurringPatterns(configFolder);
	}

	private synchronized void loadCooccurringPatterns(String configFolder) throws IOException {

		// patterns already loaded
		if (cooccurringPatterns != null) {
			return;
		}

		// load co-occurring patterns
		String suffix = cooccurFileSuffix.trim();
		if ("null".equals(suffix)) {
			suffix = "";
		} else {
			suffix = "-" + suffix;
		}

		String dataFilePath = configFolder + File.separator + "coocurrence-" + granularity + suffix + ".csv";
		cooccurringPatterns = new CooccurringPatternsData(dataFilePath, patterns, addCooccuringPatternsForPrediction);

		// add all co-occurring patterns (or features)
		allCooccurringPatterns = new LinkedHashSet<>();
		switch (coocurrOption) {
		case ONLY_COOCCURRING:
			allCooccurringPatterns.addAll(cooccurringPatterns.cooccurringPatternsOB);
			allCooccurringPatterns.addAll(cooccurringPatterns.cooccurringPatternsEB);
			allCooccurringPatterns.addAll(cooccurringPatterns.cooccurringPatternsSR);
			break;
		case INDIV_AND_COOCCURR:
			allCooccurringPatterns.addAll(cooccurringPatterns.cooccurringPatternsOB.stream()
					.filter(p -> !p.isIndividual()).collect(Collectors.toList()));
			allCooccurringPatterns.addAll(cooccurringPatterns.cooccurringPatternsEB.stream()
					.filter(p -> !p.isIndividual()).collect(Collectors.toList()));
			allCooccurringPatterns.addAll(cooccurringPatterns.cooccurringPatternsSR.stream()
					.filter(p -> !p.isIndividual()).collect(Collectors.toList()));
			break;
		default:
			break;
		}
	}

	@Override
	public PredictionOutput predictLabels(String bugRepId, String instanceId,
			LinkedHashMap<PatternMatcher, Integer> patternMatches) throws Exception {

		return predictLabels(patternMatches, cooccurringPatterns);
	}

	private PredictionOutput predictLabels(LinkedHashMap<PatternMatcher, Integer> patternMatches,
			CooccurringPatternsData cooccurringPatternsData) {

		// ---------------------------------------------

		String isOB = "";
		String isEB = "";
		String isSR = "";

		List<PatternFeature> features = createListOfFeatures(patternMatches);

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
			isSR = "x";
			features.addAll(getCooccurringFeatures(cooccurMatchesSR));
		}

		Labels labels = new Labels(isOB, isEB, isSR);
		PredictionOutput output = new PredictionOutput(labels, features);
		return output;
	}

	protected List<PatternFeature> createListOfFeatures(LinkedHashMap<PatternMatcher, Integer> patternMatches) {
		List<PatternFeature> features = new ArrayList<>();
		if (coocurrOption.equals(CooccurringFeaturesOption.INDIV_AND_COOCCURR)) {
			features = new ArrayList<>(getFeaturesMatched(patternMatches));
		}
		return features;
	}

	protected List<PatternFeature> getCooccurringFeatures(List<CooccurringPattern> cooccurMatches) {
		List<PatternFeature> features = new ArrayList<>();

		cooccurMatches.forEach(pattern -> {

			if (coocurrOption.equals(CooccurringFeaturesOption.INDIV_AND_COOCCURR)) {
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

	/**
	 * Return the elements in cooccurringPatterns which all their components
	 * exist in patternMatches, even if extra patterns exist in patternMatches.
	 * 
	 * For example, the element {pattern1-pattern2} will be return if
	 * patternMatches contain {pattern1, pattern2, pattern3}
	 * 
	 * @param cooccurringPatterns
	 * @param patternMatches
	 * @return
	 */
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
