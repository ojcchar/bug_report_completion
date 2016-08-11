package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ErrorNounPhrasePM extends ObservedBehaviorPatternMatcher {

	public final static String[] ADJ_ERROR_TERMS = { "abnormal", "aggressive",
			"annoying", "bad", "blank", "blocked", "broken", "corrupt",
			"cpu-bound", "dead", "different", "dirty", "down", "duplicate",
			"duplicated", "empty", "enormous", "erroneous", "expensive",
			"extra", "funny-looking", "gone", "hard", "ignored", "inaccesible",
			"inappropriate", "incorrect", "inconsistent", "incredible",
			"inefficient", "infinite", "lost", "insecure", "invalid",
			"meaningless", "misleading", "misplaced", "missing", "multiple",
			"off", "off-center", "old", "orphaned", "out-of-date", "outdated",
			"painful", "poor", "random", "reduced", "redundant", "repeated",
			"reversed", "runaway", "semi", "silly", "slow", "spurious",
			"strange", "stripped", "tedious", "truncated", "twice", "ugly",
			"unacceptable", "unclickable", "undesirable", "undesired",
			"unexpected", "unfriendly", "uninitialized", "unreadable",
			"unresponsive", "unsecure", "unstable", "unstyled", "unstoppable",
			"untrusted", "unusable", "voided", "wacky", "weird", "wrong" };

	public final static String[] FALSE_VERBS = { "encode", "build", "httpd",
			"duplicate", "miss", "orphan", "stack", "truncate",
			"misplace" , "freeze"};

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		ArrayList<Integer> indexVerb = new ArrayList<Integer>();
		//check if the sentences starts with a label followed by semicolon
		for (int i =0; i< tokens.size(); i++){
			if((i>0) && (tokens.get(i).getWord().equals(":")) && (tokens.get(i-1).getLemma().equals("observe"))){
				sentence = new Sentence (sentence.getId(), tokens.subList(i+1, tokens.size()));
				tokens = sentence.getTokens();
			}
		}
		boolean noVerb = true;
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")) {
				noVerb = false;
				indexVerb.add(i);
			}
		}
		// find Noun_Phrase with error terms
		if (noVerb) {
			if(checkErrorNounPhrase(tokens)==1){
				return 1;
			}
			// verify that is not an Error how sentence
		} else {
			for (int i = 0; i < indexVerb.size(); i++) {
				Token token = tokens.get(indexVerb.get(i));
				if (Arrays.stream(FALSE_VERBS).anyMatch(t-> token.getLemma().contains(t))) {
					return 1;
				}
			}
			
			//check if the verb is after : or included in () and the first sentence is a noun phrase 
			for(int i =0; i< indexVerb.size(); i++){
				Sentence sentence2 = new Sentence(sentence.getId(), tokens.subList(0, indexVerb.get(i)));
				List<Token> tokens2 = sentence2.getTokens();
				int index = findSemiColonOrParenthesis(tokens2);
				if(index != -1 && index > 0 && index < tokens2.size()){
					Sentence first = new Sentence(sentence2.getId(),tokens2.subList(0, index));
					List<Token> firstToken = first.getTokens();
					boolean noVerb2 = true;
					for (int j = 0; j < firstToken.size(); j++) {
						Token token = firstToken.get(j);
						if (token.getGeneralPos().equals("VB")) {
							noVerb = false;
						}
					}
					if(checkErrorNounPhrase(firstToken)==1 && noVerb2){
						return 1;
					}
				}
			}
			/*
			 * PatternMatcher pm = new ErrorHowPM(); int
			 * match=pm.matchSentence(sentence); if(match==0){ return 1; }
			 */
			return 0;
		}
		return 0;
	}
	
	private int findSemiColonOrParenthesis (List<Token> tokens){
		for (int i=0; i< tokens.size(); i++){
			Token token = tokens.get(i);
			if(token.getWord().equals(":")||token.getWord().equals("-LRB-")){
				return i;
			}
		}
		return -1;
	}
	
	private int checkErrorNounPhrase (List<Token> tokens){
		for (Token token : tokens) {
			if (Arrays.stream(VerbErrorPM.ERROR_TERMS).anyMatch(
					t -> token.getLemma().contains(t))
					&& token.getGeneralPos().equals("NN")) {
				return 1;
			} else if (Arrays.stream(ADJ_ERROR_TERMS).anyMatch(
					t -> token.getLemma().contains(t))
					&& token.getGeneralPos().equals("JJ")) {
				return 1;
			}
		}
		return 0;
	}

}
