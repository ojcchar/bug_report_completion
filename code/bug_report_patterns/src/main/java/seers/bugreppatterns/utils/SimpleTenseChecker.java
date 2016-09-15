package seers.bugreppatterns.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SimpleTenseChecker {

	private Set<String> partOfSpeeches;
	private Set<String> undetectedVerbs;
	private Set<String> verbsToAvoid;

	public SimpleTenseChecker(Set<String> partOfSpeeches, Set<String> undetectedVerbs) {
		this(partOfSpeeches, undetectedVerbs, null);

	}

	public SimpleTenseChecker(Set<String> partOfSpeeches, Set<String> undetectedVerbs, Set<String> verbsToAvoid) {
		this.partOfSpeeches = partOfSpeeches == null ? new HashSet<>() : partOfSpeeches;
		this.undetectedVerbs = undetectedVerbs == null ? new HashSet<>() : undetectedVerbs;
		this.verbsToAvoid = verbsToAvoid == null ? new HashSet<>() : verbsToAvoid;
	}

	public int countNumClauses(Sentence sentence) {

		int num = 0;

		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);

		// find the first clause "i performed" or "i then performed"
		int idxFirstClause = findFirstClauseInTense(clauses);

		// first clause found
		if (idxFirstClause != -1) {
			num++;

			// check the remaining clauses such as "deleted...", this is
			// done to match sentences such as "i performed..., deleted,
			// and create...."
			List<Sentence> remainingClauses = clauses.subList(idxFirstClause + 1, clauses.size());
			for (Sentence clause : remainingClauses) {
				if (checkClauseInTense(clause)) {
					num++;
				}
			}
		} else {

			// check for sentence starting with the verb in past
			for (Sentence clause : clauses) {
				if (checkClauseInTense(clause)) {
					num++;
				}
			}
		}
		return num;
	}

	private boolean checkClauseInTense(Sentence clause) {

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

	private int findFirstClauseInTense(List<Sentence> clauses) {
		for (int i = 0; i < clauses.size(); i++) {
			Sentence sentence = clauses.get(i);

			List<Token> tokens = sentence.getTokens();
			List<Integer> verbs = findVerbsInTense(tokens);

			for (Integer verb : verbs) {

				if (verb - 1 >= 0) {

					// case: I performed
					Token prevToken = tokens.get(verb - 1);
					if (checkForSubject(prevToken)) {
						return i;

						// case: I then tried
					} else if (prevToken.getGeneralPos().equals("RB")) {
						if (verb - 2 >= 0) {
							Token prevToken2 = tokens.get(verb - 2);
							if (checkForSubject(prevToken2)) {
								return i;
							}
						}
					}
				}

			}
		}
		return -1;
	}

	private boolean checkForSubject(Token prevToken) {
		return prevToken.getGeneralPos().equals("PRP") || prevToken.getGeneralPos().equals("NN");
	}

	private List<Integer> findVerbsInTense(List<Token> tokens) {

		List<Integer> idxs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (checkForVerb(token)) {
				idxs.add(i);
			}
		}
		return idxs;
	}

	private boolean checkForVerb(Token token) {
		return (partOfSpeeches.stream().anyMatch(pos -> token.getPos().equals(pos))
				|| undetectedVerbs.stream().anyMatch(verb -> token.getLemma().equals(verb)))
				&& verbsToAvoid.stream().noneMatch(verb -> token.getLemma().equals(verb));
	}
}
