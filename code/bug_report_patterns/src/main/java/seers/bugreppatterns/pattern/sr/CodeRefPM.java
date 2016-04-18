package seers.bugreppatterns.pattern.sr;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class CodeRefPM extends StepsToReproducePatternMatcher {

	final static String[] NOUNS_TERM = { "snippet", "code", "sample", "configuration", "statement", "script",
			"html/ssi", "html/fbml", "html", "trace", "test.htm" };
	final static String[] ADV_LOCATION = { "here", "below" };
	final static String[] VERB_DEMO = { "provide", "enclose", "follow", "render", "attach" };

	public int matchSentence(Sentence sentence) throws Exception {

		String text = TextProcessor.getStringFromLemmas(sentence);
		List<Token> tokens = sentence.getTokens();
		boolean isNounTerm = false;
		boolean isAdverb = false;
		boolean isVerbAsAdjective = false;
		boolean endsWith = false;
		if (text.endsWith(":") || text.matches("(?s).*from :.*") || text.endsWith("e.g.")) {
			endsWith = true;
		}
		if (text.matches("(?s).*(command line|live example|test case|file in).*")) {
			isNounTerm = true;
		}
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			// exist a noun that refer to code such as code or snippet
			if (Arrays.stream(NOUNS_TERM).anyMatch(t -> token.getLemma().equals(t))) {
				if (token.getGeneralPos().equals("NN")) {
					isNounTerm = true;
				}
			} else if (Arrays.stream(ADV_LOCATION).anyMatch(t -> token.getLemma().equals(t))
					&& ((token.getGeneralPos().equals("RB")) || token.getGeneralPos().equals("IN"))) {
				isAdverb = true;
			} else if (Arrays.stream(VERB_DEMO).anyMatch(t -> token.getLemma().equals(t))
					&& (token.getGeneralPos().equals("VB"))) {
				isVerbAsAdjective = true;
			}

		}

		if (isNounTerm && (isAdverb || isVerbAsAdjective || endsWith)) {
			return 1;
		}
		return 0;

	}

}
