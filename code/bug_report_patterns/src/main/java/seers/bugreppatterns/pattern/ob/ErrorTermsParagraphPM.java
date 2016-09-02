package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;

public class ErrorTermsParagraphPM extends ObservedBehaviorPatternMatcher {

	private final static String[] SEPARATORS = new String[] { ":" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		List<Sentence> sentences = paragraph.getSentences();

		if (!sentences.isEmpty()) {
			Sentence firstSentence = sentences.get(0);

			List<Sentence> phrases = findSubSentences(firstSentence,
					findLemmasInTokens(SEPARATORS, firstSentence.getTokens()));

			Sentence firstPhrase = phrases.get(0);
			PatternMatcher pm = new ErrorNounPhrasePM();
			if (pm.matchSentence(firstPhrase) == 1 && (phrases.size() > 1 || sentences.size() > 1)) {
				return 1;
			}
		}
		return 0;

	}

}
