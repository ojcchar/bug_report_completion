package seers.bugreppatterns.pattern.sr;

import java.util.Arrays;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class MenuNavigationPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		// return 0;
		String txt = TextProcessor.getStringFromLemmas(sentence);
		txt = removeParentheses(txt);
		boolean match = checkForSeparators(txt);

		return match ? 1 : 0;
	}

	private String removeParentheses(String txt) {
		String txtBuf = txt;
		for (int i = 0; i < TextProcessor.PARENTHESIS.length; i++) {
			String p = TextProcessor.PARENTHESIS[i].toLowerCase();
			txtBuf = txtBuf.replace(p, "");
		}
		return txtBuf;
	}

	public final static String[] SEPARATORS_REGEX = { " - > ", " - ", " \\| ", " -- " };

	// TODO: we should check for separator manually, token by token, and avoid
	// cases like "rpm -i -l"
	private boolean checkForSeparators(String txt) {
		for (int i = 0; i < SEPARATORS_REGEX.length; i++) {
			String sep = SEPARATORS_REGEX[i];
			String[] split = txt.split(sep);
			if (split.length > 2 && !Arrays.stream(split).anyMatch(s -> s.trim().isEmpty())) {
				return true;
			}
		}

		return false;
	}

}
