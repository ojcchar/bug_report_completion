package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class LogContainsPM extends ObservedBehaviorPatternMatcher {

	public final static Set<String> LOG_VERBS = JavaUtils.getSet("be", "contain", "look", "say", "see", "show", "tell");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		List<Sentence> sentences = paragraph.getSentences();

		for (Sentence sentence : sentences) {
			List<Integer> logs = findLog(sentence.getTokens());
			if (!logs.isEmpty()) {
				List<Sentence> phrases = findSubSentences(sentence, logs);

				for (Sentence phrase : phrases) {
					if (!findOutputSignal(phrase.getTokens()).isEmpty()) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

	private List<Integer> findLog(List<Token> tokens) {
		List<Integer> logs = new ArrayList<>();

		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			if (current.getGeneralPos().equals("NN") && (current.getLemma().matches(".*[^A-Za-z]?log[s]?")
					|| current.getLemma().matches(".*[^A-Za-z]?dump[s]?"))) {
				logs.add(i);
			}
		}

		return logs;
	}

	private List<Integer> findOutputSignal(List<Token> tokens) {
		List<Integer> signal = new ArrayList<>();

		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);

			if (token.getGeneralPos().equals("VB") && SentenceUtils.lemmasContainToken(LOG_VERBS, token)) {
				signal.add(i);
			} else if (token.getLemma().equals(":")) {
				signal.add(i);
			} else if (token.getGeneralPos().equals("NN") && token.getLemma().equals("message")) {
				signal.add(i);
			}
		}
		return signal;
	}
}
