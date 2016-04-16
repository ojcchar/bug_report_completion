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

	final public static String[] ERROR_TERMS = { "error", "warning", "bug", "violation", "exception", "problem", "npe",
			"ise", "fault", "404", "issue", "typo", "crash", "glitch", "failure", "errore", "truncation", "loss",
			"collision", "leak" , "conflict"};

	final public static String[] VERB_TERMS = { "throw", "cause", "produce", "give", "receive", "generate", "get",
			"got", "notice", "lead", "find", "return", "have", "consider", "respond", "authenticate", "be", "fail",
			"observe", "terminate", "show", "experience" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> verbs = findVerbs(tokens);
		for (Integer verb : verbs) {

			for (int i = verb + 1; i <= verb + 7 && i < tokens.size(); i++) {
				Token nextToken = tokens.get(i);
				boolean anyMatch = Arrays.stream(ERROR_TERMS)
						.anyMatch(p -> nextToken.getLemma().contains(p) || nextToken.getWord().contains(p));
				if (anyMatch) {
					return 1;
				}
				// else {
				// Token nextToken2 = tokens.get(i + 1);
				// }
			}

		}
		// return processSentence(sentence);

		// boolean anyMatch = tokens.stream().anyMatch(t ->
		// Arrays.stream(ERROR_TERMS)
		// .anyMatch(p -> t.getGeneralPos().equals("NN") &&
		// t.getLemma().contains(p)));
		// if (anyMatch) {
		// return 1;
		// }

		return 0;
	}

	private List<Integer> findVerbs(List<Token> tokens) {

		List<Integer> verbs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (Arrays.stream(VERB_TERMS).anyMatch(p -> token.getLemma().contains(p))) {
				verbs.add(i);
			}
		}
		return verbs;
	}

	@SuppressWarnings("unused")
	private int processSentence(Sentence sentence) {
		String sentenceTxt = TextProcessor.getStringFromTerms(sentence);

		List<Sentence> sentences = TextProcessor.processTextFullPipeline(sentenceTxt);

		SemanticGraph dependencies = sentences.get(0).getDependencies();

		Iterable<SemanticGraphEdge> edgeIterable = dependencies.edgeIterable();
		for (SemanticGraphEdge edge : edgeIterable) {
			String shortName = edge.getRelation().getShortName();
			if (shortName.equals("dobj") || shortName.equals("nsubj")) {
				IndexedWord source = dependencies.getNodeByIndex(edge.getSource().index());
				Token sourceToken = TextProcessor.parseToken(source.backingLabel());
				boolean anyMatch = Arrays.stream(VERB_TERMS)
						.anyMatch(p -> sourceToken.getGeneralPos().equals("VB") && sourceToken.getLemma().contains(p));
				if (anyMatch) {
					IndexedWord target = dependencies.getNodeByIndex(edge.getTarget().index());
					Token targetToken = TextProcessor.parseToken(target.backingLabel());
					anyMatch = Arrays.stream(ERROR_TERMS).anyMatch(
							p -> targetToken.getGeneralPos().equals("NN") && targetToken.getLemma().contains(p));
					if (anyMatch) {
						return 1;
					}

				}
			}
		}
		return 0;
	}

}
