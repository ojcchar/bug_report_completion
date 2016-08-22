package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class BeforePastClausePM extends ExpectedBehaviorPatternMatcher {

	private static final String[] BEFORE_EXPRESSIONS = { "before that", "previously", "in the past", "prior to",
			"before" };

	private static final int TOKENS_THRESHOLD;
	static {
		int maxExprLength = 0;
		for (String expr : BEFORE_EXPRESSIONS) {
			int length = expr.split(" ").length;
			if (maxExprLength < length) {
				maxExprLength = length;
			}
		}
		TOKENS_THRESHOLD = maxExprLength + 3;
	}

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		Integer idx = getFirstComma(tokens);

		if (idx == null) {
			return 0;
		}

		if (matchesBeforeExpressions(tokens.subList(0, idx))) {
			return checkPastClause(tokens.subList(idx + 1, tokens.size()));
		}

		return 0;
	}

	private boolean matchesBeforeExpressions(List<Token> tokens) {

		String txt = TextProcessor.getStringFromLemmas(new Sentence("0", tokens));

		return Arrays.stream(BEFORE_EXPRESSIONS).anyMatch(exp -> txt.contains(exp));
	}

	private Integer getFirstComma(List<Token> tokens) {
		for (int i = 0; i < tokens.size() && i < TOKENS_THRESHOLD; i++) {
			Token token = tokens.get(i);
			if (token.getLemma().equals(",") || token.getLemma().equals("_")) {
				return i;
			}
		}
		return null;
	}

	private int checkPastClause(List<Token> tokens) {
		List<Integer> verbs = findMainTokens(tokens);
		for (Integer verb : verbs) {
			Token verbToken = tokens.get(verb);

			// no previous token
			if (verb - 1 < 0) {
				continue;
			}
			Token previousToken = tokens.get(verb - 1);

			// case1: simple clauses in past tense, eg.: "I solved"
			if (verbToken.getPos().equals("VBD")) {
				if (previousToken.getGeneralPos().equals("PRP") || previousToken.getGeneralPos().equals("DT")
						|| previousToken.getGeneralPos().equals("NN")) {
					return 1;
				}

				// case2: "was/were solved"
			} else if (verbToken.getPos().equals("VBN")) {
				if (previousToken.getLemma().equals("be")) {
					return 1;
				}

				// case 3: "would solve"
			} else if (verbToken.getPos().equals("VB")) {
				if (previousToken.getLemma().equals("would")) {
					return 1;
				}
			}
		}
		return 0;

	}

	// all verbs
	private List<Integer> findMainTokens(List<Token> tokens) {

		List<Integer> mainToks = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")) {
				mainToks.add(i);
			}
		}
		return mainToks;
	}
}
