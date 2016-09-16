package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class VerbErrorPM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new ProblemInPM(), new ErrorNounPhrasePM() };
	final public static Set<String> VERB_TERMS = JavaUtils.getSet("output", "return", "got", "get", "result");

	final public static Set<String> NOT_VERBS = JavaUtils.getSet("warning", "spam", "voided");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		// System.out.println("\n" + sentence);

		for (Sentence subSentence : SimplePresentPM.findSubsentences(sentence)) {

			List<Token> tokens = subSentence.getTokens();
			List<Integer> verbs = findAllVerbs(tokens);
			
			verbloop:
			for (int verb = 0; verb < verbs.size(); verb++) {
				// there's something after the verb
				if (verbs.get(verb) + 1 < tokens.size()) {
					int start = verbs.get(verb) + 1;
					Token afterVerbToken = tokens.get(start);

					// the token after the verb is a preposition or a personal pronoun
					while (tokenIsPrep(afterVerbToken) || afterVerbToken.getGeneralPos().equals("PRP")) {
						start++;
						if (start < tokens.size()) {
							afterVerbToken = tokens.get(start);
						} else {
							break verbloop;
							//return 0;
						}

					}

					int end = start + 1;

					while (end <= tokens.size()) {
						Sentence clause = new Sentence(sentence.getId(), tokens.subList(start, end));
						if (isNegative(clause)) {
							return 1;
						}
						end++;
					}

				}
			}
		}

		return 0;
	}

	private List<Integer> findAllVerbs(List<Token> tokens) {

		List<Integer> verbs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if ((token.getGeneralPos().equals("VB") || SentenceUtils.lemmasContainToken(VERB_TERMS, token)
					&& !SentenceUtils.lemmasContainToken(NOT_VERBS, token))) {
				// if (i + 1 < tokens.size() && !tokens.get(i + 1).getGeneralPos().equals("VB"))
				verbs.add(i);
			}
		}
		return verbs;
	}

	private boolean tokenIsPrep(Token token) {
		return SentenceUtils.lemmasContainToken(ProblemInPM.PREP_TERMS, token);
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		if (tokens.size() > 1) {
			if (tokens.get(0).getLemma().equals("no")) {
				Sentence subSentence = new Sentence(sentence.getId(), tokens.subList(1, tokens.size()));
				if (sentenceMatchesAnyPatternIn(subSentence, NEGATIVE_PMS)) {
					return false;
				}
			}
		}
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}
}
