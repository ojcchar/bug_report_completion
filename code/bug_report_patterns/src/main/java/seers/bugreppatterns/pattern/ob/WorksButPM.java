package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class WorksButPM extends ObservedBehaviorPatternMatcher {

	public final static Set<String> FINE_TERM = JavaUtils.getSet( "fine", "great", "ok", "normally", "correctly", "flawlessly",
			"perfectly", "properly" );

	public final static Set<String> BUT_TERM = JavaUtils.getSet( "but", "except", "until", "however", "then", "although", "though",
			"nevertheless" );

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		int indexWork = workIndex(tokens);

		if (indexWork != -1) {
			// check for the fine term
			Token tok = tokens.get(indexWork + 1);
			if (SentenceUtils.lemmasContainToken(FINE_TERM, tok)) {
				// check for the contrast term
				Sentence sentence2 = new Sentence(sentence.getId(), tokens.subList(indexWork + 2, tokens.size()));
				List<Token> tokens2 = sentence2.getTokens();
				for (int i = 0; i < tokens2.size(); i++) {
					Token token = tokens2.get(i);
					if (SentenceUtils.lemmasContainToken(BUT_TERM, token)) {
						return 1;
					}
				}
			}
		}

		int succeedIndex = succeedIndex(tokens);
		if (succeedIndex != -1) {
			Sentence sentence2 = new Sentence(sentence.getId(), tokens.subList(succeedIndex + 1, tokens.size()));
			List<Token> tokens2 = sentence2.getTokens();
			for (int i = 0; i < tokens2.size(); i++) {
				Token tok = tokens2.get(i);
				if (SentenceUtils.lemmasContainToken(BUT_TERM, tok)) {
					return 1;
				}
			}
		}

		return 0;
	}

	private int succeedIndex(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).getGeneralPos().equals("VB")
					&& (tokens.get(i).getLemma().equals("succeed") || tokens.get(i).getLemma().equals("work"))) {
				if (!tokens.get(i - 1).getLemma().equals("not")) {
					return i;
				}
			}
		}
		return -1;
	}

	private int workIndex(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).getGeneralPos().equals("VB") && (tokens.get(i).getLemma().equals("work")
					|| tokens.get(i).getLemma().equals("function") || tokens.get(i).getLemma().equals("run"))) {
				if (i > 0 && (!tokens.get(i - 1).getLemma().equals("not"))) {
					return i;
				}

			}
		}
		return -1;
	}

}
