package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class EvenIfPM extends ObservedBehaviorPatternMatcher{

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		String text = TextProcessor.getStringFromLemmas(sentence);
		List<Token> tokens = sentence.getTokens();
		int index=adverbIndex(tokens);
		if(index>0){
			List<Token> t1=tokens.subList(0, index);
			List<Token> t2=tokens.subList(index, tokens.size());
			boolean first=false;
			for (Token token : t1) {
				if(token.getGeneralPos().equals("VB")){
					first=true;
				}
			}
			if(!first){
				for (Token token : t2) {
					if(token.getGeneralPos().equals("VB")){
						first=true;
					}
				}
			}else{
				return 1;
			}
		}
		else if(index==0){
			for (Token token : tokens) {
				if(token.getGeneralPos().equals("VB")){
					return 1;
				}
			}
		}

		return 0;
	}
	
	private int adverbIndex (List<Token> tokens){
		for(int i=0;i<tokens.size();i++){
			Token t=tokens.get(i);
			if(tokens.get(i).getLemma().equals("although")){
				return i;
			}else if(tokens.get(i).getLemma().equals("even")&&(tokens.get(i+1).getLemma().equals("if")||tokens.get(i+1).getLemma().equals("though"))){
				return i;
			}
		}
		return -1;
	}

}
