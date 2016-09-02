package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class AfterPM extends StepsToReproducePatternMatcher {

	public final static String AFTER = "after";
	private static String[] PRESENT_TENSE_VERBS = new String[] { "crashes", "builds" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		// first token is "after"
		if (tokens.size() > 3 && tokens.get(0).getLemma().equals(AFTER)) {

			// the "after" is followed by a verb in gerund and there is some other verb after that.
			if (isGerundVerb(tokens.get(1)) && !findVerbs(tokens.subList(2, tokens.size())).isEmpty()) {
				return 1;
			}
			// the "after" is followed by an adverb, which in turn is followed by a verb in gerund. There is some other
			// verb after that.
			else if (isAdverb(tokens.get(1)) && isGerundVerb(tokens.get(2))
					&& !findVerbs(tokens.subList(3, tokens.size())).isEmpty()) {
				return 1;
			}

		}

		return 0;
	}

	private boolean isAdverb(Token token) {
		if (token.getGeneralPos().equals("RB")) {
			return true;
		}
		return false;
	}

	private boolean isGerundVerb(Token token) {
		if (token.getPos().equals("VBG")) {
			return true;
		}
		return false;
	}

	private List<Integer> findVerbs(List<Token> tokens) {
		List<Integer> verbIndexes = new ArrayList<Integer>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);

			if (token.getGeneralPos().equals("VB") && !token.getPos().equals("VBG")) {
				verbIndexes.add(i);
			} else if (Arrays.stream(PRESENT_TENSE_VERBS).anyMatch(t -> token.getWord().equals(t))
					&& (tokens.get(i - 1).getGeneralPos().equals("NN")
							|| tokens.get(i - 1).getWord().equalsIgnoreCase("it"))) {
				verbIndexes.add(i);
			}
		}
		return verbIndexes;
	}

}
