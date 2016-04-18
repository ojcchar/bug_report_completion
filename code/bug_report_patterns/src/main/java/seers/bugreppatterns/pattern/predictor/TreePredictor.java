package seers.bugreppatterns.pattern.predictor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;

public class TreePredictor extends LabelPredictor {

	@Override
	public Labels predictLabels(String bugRepId, String instanceId,
			LinkedHashMap<PatternMatcher, Integer> patternMatches, String granularity) throws Exception {

		return predictLabelsSentence(patternMatches);

		// if ("S".equals(granularity)) {
		// return predictLabelsSentence(patternMatches);
		// } else if ("P".equals(granularity)) {
		//// return predictLabelsSentence(patternMatches);
		// return predictLabelsParagraph(patternMatches);
		// } else if ("B".equals(granularity)) {
		//// return predictLabelsSentence(patternMatches);
		// return predictLabelsBugs(patternMatches);
		// }
		// return null;
	}

	private Labels predictLabelsBugs(LinkedHashMap<PatternMatcher, Integer> patternMatches) {

		return predictLabelsParagraph(patternMatches);
	}

	private Labels predictLabelsParagraph(LinkedHashMap<PatternMatcher, Integer> patternMatches) {

		List<Entry<PatternMatcher, Integer>> patterns = patternMatches.entrySet().stream()
				.filter(p -> PatternMatcher.OB.equals(p.getKey().getType())).collect(Collectors.toList());
		List<Entry<PatternMatcher, Integer>> topPatterns = getTopPatterns(patterns);

		return new Labels("", "", "");
	}

	private List<Entry<PatternMatcher, Integer>> getTopPatterns(List<Entry<PatternMatcher, Integer>> patterns) {
		patterns.sort((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue()));

		if (patterns.size() < 3) {
			return new ArrayList<>(patterns);
		} else {
			return patterns.subList(0, 3);
		}
	}

	private Labels predictLabelsSentence(LinkedHashMap<PatternMatcher, Integer> patternMatches) {

		List<PatternMatcher> obPatterns = patternMatches.keySet().stream()
				.filter(p -> PatternMatcher.OB.equals(p.getType())).collect(Collectors.toList());

		List<PatternMatcher> ebPatterns = patternMatches.keySet().stream()
				.filter(p -> PatternMatcher.EB.equals(p.getType())).collect(Collectors.toList());

		List<PatternMatcher> srPatterns = patternMatches.keySet().stream()
				.filter(p -> PatternMatcher.SR.equals(p.getType())).collect(Collectors.toList());

		if (didPatternMatch(patternMatches, "P_SR_LABELED_LIST") && !obPatterns.isEmpty() && !ebPatterns.isEmpty()) {
			return new Labels("x", "x", "x");
		} else if (didPatternMatch(patternMatches, "P_SR_LABELED_LIST") && !obPatterns.isEmpty()) {
			return new Labels("x", "", "x");
		} else if (didPatternMatch(patternMatches, "P_SR_LABELED_LIST") && !ebPatterns.isEmpty()) {
			return new Labels("", "x", "x");
		} else

		// ----------------

		if (didPatternMatch(patternMatches, "S_OB_BUT_NEG") && didPatternMatch(patternMatches, "S_EB_SHOULD")) {
			return new Labels("x", "x", "");
		} else if (didPatternMatch(patternMatches, "S_OB_STILL")
				&& didPatternMatch(patternMatches, "S_EB_SUPPOSED_TO")) {
			return new Labels("x", "x", "");
		} else if (didPatternMatch(patternMatches, "S_OB_NEG_ADV_ADJ")
				&& didPatternMatch(patternMatches, "S_EB_INSTEAD_OF_EXP_BEHAVIOR")) {
			return new Labels("x", "x", "");
		} else if (didPatternMatch(patternMatches, "S_OB_INSTEAD_OF")
				&& didPatternMatch(patternMatches, "S_EB_INSTEAD_OF_EXP_BEHAVIOR")) {
			return new Labels("x", "x", "");
		} else if (didPatternMatch(patternMatches, "S_OB_PROBLEM")
				&& didPatternMatch(patternMatches, "S_EB_SUPPOSED_TO")) {
			return new Labels("x", "x", "");
		} else if (didPatternMatch(patternMatches, "S_OB_BUT_NEG")
				&& didPatternMatch(patternMatches, "S_EB_EXPECTED")) {
			return new Labels("x", "x", "");
		} else if (didPatternMatch(patternMatches, "S_OB_NEG_AUX_VERB")
				&& didPatternMatch(patternMatches, "S_EB_SHOULD")) {
			return new Labels("x", "x", "");
		} else if (didPatternMatch(patternMatches, "S_OB_NEG_ADV_ADJ")
				&& didPatternMatch(patternMatches, "S_EB_MUST")) {
			return new Labels("x", "x", "");
		} else if (didPatternMatch(patternMatches, "S_OB_NEG_AUX_VERB")
				&& didPatternMatch(patternMatches, "S_EB_WANT")) {
			return new Labels("x", "x", "");
		} else if (didPatternMatch(patternMatches, "S_OB_NEG_ADV_ADJ")
				&& didPatternMatch(patternMatches, "S_EB_NEED_TO")) {
			return new Labels("x", "x", "");
		} else if (didPatternMatch(patternMatches, "S_OB_NEG_ADV_ADJ")
				&& didPatternMatch(patternMatches, "S_EB_SHOULD")) {
			return new Labels("x", "x", "");
		} else if (didPatternMatch(patternMatches, "S_OB_BUT_NEG")
				&& didPatternMatch(patternMatches, "S_EB_WOULD_LIKE")) {
			return new Labels("x", "x", "");
		}
		// ----------------------------------------------
		else if (didPatternMatch(patternMatches, "S_OB_VERB_ERROR")
				&& didPatternMatch(patternMatches, "S_SR_CODE_REF")) {
			return new Labels("x", "", "x");
		} else if (didPatternMatch(patternMatches, "S_OB_NEG_VERB")
				&& didPatternMatch(patternMatches, "S_SR_CODE_REF")) {
			return new Labels("x", "", "x");
		}
		// ----------------------------------------------
		else if (didPatternMatch(patternMatches, "S_EB_SHOULD") && didPatternMatch(patternMatches, "S_SR_CODE_REF")) {
			return new Labels("", "x", "x");
		}
		// ----------------------------------------------
		else if (!ebPatterns.isEmpty()) {
			return new Labels("", "x", "");
		} else if (!srPatterns.isEmpty()) {
			return new Labels("", "", "x");
		} else if (!obPatterns.isEmpty()) {
			return new Labels("x", "", "");
		}

		return new Labels("", "", "");
	}

	private boolean didPatternMatch(LinkedHashMap<PatternMatcher, Integer> patternMatches, String name) {
		PatternMatcher patt = new PatternMatcher() {

			@Override
			public int matchSentence(Sentence sentence) throws Exception {
				return 0;
			}

			@Override
			public String getType() {
				return null;
			}
		};
		patt.setName(name);
		Integer integer = patternMatches.get(patt);
		if (integer != null) {
			return true;
		}

		return false;
	}

}
