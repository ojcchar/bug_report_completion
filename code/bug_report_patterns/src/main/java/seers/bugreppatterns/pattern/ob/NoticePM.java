package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NoticePM extends ObservedBehaviorPatternMatcher {

	private final static Set<String> NOTICE_TERMS = JavaUtils.getSet("notice", "see");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> finds = findFinds(tokens);

		if (!finds.isEmpty()) {
			return 1;
		}

		return 0;
	}

	private List<Integer> findFinds(List<Token> tokens) {
		List<Integer> finds = new ArrayList<Integer>();

		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			// Look for every "notice"
			if (SentenceUtils.lemmasContainToken(NOTICE_TERMS, current)) {

				// The right "notice"
				if (i - 1 >= 0 && i < tokens.size() - 1) {
					Token previous = tokens.get(i - 1);

					// the one preceded by a pronoun
					if ((current.getPos().equals("VBD") || current.getPos().equals("VBP")
							|| current.getPos().equals("VBN"))
							&& (previous.getGeneralPos().equals("PRP")
									|| previous.getWord().toLowerCase().equals("i"))) {
						finds.add(i);
					}
					// the one preceded by another verb/modal that is preceded
					// by a pronoun
					else if ((previous.getGeneralPos().equals("VB")
							|| (previous.getGeneralPos().equals("MD") && !isEBModal(tokens.subList(i - 1, i))))
							&& i - 2 >= 0) {
						if (tokens.get(i - 2).getGeneralPos().equals("PRP")) {
							finds.add(i);
						}
					}
					// the one preceded by a conjunction, which is preceded at
					// some point by a pronoun
					else if (previous.getGeneralPos().equals("CC")) {
						for (int j = i - 2; j >= 0; j--) {
							if (tokens.get(j).getGeneralPos().equals("PRP")) {
								finds.add(i);
							}
						}
					}
					// the one starting a sentence
					else if (previous.getLemma().equals(",") || previous.getLemma().equals("-")) {
						finds.add(i);
					}
					// the one preceded by after
					else if (previous.getLemma().equals("after")) {
						finds.add(i);
					}
				}
				// the one starting a sentence
				else if (i == 0 && i < tokens.size() - 1) {
					finds.add(i);
				}

			}

		}
		return finds;
	}
}
