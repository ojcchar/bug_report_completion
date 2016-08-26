package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class FoundPM extends ObservedBehaviorPatternMatcher {

	private final static String[] FIND_TERMS = { "find", "discover" };

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

			// Look for every "find"
			if (current.getGeneralPos().equals("VB")
					&& Arrays.stream(FIND_TERMS).anyMatch(t -> current.getLemma().equals(t))) {
				if (i - 1 >= 0) {
					Token previous = tokens.get(i - 1);
					if ((current.getPos().equals("VBD") || current.getPos().equals("VBP")
							|| current.getPos().equals("VBN"))
							&& (previous.getGeneralPos().equals("PRP")
									|| previous.getWord().toLowerCase().equals("i"))) {
						finds.add(i);
					} else if (previous.getGeneralPos().equals("VB") && i - 2 >= 0) {
						if (tokens.get(i - 2).getGeneralPos().equals("PRP")) {
							finds.add(i);
						}
					} else if (previous.getGeneralPos().equals("CC")) {
						for (int j = i - 2; j >= 0; j--) {
							if (tokens.get(j).getGeneralPos().equals("PRP")) {
								finds.add(i);
							}
						}
					}
				}

			}

		}
		return finds;
	}
}