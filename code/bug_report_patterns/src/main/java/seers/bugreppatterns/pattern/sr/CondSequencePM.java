package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.ob.ButNegativePM;
import seers.bugreppatterns.pattern.ob.ConditionalNegativePM;
import seers.bugreppatterns.pattern.ob.FrequencyAdverbPM;
import seers.bugreppatterns.pattern.ob.NegativeAdjOrAdvPM;
import seers.bugreppatterns.pattern.ob.NegativeAuxVerbPM;
import seers.bugreppatterns.pattern.ob.NegativeVerbPM;
import seers.bugreppatterns.pattern.ob.NoLongerPM;
import seers.bugreppatterns.pattern.ob.ProblemInPM;
import seers.bugreppatterns.pattern.ob.ThereIsNoPM;
import seers.bugreppatterns.pattern.ob.UnableToPM;
import seers.bugreppatterns.pattern.ob.VerbErrorPM;
import seers.bugreppatterns.pattern.ob.VerbNoPM;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class CondSequencePM extends StepsToReproducePatternMatcher{

	final static String[] CONDITIONAL_TERMS={"while","when","if","whenever"};
	
	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}
	
	@Override
	public int matchParagraph(Paragraph paragraph)throws Exception{
		
		PatternMatcher pm = new ConditionalNegativePM();
		
		List<Sentence> sentences = paragraph.getSentences();
		int condSentencesNumber=0;
		int negaux=0;
		
		if(sentences.size()>1){

			for (int i=0;i<(sentences.size()-1);i++){
				if(containsCondTerm(sentences.get(i))){
					//conditional no negative sentences
					int condTermNumber=(findConditionalTerms(sentences.get(i).getTokens()).size());
					if(condTermNumber<1){
						int match=pm.matchSentence(sentences.get(i));
						if((match==0)){
							condSentencesNumber++;
						}
					}else{
						condSentencesNumber++;
/*						Sentence s=sentences.get(i);
						int match2=pm2.matchSentence(s);
						int match3=pm3.matchSentence(s);
						if((match2==1)||(match3==1)){
							negaux++;
						}*/
						for (PatternMatcher pm2 : ButNegativePM.NEGATIVE_PMS) {
							int match = pm2.matchSentence(sentences.get(i));
							if (match == 1) {
								negaux++;
								//modify if there are others patterns thaat implements the following conditions
							}else if(TextProcessor.getStringFromLemmas(sentences.get(i)).contains("problem")||TextProcessor.getStringFromLemmas(sentences.get(i)).contains("error")||TextProcessor.getStringFromLemmas(sentences.get(i)).contains("even longer")){
								negaux++;
							}
						}
					}
				}else {
					for (PatternMatcher pm2 : ButNegativePM.NEGATIVE_PMS) {
						int match = pm2.matchSentence(sentences.get(i));
						if (match == 1) {
							negaux++;
						}else if(TextProcessor.getStringFromLemmas(sentences.get(i)).contains("problem")||TextProcessor.getStringFromLemmas(sentences.get(i)).contains("error")||TextProcessor.getStringFromLemmas(sentences.get(i)).contains("even longer")){
							negaux++;
						}
					}
					
				}
			}
			if(condSentencesNumber>0 && negaux>0){
				return 1;
			}
			/*if(condSentencesNumber>=1){
				//verify that the last sentences is a NEG_AUX_VERB sentence
				Sentence s = sentences.get((sentences.size()-1));
				pm = new NegativeAuxVerbPM();
				if(pm.matchSentence(s)==1){
					return 1;
				}
			}*/
		}
		return 0;
	}
	
	private boolean containsCondTerm(Sentence sentence){
		List<Token> tokens = sentence.getTokens();
		for(int i=0;i<tokens.size();i++){
			Token token = tokens.get(i);
			if (Arrays.stream(CONDITIONAL_TERMS).anyMatch(t -> t.equals(token.getLemma()))) {
				return true;
			}
		}
		return false;
	}
	
	private List<Integer> findConditionalTerms(List<Token> tokens) {

		List<Integer> conds = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (Arrays.stream(CONDITIONAL_TERMS).anyMatch(t -> t.equals(token.getLemma()))) {
				conds.add(i);
			}
		}
		return conds;
	}
}


