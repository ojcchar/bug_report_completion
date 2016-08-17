package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class VerbErrorPM extends ObservedBehaviorPatternMatcher {

	final public static String[] VERB_TERMS = { "output", "return", "got", "get" };

	final public static String[] NOT_VERBS = { "warning", "spam", "voided" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> verbs = findAllVerbs(tokens);
		for (int verb = 0; verb < verbs.size(); verb++) {
			int i = verbs.get(verb) + 1;
			int j = verb == verbs.size() - 1 ? tokens.size() : verbs.get(verb + 1);
			// System.out.println(tokens.get(i - 1).getLemma());

			while (i < j) {
				Token nextToken = tokens.get(i);
				// System.out.println("\t" + nextToken.getLemma());
				boolean anyMatch = (ErrorNounPhrasePM
						.checkErrorNounPhrase(tokens.subList(i, i+1)) == 1) ? true : false;

				if (anyMatch) {
					return 1;
				}
				i++;
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
				verbs.add(i);
			}
		}
		return verbs;
	}

}
