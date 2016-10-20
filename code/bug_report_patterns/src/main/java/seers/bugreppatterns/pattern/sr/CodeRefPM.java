package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class CodeRefPM extends StepsToReproducePatternMatcher {

	final static Set<String> NOUNS_TERM = JavaUtils.getSet("attachment", "build/install", "call", "class", "code",
			"command", "config", "configuration", "configure", "container", "dockerfile", 
			"env",
			"example",
			"example/test", "excerpt", "file", "function", "hql", "html", "html/fbml", "html/ssi", "htpasswd", "image",
			"installer", "line", "method", "model", "parameter", "program", "project", "query", "sample", "screenshot",
			"screen-shot", "script", "snippet", "statement", "test", "test case", "test.htm", "tool", "trace", "video",
			"xml");

	final static String NOUN_TERMS_REGEX;
	static {
		StringBuffer buff = new StringBuffer("(");
		NOUNS_TERM.forEach(nt -> {
			buff.append(nt);
			buff.append("|");
		});
		if (buff.length() != 0) {
			buff.delete(buff.length() - 1, buff.length());
		}
		buff.append(")");
		NOUN_TERMS_REGEX = buff.toString();
	}

	final static Set<String> LOCATION_TERMS = JavaUtils.getSet("here", "below", "above", "next");

	final static Set<String> VERB_AS_ADJS = JavaUtils.getSet("provide", "enclose", "join", "attach", "upload",
			"include");
	final static Set<String> ADJS = JavaUtils.getSet("provided", "enclosed", "joined", "attached", "uploaded",
			"included");

	public int matchSentence(Sentence sentence) throws Exception {

		// remove label: "created attachment 34535"
		List<Token> tokensNoLabel = getTokensNoLabel(sentence.getTokens());

		String text = TextProcessor.getStringFromLemmas(new Sentence(sentence.getId(), tokensNoLabel));

		// -----------------------------------

		// cases: this/here is the snippet
		String regex = "(?s).*(this|here) be [a-zA-Z]+ (\\p{Punct}+ )?" + NOUN_TERMS_REGEX + ".*";
		if (text.matches(regex)) {
			return 1;
		}

		// -----------------------------------

		int nounIdx = -1;
		boolean isLocationTerm = false;
		boolean isAdjective = false;
		boolean endsWith = false;
		boolean nextColon = false;
		boolean isFollowingNoun = false;

		// check for some common sentence ends: ":", "e.g.", "like:", etc
		if (text.endsWith(":") || text.endsWith("e.g.")
				|| text.matches("(?s).*(,) (for example|as|like|like this|here) :$")) {
			endsWith = true;
		}

		for (int i = 0; i < tokensNoLabel.size(); i++) {
			Token token = tokensNoLabel.get(i);

			// there is a noun that refer to code, files, etc.
			if (NOUNS_TERM.stream().anyMatch(t -> token.getLemma().equalsIgnoreCase(t))) {
				if (token.getGeneralPos().equals("NN")) {
					nounIdx = i;
					if (i + 1 < tokensNoLabel.size()) {

						// case: "code:"
						if (tokensNoLabel.get(i + 1).getLemma().equals(":")) {
							nextColon = true;
						} else {
							// case "code is"
							if (i + 2 < tokensNoLabel.size()) {
								if (tokensNoLabel.get(i + 1).getGeneralPos().equals("VB")
										&& tokensNoLabel.get(i + 1).getLemma().equals("be")
										&& tokensNoLabel.get(i + 2).getLemma().equals(":")) {
									nextColon = true;
								}
							}
						}
					}
				}

				// check for location terms
			} else if (LOCATION_TERMS.stream().anyMatch(t -> token.getLemma().equalsIgnoreCase(t))
					&& ((token.getGeneralPos().equals("RB")) || token.getGeneralPos().equals("IN")
							|| token.getGeneralPos().equals("JJ"))) {
				isLocationTerm = true;

				// check for adjectives expressing attachment
			} else if ((VERB_AS_ADJS.stream().anyMatch(t -> token.getLemma().equalsIgnoreCase(t))
					&& token.getPos().equals("VBN"))
					|| (token.getLemma().equals("follow") && token.getPos().equals("VBG"))
					|| (ADJS.stream().anyMatch(t -> token.getLemma().equalsIgnoreCase(t))
							&& token.getGeneralPos().equals("JJ"))) {
				isAdjective = true;

				// check for "following"
			} else if (token.getLemma().equals("following")) {
				isFollowingNoun = true;
			}

			// if there is at least a code term and (location term, adjective,
			// it's followed by a colon, "following")
			if (nounIdx != -1 && (isLocationTerm || isAdjective || endsWith || nextColon || isFollowingNoun)) {
				return 1;
			}
		}

		// -------------------------

		// check for just the noun, no verb
		// case: "Excel file with macro
		if (nounIdx != -1 && !tokensNoLabel.stream().anyMatch(t -> t.getGeneralPos().equals("VB"))) {
			return 1;

		} else
		// case allowed: "validated file"
		if (nounIdx != -1 && nounIdx - 1 >= 0) {
			if (tokensNoLabel.get(nounIdx - 1).getPos().equals("VBN")) {
				boolean otherTokensOk = true;
				for (int i = 0; i < tokensNoLabel.size(); i++) {
					if (nounIdx != i && nounIdx - 1 != i) {
						Token t = tokensNoLabel.get(i);
						if (t.getGeneralPos().equals("VB")) {
							otherTokensOk = false;
							break;
						}
					}
				}
				if (otherTokensOk) {
					return 1;
				}
			}
		}

		// -------------------------------

		// cases: ... the code shows: ...
		if (nounIdx != -1 && text.matches(".* " + tokensNoLabel.get(nounIdx).getLemma() + " .* : .*")) {
			return 1;
		}

		// -------------------------

		return 0;

	}

	private List<Token> getTokensNoLabel(List<Token> tokens) {
		List<Token> tokensNoLabel = new ArrayList<>();

		// remove label
		for (int i = 0; i < tokens.size();) {

			Token token = tokens.get(i);
			if (token.getLemma().equals("create") && token.getPos().equals("VBN")) {
				if (i + 2 < tokens.size()) {
					if (tokens.get(i + 1).getLemma().equals("attachment")
							&& tokens.get(i + 2).getLemma().matches("\\d+")) {
						i += 3;
						continue;
					}

				}
			}

			tokensNoLabel.add(token);
			i++;
		}
		return tokensNoLabel;
	}

}
