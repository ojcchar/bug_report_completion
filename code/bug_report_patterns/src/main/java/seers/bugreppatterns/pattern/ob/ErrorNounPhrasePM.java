package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ErrorNounPhrasePM extends ObservedBehaviorPatternMatcher{

	final String [] ADJ_ERROR_TERMS={"corrupt"};
	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		String text=TextProcessor.getStringFromLemmas(sentence);
		List<Token> tokens = sentence.getTokens();
		ArrayList<Integer> indexVerb = new ArrayList<Integer> ();
		boolean noVerb=true;
		for(int i=0;i<tokens.size();i++) {
			Token token = tokens.get(i);
			if(token.getGeneralPos().equals("VB")){
				noVerb=false;
				indexVerb.add(i);
			}
		}
		//find Noun_Phrase with error terms
		if(noVerb){
			for (Token token : tokens) {
				if (Arrays.stream(VerbErrorPM.ERROR_TERMS).anyMatch(t -> token.getLemma().contains(t))
					&& token.getGeneralPos().equals("NN")){
					return 1;
				}else if(Arrays.stream(ADJ_ERROR_TERMS).anyMatch(t -> token.getLemma().contains(t))
					&& token.getGeneralPos().equals("JJ")){
					return 1;
				}
			}
		//verify that is not an Error how sentence
		}else{
			for (int i=0;i<indexVerb.size();i++){
				if(tokens.get(indexVerb.get(i)).getLemma().equals("encode")||tokens.get(indexVerb.get(i)).getLemma().equals("build")||tokens.get(indexVerb.get(i)).getLemma().equals("httpd")){
					return 1;
				}
			}
/*			PatternMatcher pm = new ErrorHowPM();
			int match=pm.matchSentence(sentence);
			if(match==0){
				return 1;
			}*/
			return 0;
		}
		return 0;
	}

}
