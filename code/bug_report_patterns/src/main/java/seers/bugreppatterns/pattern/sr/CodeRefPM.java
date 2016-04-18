package seers.bugreppatterns.pattern.sr;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class CodeRefPM extends StepsToReproducePatternMatcher{

	final static String[] NOUNS_TERM = {"snippet","code","sample","configuration","statement","script","html/ssi","html/fbml","html","trace",".htm"};
	final static String[] ADV_LOCATION = {"here","below"};
	final static String[] VERB_DEMO={"provide","enclose","follow","render","attach"};
	
	public int matchSentence(Sentence sentence) throws Exception {
		
		String text = TextProcessor.getStringFromLemmas(sentence);
		
		List<Token> tokens=sentence.getTokens();
		
		for(int i=0;i<tokens.size();i++){
			Token token=tokens.get(i);
			//exist a noun that refer to code such as code or snippet
			if (Arrays.stream(NOUNS_TERM).anyMatch(t -> token.getLemma().contains(t))){
				if(token.getGeneralPos().equalsIgnoreCase("NN")){
					return 1;
				}
			}
		}
		if(text.matches(".*(command line|live example|test case|file in).*")){
			return 1;
		}else{
			//return existLocation(tokens)?1:0;
			return 0;
		}
		
	}

	/*private boolean existLocation(List<Token> tokens) {
		boolean adverb=false;
		for(int i=0;i<tokens.size();i++){
			Token token = tokens.get(i);
			if (Arrays.stream(ADV_LOCATION).anyMatch(t -> token.getLemma().equals(t))){
				adverb=true;
			}
		}
		if(adverb){
			for(int i=0;i<tokens.size();i++){
				Token token=tokens.get(i);
				if(Arrays.stream(VERB_DEMO).anyMatch(t -> token.getLemma().equals(t))){
					return true;
				}
			}
		}

		return false;
	}
*/
}
