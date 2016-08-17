package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class PositiveConditionalPM extends ObservedBehaviorPatternMatcher{

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> conditionalTermPositions = ConditionalNegativePM
				.findConditionalTerms(tokens,
						ConditionalPositivePM.CONDITIONAL_TERMS);

		if (conditionalTermPositions != null
				&& conditionalTermPositions.size() > 0) {
			// there is a conditional expression now check that the first part
			// is not EB
			for (Integer ctp : conditionalTermPositions) {
				if (ctp > 0) {
					Sentence sentence2 = new Sentence(sentence.getId(),
							tokens.subList(0, ctp));
					List<Token> tok2 = sentence2.getTokens();
					if(!checkEBModal(tok2) && !checkNegative(sentence2)){
						return 1;
					}
				}
			}
		}
		return 0;
	}
	
	private boolean checkNegative (Sentence sentence) throws Exception{
		
		for(PatternMatcher pm : NegativeAfterPM.NEGATIVE_PMS){
			int match = pm.matchSentence(sentence);
			if(match == 1){
				return true;
			}
		}
		return false;
	}
	
	private boolean checkEBModal(List<Token> tokens){
		for (int i = 0; i < tokens.size(); i++) {
			if ((tokens.get(i).getLemma().equals("must")
					|| tokens.get(i).getLemma().equals("need") || tokens
					.get(i).getLemma().equals("should"))) {
				//check that it is not NEGATIVE 
				return true;
			}
		}
		return false;
	}

}

