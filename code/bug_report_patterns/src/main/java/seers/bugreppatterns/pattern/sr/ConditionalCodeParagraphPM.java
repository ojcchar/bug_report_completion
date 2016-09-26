package seers.bugreppatterns.pattern.sr;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.ob.ButNegativePM;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class ConditionalCodeParagraphPM extends StepsToReproducePatternMatcher {

	final static Set<String> NOUNS_TERM = JavaUtils.getSet("snippet", "code", "statement", "script", "function",
			"method", "class", "test", "build", "program", "line", "hql", "installer", "command", "dockerfile", "tool",
			"test case", "example/test", "query",
			"docker", "file", "setup", "testcase", "query", "hql", "entity", "mapping", "config", "xml", "table",
			"macro");

	final static Set<String> VERB_TERMS = JavaUtils.getSet("run", "execute", "compile");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		List<Sentence> sentences = paragraph.getSentences();

		if (sentences.size() < 3) {
			return 0;
		}

		// ----------------------------------

		Sentence firstSentence = sentences.get(0);
		List<Integer> conditionalPositions = SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS,
				firstSentence.getTokens());

		// check the first sentence for conditional and terms expressing "code"
		// or verb expressing "execution"
		if ((conditionalPositions.isEmpty())
				|| SentenceUtils.findLemmasInTokens(NOUNS_TERM, firstSentence.getTokens()).isEmpty()
						&& SentenceUtils.findLemmasInTokens(VERB_TERMS, firstSentence.getTokens()).isEmpty()) {
			return 0;
		}

		// -------------------------
		// check for code in the second sentence
		Sentence secondSentence = sentences.get(1);

		String txt = TextProcessor.getStringFromLemmas(secondSentence);
		// select sql queries
		// sudo command...
		if (!txt.matches(".*select .* from .* where .*") && !txt.matches("sudo \\w+.*")) {
			return 0;
		}

		// --------------------------
		// check for any OB sentence

		for (int i = 2; i < sentences.size(); i++) {
			Sentence sentence = sentences.get(i);
			if (sentenceMatchesAnyPatternIn(sentence, ButNegativePM.NEGATIVE_PMS)) {
				return 1;
			}
		}

		return 0;
	}

}
