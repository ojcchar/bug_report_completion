package seers.bugreppatterns.pattern.predictor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import seers.bugreppatterns.pattern.PatternMatcher;

public class AnyMatchPredictor extends LabelPredictor {

	@Override
	public Labels predictLabels(String bugRepId, String instanceId,
			LinkedHashMap<PatternMatcher, Integer> patternMatches, String granularity) throws Exception {
		String isOB = "";
		String isEB = "";
		String isSR = "";

		List<PatternMatcher> patterns = patternMatches.keySet().stream()
				.filter(p -> PatternMatcher.OB.equals(p.getType())).collect(Collectors.toList());
		if (!patterns.isEmpty()) {
			isOB = "x";
		}

		patterns = patternMatches.keySet().stream().filter(p -> PatternMatcher.EB.equals(p.getType()))
				.collect(Collectors.toList());
		if (!patterns.isEmpty()) {
			isEB = "x";
		}

		patterns = patternMatches.keySet().stream().filter(p -> PatternMatcher.SR.equals(p.getType()))
				.collect(Collectors.toList());
		if (!patterns.isEmpty()) {
			isSR = "x";
		}

		return new Labels(isOB, isEB, isSR);
	}

}
