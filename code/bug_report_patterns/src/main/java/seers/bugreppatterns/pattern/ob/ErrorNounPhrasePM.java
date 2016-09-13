package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ErrorNounPhrasePM extends ObservedBehaviorPatternMatcher {

	public final static Set<String> FALSE_VERBS = JavaUtils.getSet( "build", "httpd", "stack" );

	private final static Set<String> SUBJECTS = JavaUtils.getSet( "NN", "VB", "DT", "RB", "EX", "IN" );

	private static final Set<String> PUNCTUATION = JavaUtils.getSet( ":", ".", ";", "-LRB-", "-RRB-", "``", "''" );

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		// System.out.println(sentence);
		List<Token> tokens = sentence.getTokens();

		// divide sentence by punctuation
		List<Integer> punctuation = findPunctuation(tokens);
		List<Sentence> subSentences = findSubSentences(sentence, punctuation);

		boolean match = false;

		for (Iterator<Sentence> iterator = subSentences.iterator(); iterator.hasNext();) {
			Sentence subSentence = iterator.next();

			// divide sentence by prepositions
			List<Integer> prepositions = findPrepositions(subSentence.getTokens());
			List<Sentence> phrases = findSubSentences(subSentence, prepositions);

			// there's only one phrase (i.e., no preps)
			if (phrases.size() == 1) {
				match = match || matchSubSentence(phrases.get(0)) == 1;
			} else {

				for (Iterator<Sentence> iterator2 = phrases.iterator(); iterator2.hasNext();) {
					Sentence phrase = (Sentence) iterator2.next();

					// the S_OB_PROBLEM_IN case
					if (matchSubSentence(phrase) == 1) {
						if (iterator2.hasNext()) {
							return 0;
						} else {
							match = match || true;
						}
					} else {
						match = match || false;
					}

				}
			}

		}

		if (match)
			return 1;
		return 0;
	}

	private int matchSubSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		ArrayList<Integer> verbIndex = new ArrayList<Integer>();

		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")) {
				if (token.getLemma().equals("stack") && i + 1 < tokens.size()
						&& tokens.get(i + 1).getLemma().equals("trace")) {
					continue;
				}
				verbIndex.add(i);
			}
		}
		// find Noun_Phrase with error terms
		if (verbIndex.isEmpty()) {
			if (checkErrorNounPhrase(tokens) == 1) {
				return 1;
			}
			// verify that is not an Error how sentence
		} else {
			boolean matches = true;
			for (int i = 0; i < verbIndex.size(); i++) {

				Token token = tokens.get(verbIndex.get(i));

				if (token.getLemma().equals("be") || token.getPos().equals("VBP") || token.getPos().equals("VBZ")) {
					matches = false;
				} else if (verbIndex.get(i) == 0) {
					matches = matches && true;
				} else {
					Token previousToken = tokens.get(verbIndex.get(i) - 1);
					if ((token.getPos().equals("VBG") || token.getPos().equals("VBN"))
							&& !previousToken.getGeneralPos().equals("VB")) {
						matches = matches && true;
					} else {
						if (SentenceUtils.lemmasContainToken(SUBJECTS, previousToken)) {
							matches = matches && false;
						}
					}
				}
				if (SentenceUtils.lemmasContainToken(FALSE_VERBS, token)) {
					matches = matches || true;
				}
			}
			if (matches) {
				return checkErrorNounPhrase(tokens);
			}
		}
		return 0;
	}

	private List<Integer> findPunctuation(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(PUNCTUATION, tokens);
	}

	private List<Integer> findPrepositions(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(ProblemInPM.PREP_TERMS, tokens);
	}

	public static int checkErrorNounPhrase(List<Token> tokens) {
		// System.out.println(new Sentence(OB, tokens));
		int i = 0;

		for (Token token : tokens) {
			// System.out.println("checking: " +token);
			if (SentenceUtils.lemmasContainToken(NegativeTerms.NOUNS, token) && (token.getGeneralPos().equals("NN")
					|| token.getGeneralPos().equals("VB") || token.getGeneralPos().equals("CD"))) {
				return 1;
			} else if (token.getLemma().matches("([A-Za-z0-9.]+)(exception)") && token.getGeneralPos().equals("NN")) {
				return 1;
			} else if (token.getLemma().matches("([A-Za-z0-9.]+)(error)") && token.getGeneralPos().equals("NN")) {
				return 1;
			} else if (token.getLemma().matches("(illegal)([A-Za-z0-9.]+)") && token.getGeneralPos().equals("NN")) {
				return 1;
			} else if (SentenceUtils.lemmasContainToken(NegativeTerms.ADJECTIVES, token)
					&& (token.getGeneralPos().equals("JJ") || token.getGeneralPos().equals("NN")
							|| token.getGeneralPos().equals("RB") || token.getGeneralPos().equals("VB"))) {
				if (tokens.size() > 1) {
					return 1;
				}

			} else if (token.getLemma().equalsIgnoreCase("stack") && i + 1 < tokens.size()
					&& tokens.get(i + 1).getLemma().equalsIgnoreCase("trace")) {
				return 1;
			}

			i++;
		}
		// System.out.println("no ENP");
		return 0;
	}

}
