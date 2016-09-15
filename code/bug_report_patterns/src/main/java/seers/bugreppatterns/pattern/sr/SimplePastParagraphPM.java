package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SimplePastParagraphPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		int num = 0;

		List<Sentence> sentences = paragraph.getSentences();
		for (Sentence sentence : sentences) {

			// no bullets allowed
			List<Token> tokensNoBullet = LabeledListPM.getTokensNoBullet(sentence);
			if (!tokensNoBullet.isEmpty()) {
				continue;
			}

			num += countNumClausesInSimplePresent(sentence);

		}

		// more than 1 match?
		if (num > 1) {
			return 1;
		}

		return 0;
	}

	public static int countNumClausesInSimplePresent(Sentence sentence) {

		int num = 0;

		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);

		// find the first clause "i performed" or "i then performed"
		int idxFirstClause = findFirstSimplePastClause(clauses);

		// first clause found
		if (idxFirstClause != -1) {
			num++;

			// check the remaining clauses such as "deleted...", this is
			// done to match sentences such as "i performed..., deleted,
			// and create...."
			for (Sentence clause : clauses.subList(idxFirstClause + 1, clauses.size())) {
				if (checkSimplePastClause(clause)) {
					num++;
				}
			}
		} else {

			// check for sentence starting with the verb in past
			for (Sentence clause : clauses) {
				if (checkSimplePastClause(clause)) {
					num++;
				}
			}
		}
		return num;
	}

	private static boolean checkSimplePastClause(Sentence clause) {

		if (clause.getTokens().size() < 2) {
			return false;
		}

		Token token = clause.getTokens().get(0);

		// case: performed
		if (checkForVerb(token)) {
			return true;
		}
		return false;
	}

	private static int findFirstSimplePastClause(List<Sentence> clauses) {
		for (int i = 0; i < clauses.size(); i++) {
			Sentence sentence = clauses.get(i);

			List<Token> tokens = sentence.getTokens();
			List<Integer> verbs = findVerbsInPast(tokens);

			for (Integer verb : verbs) {

				if (verb - 1 >= 0) {

					// case: I performed
					Token prevToken = tokens.get(verb - 1);
					if (checkForPronoun(prevToken)) {
						return i;

						// case: I then tried
					} else if (prevToken.getGeneralPos().equals("RB")) {
						if (verb - 2 >= 0) {
							Token prevToken2 = tokens.get(verb - 2);
							if (checkForPronoun(prevToken2)) {
								return i;
							}
						}
					}
				}

			}
		}
		return -1;
	}

	private static boolean checkForPronoun(Token prevToken) {
		return prevToken.getLemma().equals("i") || prevToken.getGeneralPos().equals("PRP");
	}

	private static List<Integer> findVerbsInPast(List<Token> tokens) {

		List<Integer> idxs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (checkForVerb(token)) {
				idxs.add(i);
			}
		}
		return idxs;
	}

	private static boolean checkForVerb(Token token) {
		return token.getPos().equals("VBD") || token.getPos().equals("VBN") || token.getLemma().equals("set");
	}
}
