package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NoNounPM extends ObservedBehaviorPatternMatcher{

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		String text=TextProcessor.getStringFromLemmas(sentence);
		List<Token> tokens=sentence.getTokens();
		if(tokens.size()>1){
			if(text.startsWith("no ")||(text.startsWith("nothing"))){
				if(tokens.get(1).getGeneralPos().equals("NN")||(tokens.get(1).getGeneralPos().equals("VB"))){
					return 1;
				}
			}else if(text.startsWith("issue")||(text.startsWith("problem")||(text.startsWith("error")))){
				int match = isInner(tokens);
				if(match==1){
					return 1;
				}
			}else{
				int match = isInner(tokens);
				return match;
			}
		}

		return 0;
	}
	public int isInner(List<Token> tokens){
		int index=0;
		for (Token token : tokens) {
			if(token.getLemma().equals("no")||token.getLemma().equals("nothing")){
				if(tokens.get(index+1).getGeneralPos().equals("NN")||(tokens.get(index+1).getGeneralPos().equals("VB"))||(tokens.get(index+1).getGeneralPos().equals("RP"))){
					return 1;
				}
			}else if(token.getLemma().equals("and")&&(tokens.get(index+1).getLemma().equals("not"))){
				return 1;
			}
			index++;
		}
		return 0;
	}
}



