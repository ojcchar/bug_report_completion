package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class FrequencyAdverbPM extends ObservedBehaviorPatternMatcher {

	final private static String FA = "always|annually|constantly|continually|daily|eventually|ever|every day|every month|"
			+ "every time|every week|every year|fortnightly|frequently|from time to time|generally|infrequently|"
			+ "intermittently|monthly|most of the time|never|normally|now and then|occasionally|often|once in a while|periodically|rarely|"
			+ "regularly|seldom|sometimes|some time|usually|weekly|yearly";

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		String text = TextProcessor.getStringFromLemmas(sentence);
		boolean containsFreqAdv = text.matches(".*[^a-z]?(" + FA + ")[^a-z]?.*");

		List<Token> tokens = sentence.getTokens();
		List<Integer> verbs = findVerbs(tokens);

		if (containsFreqAdv && !verbs.isEmpty() && !isEBModal(tokens)) {
			return 1;
		}
		return 0;
	}

	private List<Integer> findVerbs(List<Token> tokens) {

		List<Integer> verbs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")) {

				verbs.add(i);
			}
		}
		return verbs;
	}

}
