package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class OutputPM extends ObservedBehaviorPatternMatcher{

	public final static String[] OUTPUT_NOUN_TERMS = {"result", "output"};
	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		if(checkForOutputIs(tokens)){
			PatternMatcher pm = new OutputVerbPM();
			int match = pm.matchSentence(sentence);
			if(match == 0){
				return 1;
			}
		}else if (checkBeOutput(sentence)){
			return 1;
		}
		return 0;
	}
	
	public boolean checkForOutputIs(List<Token> tokens) {

		for (int i = 0; i < (tokens.size() - 1); i++) {
			Token actual = tokens.get(i);
			Token after = tokens.get(i + 1);
			if (Arrays.stream(OUTPUT_NOUN_TERMS).anyMatch(
					t -> actual.getLemma().equals(t))
					&& actual.getGeneralPos().equals("NN")
					&& (after.getPos().equals("VBD") || after.getPos().equals("VBZ") || after.getPos().equals("VBP"))){
					//&& after.getLemma().equals("be")) {
					
				return true;
			}
		}
		return false;
	}
	
	public boolean checkBeOutput(Sentence sentence) {
		List<Token> tokens = sentence.getTokens();
		for (int i = 0; i < (tokens.size() - 1); i++) {
			Token actual = tokens.get(i);
			if (actual.getGeneralPos().equals("VB")
					&& (actual.getWord().equals("is") || actual.getWord()
							.equals("are"))) {
				Sentence sentence2 = new Sentence(sentence.getId(),
						tokens.subList(i + 1, tokens.size()));
				List<Token> tok2 = sentence2.getTokens();
				if (tok2.size() > 1) {
					Token first = tok2.get(0);
					Token second = tok2.get(1);
					if (!first.getGeneralPos().equals("VB")) {
						if (Arrays.stream(OUTPUT_NOUN_TERMS).anyMatch(
								t -> first.getLemma().equals(t))) {
							return true;
						} else if (!second.getGeneralPos().equals("VB")
								&& Arrays.stream(OUTPUT_NOUN_TERMS).anyMatch(
										t -> second.getLemma().equals(t)) && !first.getWord().equals("not")) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
