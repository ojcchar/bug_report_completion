package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ErrorTermSubjectPM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new ProblemInPM(), new ErrorNounPhrasePM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		List<Integer> verbIndexes = findVerbs(tokens);

		// If there is no verb, it can't be a S_OB_ERROR_AS_SUBJECT
		if (!verbIndexes.isEmpty()) {
			List<Sentence> subSentences = findSubSentences(sentence, verbIndexes);

			// the last subSentence doesn't matter
			for (int i = 0; i < subSentences.size() - 1; i++) {
				// System.out.println(subSentences.get(i));
				for (PatternMatcher pm : NEGATIVE_PMS) {
					int match = pm.matchSentence(subSentences.get(i));
					if (match == 1) {
						return 1;
					}
				}
			}
		}

		return 0;
	}

	public boolean compoundErrorTerms(List<Token> tokens) {
		for (int i = 0; i < (tokens.size() - 1); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("NN") && token.getLemma().equals("blank")
					&& tokens.get(i + 1).getGeneralPos().equals("NN") && tokens.get(i + 1).getLemma().equals("page")) {
				return true;
			}
		}
		return false;
	}

	public boolean NounsAsVerbs(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB") && (token.getLemma().equals("double"))) {
				return true;
			}
		}
		return false;

	}

	private List<Integer> findVerbs(List<Token> tokens) {

		List<Integer> verbs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);

			if (token.getGeneralPos().equals("VB")) {
				boolean add = true;
				// disregard verb if it starts the sentence
				if (i == 0) {
					add = false;
				} else
				// disregard verbs that come after preposition or determiner
				if (i - 1 >= 0) {
					Token prevToken = tokens.get(i - 1);
					if (Arrays.stream(ProblemInPM.PREP_TERMS).anyMatch(t -> prevToken.getWord().equalsIgnoreCase(t))
							|| prevToken.getGeneralPos().equals("DT")) {
						add = false;
					}

				}
				if (add) {
					verbs.add(i);
				}
			}
		}
		return verbs;
	}

}
