package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.eb.CorrectIsPM;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class OutputPM extends ObservedBehaviorPatternMatcher {

	public final static Set<String> OUTPUT_NOUN_TERMS = JavaUtils.getSet("result", "output");

	private final static PatternMatcher[] NOT_ALLOWED_PMS = { new CorrectIsPM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		if (isEBPattern(sentence)) {
			return 0;
		}
		if (checkForOutputIs(tokens)) {
			PatternMatcher pm = new OutputVerbPM();
			int match = pm.matchSentence(sentence);
			if (match == 0) {
				return 1;
			}
		} else if (checkBeOutput(sentence)) {
			return 1;
		}
		return 0;
	}

	private boolean isEBPattern(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NOT_ALLOWED_PMS);
	}

	public boolean checkForOutputIs(List<Token> tokens) {

		for (int i = 0; i < (tokens.size() - 1); i++) {
			Token actual = tokens.get(i);
			Token after = tokens.get(i + 1);
			if (SentenceUtils.lemmasContainToken(OUTPUT_NOUN_TERMS, actual) && actual.getGeneralPos().equals("NN")
					&& (after.getPos().equals("VBD") || after.getPos().equals("VBZ") || after.getPos().equals("VBP"))) {
				// && after.getLemma().equals("be")) {

				return true;
			}
		}
		return false;
	}

	public boolean checkBeOutput(Sentence sentence) {
		List<Token> tokens = sentence.getTokens();
		for (int i = 0; i < (tokens.size() - 1); i++) {
			Token actual = tokens.get(i);
			if (actual.getGeneralPos().equals("VB")
					&& (actual.getWord().equals("is") || actual.getWord().equals("are"))) {
				Sentence sentence2 = new Sentence(sentence.getId(), tokens.subList(i + 1, tokens.size()));
				List<Token> tok2 = sentence2.getTokens();
				if (tok2.size() > 1) {
					Token first = tok2.get(0);
					Token second = tok2.get(1);
					if (!first.getGeneralPos().equals("VB")) {
						if (SentenceUtils.lemmasContainToken(OUTPUT_NOUN_TERMS, first)) {
							return true;
						} else if (!second.getGeneralPos().equals("VB")
								&& SentenceUtils.lemmasContainToken(OUTPUT_NOUN_TERMS, second)
								&& !first.getWord().equals("not")) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
