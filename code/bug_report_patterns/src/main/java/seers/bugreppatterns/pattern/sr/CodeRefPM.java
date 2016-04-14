package seers.bugreppatterns.pattern.sr;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class CodeRefPM extends StepsToReproducePatternMatcher{

	final static String[] NOUNS_TERM = {"snippet","code","sample","configuration","file","statement","example","script","line","command","case","test"};
	final static String[] ADV_LOCATION = {"here","below"};
	final static String[] VERB_DEMO={"provide","enclose","follow","render"};
	
	public int matchSentence(Sentence sentence) throws Exception {
		
		List<Token> tokens=sentence.getTokens();
		boolean b=existLocation(tokens);
		if(b){
			return 1;
		}
		for(int i=0;i<tokens.size();i++){
			Token token=tokens.get(i);
			if (Arrays.stream(NOUNS_TERM).anyMatch(t -> token.getLemma().equals(t))){
				if(token.getGeneralPos().equalsIgnoreCase("NN")){
					return 1;
				}
			}
		}
		return 0;
	}

	private boolean existLocation(List<Token> tokens) {
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

}
