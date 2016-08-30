package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class MenuNavigationPM extends StepsToReproducePatternMatcher {

	public final static String[] SEPARATORS = { "->", "-", "|", "--", ">" };

	public final static String[] GUI_TERMS = { "window", "menu", "option", "preference", "tool", "setting", "checkbox",
			"list", "panel", "table", "project", "edit", "download", "section", "page", "homepage", "screen" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		// separate the sentence into clauses, by any of the defined separators
		List<Token> tokens2 = sentence.getTokens();
		List<List<Token>> clauses = extractClauses(tokens2, SEPARATORS);

		// -----------------------

		if (clauses.size() > 1) {
			int numClausesWithGuiTerms = 0;

			// check for gui terms
			for (List<Token> elemTokens : clauses) {
				if (Arrays.stream(GUI_TERMS)
						.anyMatch(guiTerm -> elemTokens.stream().anyMatch(t -> t.getLemma().equalsIgnoreCase(guiTerm)
								|| t.getLemma().toLowerCase().startsWith(guiTerm)))) {
					numClausesWithGuiTerms++;
				}
			}

			if (numClausesWithGuiTerms > 1) {
				return 1;
			}
		}

		return 0;

	}

	public static List<List<Token>> extractClauses(List<Token> tokens2, String[] separators) {
		List<List<Token>> clauses = null;
		for (int i = 0; i < separators.length; i++) {
			String separator = separators[i];
			clauses = getElementsBySeparator(tokens2, separator);
			if (clauses.size() != 1) {
				break;
			}
		}
		return clauses;
	}

	public static List<List<Token>> getElementsBySeparator(List<Token> tokens, String separator) {

		List<List<Token>> clauses = new ArrayList<>();
		List<Token> clause = new ArrayList<>();
		for (int i = 0; i < tokens.size();) {
			Token token = tokens.get(i);

			String lemma = getLemmas(tokens, i, separator.length());
			if (lemma.equals(separator)) {
				if (!clause.isEmpty()) {
					clauses.add(clause);
				}

				clause = new ArrayList<>();
				i += separator.length();
			} else {
				clause.add(token);
				i++;
			}
		}
		if (!clause.isEmpty()) {
			clauses.add(clause);
		}

		return clauses;
	}

	private static String getLemmas(List<Token> tokens, int i, int length) {

		int toIndex = i + length;
		List<Token> subList;
		if (toIndex < tokens.size()) {
			subList = tokens.subList(i, toIndex);
		} else {
			subList = tokens.subList(i, tokens.size());
		}

		StringBuffer buf = new StringBuffer();
		for (Token token : subList) {
			buf.append(token.getLemma());
		}
		return buf.toString();
	}

}
