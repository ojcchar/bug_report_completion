package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import edu.stanford.nlp.ling.tokensregex.SequenceMatchAction.NextMatchAction;
import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SeemsToNegativeVerbPM extends ObservedBehaviorPatternMatcher{
	
	public final static String [] NEG_VERB = {"affect", "break", "block", "bypass", "clear", "clobber", "close", "complain",
		"consume", "crash", "cut", "delete", "delay", "die", "disappear", "enforce", "erase", "exit", "expire", "fail",
		"flicker", "freeze", "forget", "glitch", "grow", "hang", "ignore", "increase", "interfere", "lose", "jerk",
		"jitter", "mishandle", "mute", "offset", "overlap", "pause", "prohibit", "reduce", "refuse", "remain", "reject", "rest",
		"restart", "revert", "skip", "stop", "stick", "suffer", "throw", "truncate", "vanishe", "terminate", "trim"};

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		int seemsIndex = seemsIndex(tokens);
		if (seemsIndex != -1
				&& tokens.get(seemsIndex + 1).getLemma().equals("to")) {
			// check that is not SeemsToBe
			PatternMatcher pm = new SeemsToBePM();
			int match = pm.matchSentence(sentence);
			if (match == 0) {
				if (tokens.get(seemsIndex + 2).getLemma().equals("not")
						&& tokens.get(seemsIndex + 3).getGeneralPos()
								.equals("VB")) {
					return 1;
				} else if (tokens.get(seemsIndex + 2).getLemma().equals("only")
						&& tokens.get(seemsIndex+3).getLemma().equals("not") && 
						tokens.get(seemsIndex + 4).getGeneralPos()
								.equals("VB")) {
					return 1;
					
				}else {
					Token tok = tokens.get(seemsIndex+2);
					if(tok.getLemma().equals("only")){
						Token nextToken = tokens.get(seemsIndex+3);
						if(nextToken.getGeneralPos().equals("VB")){
							if (Arrays.stream(NEG_VERB).anyMatch(t -> nextToken.getLemma().equals(t))) {
								return 1;
							} 
						} 
					}else{
						if(tok.getGeneralPos().equals("VB")){
							if (Arrays.stream(NEG_VERB).anyMatch(t -> tok.getLemma().equals(t))) {
								return 1;
							} 
						} else if (tok.getGeneralPos().equals("RB")) {
							if(Arrays.stream(NegativeAdjOrAdvPM.NEGATIVE_ADV).anyMatch(t -> tok.getLemma().equals(t))){
								return 1;
							}
						}
					}

				}
			}
			if(tokens.get(seemsIndex+2).getLemma().equals("get")){
				Token nextToken = tokens.get(seemsIndex+3);
				if(Arrays.stream(NEG_VERB).anyMatch(t-> nextToken.getLemma().equals(t))){
					return 1;
				}
			}
		} 
		return 0;
	}
	
	public int seemsIndex (List<Token> tokens){
		
		for(int i=0; i<tokens.size(); i++){
			if (tokens.get(i).getLemma().equals("seem") || tokens.get(i).getLemma().equals("appear")){
				return i;
			}
		}
		return -1;
	}

}
