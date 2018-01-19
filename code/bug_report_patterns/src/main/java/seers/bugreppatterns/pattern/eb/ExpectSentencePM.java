package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ExpectSentencePM extends ExpectedBehaviorPatternMatcher {

	public final static Set<String> WORK_VERBS = JavaUtils.getSet("work", "behave");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		// discard sentences with labels such as "expected behavior:"
		ExpBehaviorLiteralSentencePM pm = new ExpBehaviorLiteralSentencePM();
		int match = pm.matchSentence(sentence);

		List<Token> tokens = sentence.getTokens();
		// analyze the token after the label (e.g., expect. results: bla bla
		// bla --> bla bla bla)
		if (match > 0) {
			int i = findFirstToken(tokens, ":");
			tokens = tokens.subList(i + 1, tokens.size());
		}

		// -----------------------------------

		// Discard imperative sentences

		ImperativeSentencePM pm2 = new ImperativeSentencePM();
		int matchSentence = pm2.matchSentence(sentence);
		if (matchSentence > 0) {
			return 0;
		}

		// -----------------------------------

		// discard questions
		String txt = TextProcessor.getStringFromLemmas(sentence);

		if (!txt.endsWith("right ?") && SentenceUtils.isQuestion(sentence)) {
			return 0;
		}

		// -----------------------------------

		// find "expect" verbs (any conjugation)
		List<Integer> expVerbs = findMainTokens(tokens);

		for (Integer expVerb : expVerbs) {
			// discard cases as "works as expected"
			if (expVerb - 2 >= 0) {
				Token asToken = tokens.get(expVerb - 1);
				Token verbToken = tokens.get(expVerb - 2);

				if (asToken.getLemma().equals("as") && SentenceUtils.lemmasContainToken(WORK_VERBS, verbToken)) {
					return 0;
				}
			}

		}

		// accept any other case
		if (expVerbs.size() > 0) {
			return 1;
		}

		// match the noun expectation
		boolean anyMatch2 = tokens.stream()
				.anyMatch(t -> t.getLemma().equalsIgnoreCase("expectation") && t.getGeneralPos().equals("NN"));
		if (anyMatch2) {
			return 1;
		}

		return 0;
	}

	private List<Integer> findMainTokens(List<Token> tokens) {

		List<Integer> mainToks = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			// match the verb in any conjugation
			if (token.getLemma().equalsIgnoreCase("expect") && token.getGeneralPos().equals("VB")) {
				mainToks.add(i);
			}
		}
		return mainToks;
	}

	private int findFirstToken(List<Token> tokens, String lemma) {
		for (int i = 0; i < tokens.size(); i++) {
			Token t = tokens.get(i);
			if (t.getLemma().equalsIgnoreCase(lemma)) {
				return i;
			}

		}
		return -1;
	}

}
