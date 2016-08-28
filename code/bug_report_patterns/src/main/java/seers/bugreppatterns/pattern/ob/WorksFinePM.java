package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class WorksFinePM extends ObservedBehaviorPatternMatcher {

	private final static String[] WORK_TERMS = { "work", "suceed", "be", "function" };
	private final static String[] FINE_TERMS = { "correctly", "expect", "fine", "flawlessly", "good", "great",
			"normally", "ok", "perfectly", "properly", "reliably", "well" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> works = findWorks(tokens);

		if (!works.isEmpty()) {
			return 1;
		}

		return 0;
	}

	private List<Integer> findWorks(List<Token> tokens) {
		List<Integer> finds = new ArrayList<Integer>();

		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			// Look for every "work"
			if ((current.getGeneralPos().equals("VB") || current.getGeneralPos().equals("NN"))
					&& Arrays.stream(WORK_TERMS).anyMatch(t -> current.getLemma().equals(t))) {

				if (i - 1 >= 0) {
					Token previous = tokens.get(i - 1);
					// The one preceded by a "do"
					if (previous.getGeneralPos().equals("VB") && previous.getLemma().equals("do")) {
						finds.add(i);
						break;
					}
					// the one that is not preceded by a "not"
					else if (previous.getLemma().equals("not")) {
						break;
					}
				}

				if (i + 1 < tokens.size()) {
					Token next = tokens.get(i + 1);
					// The one that precedes a "fine" term
					if (Arrays.stream(FINE_TERMS).anyMatch(t -> next.getLemma().equals(t))) {
						finds.add(i);
					}
					// The one that precedes an "as [fine term]"
					else if (next.getLemma().equals("as") && i + 2 < tokens.size()) {
						Token next2 = tokens.get(i + 2);
						if (Arrays.stream(FINE_TERMS).anyMatch(t -> next2.getLemma().equals(t))) {
							finds.add(i);
						}
					} // The one that precedes a preposition
					else if (next.getGeneralPos().equals("TO") || next.getGeneralPos().equals("IN")) {
						finds.add(i);
					}
					// The one that ends a sentence
					else if (next.getLemma().matches("[^A-Za-z ]{1}")) {
						finds.add(i);
					}
				} else if (i == tokens.size() - 1) {
					finds.add(i);
				}

			}

		}
		return finds;
	}
}
