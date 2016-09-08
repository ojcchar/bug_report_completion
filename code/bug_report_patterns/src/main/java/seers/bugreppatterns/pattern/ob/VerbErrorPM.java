package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class VerbErrorPM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new ProblemInPM(), new ErrorNounPhrasePM() };
	final public static String[] VERB_TERMS = { "output", "return", "got", "get", "result"};

	final public static String[] NOT_VERBS = { "warning", "spam", "voided" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		//System.out.println("\n" + sentence);
		
		List<Token> tokens = sentence.getTokens();
		List<Integer> verbs = findAllVerbs(tokens);
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
						return 0;
					}

				}
			
				int end = start + 1;
				
				while (end <= tokens.size()) {
					Sentence subSentence = new Sentence(sentence.getId(), tokens.subList(start, end));
					if (isNegative(subSentence)) {
						return 1;
					}
					end++;
				}

			}
		}

		return 0;
	}

	private List<Integer> findAllVerbs(List<Token> tokens) {

		List<Integer> verbs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if ((token.getGeneralPos().equals("VB")
					|| Arrays.stream(VERB_TERMS).anyMatch(p -> token.getLemma().equals(p)))
					&& !Arrays.stream(NOT_VERBS).anyMatch(p -> token.getWord().equals(p))) {
				//if (i + 1 < tokens.size() && !tokens.get(i + 1).getGeneralPos().equals("VB"))
					verbs.add(i);
			}
		}
		return verbs;
	}

	private boolean tokenIsPrep(Token token) {
		return Arrays.stream(ProblemInPM.PREP_TERMS).anyMatch(p -> token.getWord().equalsIgnoreCase(p));
	}
	
	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}
}
