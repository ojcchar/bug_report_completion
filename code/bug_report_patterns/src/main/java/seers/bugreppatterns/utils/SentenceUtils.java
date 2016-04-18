package seers.bugreppatterns.utils;

import java.util.List;

import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SentenceUtils {

	public static boolean isQuestion(Sentence sentence) {

		List<Token> tokens = sentence.getTokens();
		if (tokens.isEmpty()) {
			return false;
		}
		if (tokens.get(tokens.size() - 1).getLemma().equals("?")) {
			return true;
		} else if (tokens.size() > 1 && tokens.get(tokens.size() - 1).getLemma().equals("?")) {
			return true;
		}
		return false;
	}

}
