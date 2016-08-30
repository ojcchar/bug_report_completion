package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class AttachRefPM extends ObservedBehaviorPatternMatcher {

	private static String ATTACH_VERB = "attach";
	private static String ATTACH_NOUN = "attachment";
	private static String[] ATTACHMENTS = { "screenshot", "screen", "log", "report", "document", "file",
			"sample", "example", "docx", "test", "project" };

	public final static PatternMatcher[] NEGATIVE_PMS = { new ErrorNounPhrasePM(), new NegativeAdjOrAdvPM(),
			new NoNounPM(), new ErrorTermSubjectPM(), new InsteadOfOBPM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		// System.out.println(sentence);
		// System.out.println(TextProcessor.getStringFromTerms(sentence));
		if (containsAttachment(sentence.getTokens()) && isNegative(sentence)) {
			return 1;
		}
		return 0;
	}

	private boolean containsAttachment(List<Token> tokens) {
		boolean attachTerm = false;
		boolean attachedElement = false;

		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			if ((current.getGeneralPos().equals("NN") && current.getLemma().equals(ATTACH_NOUN))
					|| (current.getGeneralPos().equals("VB") && current.getLemma().equals(ATTACH_VERB))) {

				if (i > 0) {
					Token previous = tokens.get(i - 1);
					if (!previous.getGeneralPos().equals("IN") && !previous.getGeneralPos().equals("TO")) {
						attachTerm = true;
					}
				} else {
					attachTerm = true;
				}
			} else if (current.getGeneralPos().equals("NN")
					&& Arrays.stream(ATTACHMENTS).anyMatch(t -> current.getLemma().matches("[A-Za-z]*[/.]?" + t + "[/.]?[A-Za-z]*"))) {
				attachedElement = true;
			}

		}

		return attachTerm && attachedElement;
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