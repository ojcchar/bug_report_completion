package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class UntilNegPM extends ObservedBehaviorPatternMatcher{

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> untilIndex = findUntil(tokens);
		if(!untilIndex.isEmpty()){
			for (Integer until : untilIndex) {
				//check a verb in the clause and a negative sentence in the last part
				Sentence clause = new Sentence(sentence.getId(), tokens.subList(0, until));
				Sentence after = new Sentence (sentence.getId(), tokens.subList(until+1, tokens.size()));
				List<Token> clause_tokens = clause.getTokens();
				if(isAClause(clause_tokens) && !isEBModal(clause_tokens)){
					if (isNegative(after)) {
						return 1;
					}
				}
			}
		}
		return 0;
	}
	
	public boolean isAClause (List<Token> tokens){
		for (Token token : tokens) {
			if(token.getGeneralPos().equals("VB")){
				return true;
			}
		}
		return false;
	}
	
	public List<Integer> findUntil (List<Token> tokens){
		List<Integer> termsIndex = new ArrayList<Integer>();
		for(int i=0; i<tokens.size(); i++){
			if(tokens.get(i).getLemma().equals("until")){
				termsIndex.add(i);
			}
		}
		return termsIndex;
	}
	
	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, ButNegativePM.NEGATIVE_PMS);
	}

}
