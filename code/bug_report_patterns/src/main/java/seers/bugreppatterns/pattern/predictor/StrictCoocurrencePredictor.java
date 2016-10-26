package seers.bugreppatterns.pattern.predictor;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import seers.bugreppatterns.pattern.PatternMatcher;

public class StrictCoocurrencePredictor extends CoocurrencePredictor {

	private boolean strict;

	public StrictCoocurrencePredictor(String configFolder, boolean strict) throws IOException {
		super(configFolder);
		this.strict = strict;
	}

	@Override
	public Labels predictLabels(String bugRepId, String instanceId,
			LinkedHashMap<PatternMatcher, Integer> patternMatches, String granularity) throws Exception {

		if ("B".equals(granularity)) {
			return predictLabelsForBugs(patternMatches);
		}

		return new Labels();
	}

	private Labels predictLabelsForBugs(LinkedHashMap<PatternMatcher, Integer> patternMatches) {
		String isOB = "";
		String isEB = "";
		String isSR = "";

		Set<PatternMatcher> obPatterns = getPatterns(patternMatches, PatternMatcher.OB);
		if (matchesAllPattern(cooccurringPatternsOB, obPatterns)) {
			isOB = "x";
		}

		Set<PatternMatcher> ebPatterns = getPatterns(patternMatches, PatternMatcher.EB);
		if (matchesAllPattern(cooccurringPatternsEB, ebPatterns)) {
			isEB = "x";
		}

		Set<PatternMatcher> srPatterns = getPatterns(patternMatches, PatternMatcher.SR);
		if (matchesAllPattern(cooccurringPatternsSR, srPatterns)) {
			isSR = "x";
		}

		return new Labels(isOB, isEB, isSR);
	}

	private Set<PatternMatcher> getPatterns(LinkedHashMap<PatternMatcher, Integer> patternMatches,
			String typeOfPattern) {
		return new HashSet<>(patternMatches.keySet().stream().filter(p -> typeOfPattern.equals(p.getType()))
				.collect(Collectors.toList()));
	}

	private boolean matchesAllPattern(Set<LinkedHashSet<String>> cooccurringPatterns,
			Set<PatternMatcher> patternMatches) {

		if (patternMatches.isEmpty()) {
			return false;
		}

		LinkedHashSet<String> cooccur = null;

		for (LinkedHashSet<String> patterns : cooccurringPatterns) {

			boolean matchesAllPM = true;
			for (PatternMatcher pm : patternMatches) {
				if (!patterns.contains(pm.getName())) {
					matchesAllPM = false;
					break;
				}
			}

			if (strict) {
				if (matchesAllPM && patterns.size() == patternMatches.size()) {
					cooccur = patterns;
					break;
				}
			} else {
				if (matchesAllPM) {
					cooccur = patterns;
					break;
				}
			}

		}

//		if (cooccur != null) {
//			LOGGER.debug(cooccur.toString());
//		}

		return cooccur != null;
	}

}
