package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ErrorTermsParagraphPM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new ProblemInPM(), new ErrorTermSubjectPM(),
			new ErrorNounPhrasePM() };

	private final static Set<String> PUNCTUATION = JavaUtils.getSet(",", ";");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		List<Sentence> sentences = paragraph.getSentences();

		if (!sentences.isEmpty()) {
			Sentence firstSentence = sentences.get(0);
			List<Integer> colonIndexes = SentenceUtils.findLemmasInTokens(JavaUtils.getSet(":"),
					firstSentence.getTokens());

			if (!colonIndexes.isEmpty()) {
				Sentence beforeColonSentence = new Sentence(firstSentence.getId(),
						firstSentence.getTokens().subList(0, colonIndexes.get(0)));

				List<Sentence> phrases = SentenceUtils.findSubSentences(beforeColonSentence,
						findPunctuation(beforeColonSentence.getTokens()));
				Sentence lastPhrase = phrases.get(phrases.size() - 1);

				if (isNegative(lastPhrase) && (phrases.size() > 1 || sentences.size() > 1
						|| beforeColonSentence.getTokens().size() < firstSentence.getTokens().size())) {
					return 1;
				}
			} else {
				List<Sentence> phrases = SentenceUtils.findSubSentences(firstSentence, findPunctuation(firstSentence.getTokens()));
				Sentence lastPhrase = phrases.get(phrases.size() - 1);

				if (isNegative(lastPhrase) && (phrases.size() > 1 || sentences.size() > 1)) {
					return 1;
				}
			}
		}
		return 0;

	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}

	private List<Integer> findPunctuation(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(PUNCTUATION, tokens);
	}

}
