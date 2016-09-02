package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class BecomesPM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new ThereIsNoPM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new NoNounPM(), new ErrorTermSubjectPM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		List<Sentence> sentences = paragraph.getSentences();

		if (!sentences.isEmpty()) {
			Sentence firstSentence = sentences.get(0);

			String text = TextProcessor.getStringFromLemmas(firstSentence);

			// ----------------
			if (text.matches(".*[^A-Za-z]?become[^A-Za-z]?.*:") && !isNegative(firstSentence)) {
				return 1;
			}
		}
		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		for (PatternMatcher pm : NEGATIVE_PMS) {
			int match = pm.matchSentence(sentence);
			if (match == 1) {
				return true;
			}
		}
		return false;
	}
}
