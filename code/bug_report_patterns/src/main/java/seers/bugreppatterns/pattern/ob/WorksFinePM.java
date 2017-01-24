package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class WorksFinePM extends ObservedBehaviorPatternMatcher {

	private final static Set<String> WORK_TERMS = JavaUtils.getSet("work", "suceed", "be", "function");
	private final static Set<String> FINE_TERMS = JavaUtils.getSet("correctly", "expect", "fine", "flawlessly", "good",
			"great", "normally", "ok", "perfectly", "properly", "reliably", "well");

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
					&& SentenceUtils.lemmasContainToken(WORK_TERMS, current)) {

				if (i - 1 >= 0) {
					Token previous = tokens.get(i - 1);
					// The one preceded by a "do"
					// case: "it does work..."
					if (previous.getGeneralPos().equals("VB") && previous.getLemma().equals("do")) {
						finds.add(i);
						break;
					} else
					// the one that is not preceded by a "not"
					// ignore cases like: "it does not work"
					if (previous.getLemma().equals("not")) {
						break;
					}
				}

				if (i + 1 < tokens.size()) {
					Token next = tokens.get(i + 1);
					// The one that precedes a "fine" term
					// case: "it works fine"
					if (SentenceUtils.lemmasContainToken(FINE_TERMS, next)) {
						finds.add(i);
					} else
					// case: "it works just fine"
					if (next.getGeneralPos().equals("RB") && i + 2 < tokens.size()
							&& SentenceUtils.lemmasContainToken(FINE_TERMS, tokens.get(i + 2))) {
						finds.add(i);

					}
					// The one that precedes an "as [fine term]"
					// case: "work as expected"
					else if (next.getLemma().equals("as") && i + 2 < tokens.size()) {
						Token next2 = tokens.get(i + 2);
						if (SentenceUtils.lemmasContainToken(FINE_TERMS, next2)) {
							finds.add(i);
						}
					}
					// The one that precedes a preposition
					// case: "it works to something"
					else if (next.getGeneralPos().equals("TO")
					// || next.getGeneralPos().equals("IN")
					) {
						finds.add(i);
					}
					// The one that ends a sentence
					// case: "it works!"
					else if (next.getLemma().matches("[^A-Za-z ]{1}")) {
						finds.add(i);
					}
				} else
				// case: "it works"
				if (i == tokens.size() - 1) {
					finds.add(i);
				}

			}

		}
		return finds;
	}
}
