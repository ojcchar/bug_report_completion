package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SimplePresentSubordinatesPM extends StepsToReproducePatternMatcher{

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		int index=0;
		int count =0;
		int useful = 0;
		if(tokens.size()>1){
			while(index<(tokens.size()-1)){
				Token firsttoken = tokens.get(index);
				Token secondtoken = tokens.get(index+1);
				//present continuous
				if(firsttoken.getGeneralPos().equals("VB")&& secondtoken.getGeneralPos().equals("VB")&&((firsttoken.getPos().equals("VBP")||firsttoken.getPos().equals("VBZ")||firsttoken.getPos().equals("VB"))&&secondtoken.getPos().equals("VBG"))){
					index = index +2;
					count++;
					useful++;
				} //tenses not of our interest
				else if(firsttoken.getGeneralPos().equals("VB")&&secondtoken.getGeneralPos().equals("VB")){
					index++;
					count++;
					//simple present
				} else if ((firsttoken.getPos().equals("VBP")||firsttoken.getPos().equals("VBZ")||(firsttoken.getPos().equals("VB")))&& (!secondtoken.getGeneralPos().equals("VB"))){
					index++;
					count++;
					useful++;
				}else if(firsttoken.getGeneralPos().equals("VB")){
					count++;
					index++;
				}else{
					index++;
				}
			}
		}
		if(useful==count){
			return 1;
		}
		return 0;
	}
	
	public boolean countVerb(List<Token> tokens){
		int count = 0;
		int simplePresent = 0;
		for(int i=0;i<tokens.size();i++){
			Token token = tokens.get(i);
			if(token.getGeneralPos().equals("VB")){
				if(token.getPos().equals("VBP")||token.getPos().equals("VBZ")){
					
				}
				if(!(token.getPos().equals("VBD"))&&(!(token.getPos().equals("VBN")))&&(!(token.getPos().equals("VBG")))){
					simplePresent++;
				}
			}
		}
		if(count==simplePresent){
			return true;
		}
		return false;
	}
	
}
