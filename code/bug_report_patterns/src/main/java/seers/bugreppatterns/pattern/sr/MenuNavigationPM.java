package seers.bugreppatterns.pattern.sr;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;

public class MenuNavigationPM extends StepsToReproducePatternMatcher {

	public final static Set<String> SEPARATORS = JavaUtils.getSet("->", "-", "|", "--", ">");

	public final static Set<String> GUI_TERMS = JavaUtils.getSet("window", "windows", "menu", "option", "preference",
			"tool", "setting", "checkbox", "list", "panel", "table", "project", "edit", "download", "section", "page",
			"homepage", "screen", "preferences", "preference");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		// separate the sentence into clauses, by any of the defined separators
		List<Sentence> clauses = SentenceUtils.extractClausesBySeparators(sentence, SEPARATORS);

		// -----------------------

		if (clauses.size() > 1) {
			int numClausesWithGuiTerms = 0;

			// check for gui terms
			for (Sentence clause : clauses) {
				if (SentenceUtils.sentenceContainsAnyLemmaIn(clause, GUI_TERMS)) {
					numClausesWithGuiTerms++;
				}
			}

			if (numClausesWithGuiTerms > 1) {
				return 1;
			}
		}

		return 0;

	}

}
