package seers.bugreppatterns.pattern.sr;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NounActionsMultiPM extends StepsToReproducePatternMatcher{

 	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		return 0;
	}
	
	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception{
		
		List<Sentence> sentences = paragraph.getSentences();
		if(sentences.size()>1){
			int count = 0;
			Sentence sentence=sentences.get(0);
			String text=TextProcessor.getStringFromLemmas(sentence);
			if(text.equals("step :")||text.matches(".*to reproduce :")||(text.matches(".*reproduce as follow :"))){
				for(int i=1;i<sentences.size();i++){
					Sentence s=sentences.get(i);
					List<Token> tokens = s.getTokens();
					text=TextProcessor.getStringFromLemmas(s);
					if(text.matches("^(\\d+|\\-) \\.")){
						continue;
					}else{
						boolean isNoun = isANounPhrase(s);
						boolean isAction = isAnAction(tokens.get(0),tokens.get(1));
						if(isNoun){
							count++;
						}else if(hasANounTerm(tokens)){
							count++;
						}
						if(isAction){
							count++;
						}else{
							for (int y = 0; y < tokens.size(); y++) {
								Token tok = tokens.get(y);
								if (tok.getLemma().matches("\\p{Punct}")) {
									if (y + 1 < tokens.size()) {
										Token firstToken2 = tokens.get(y + 1);
										isAction = isAnAction(firstToken2, null);
										if (isAction) {
											count++;
										}
									}
								}

							}
						}
					}
				}
			}
			if(count>1){
				return 1;
			}
		}

		return 0;
	}

	public boolean isANounPhrase(Sentence sentence){
		List<Token> tokens=sentence.getTokens();
		if(tokens.size()>2){
			for (Token token : tokens) {
				if(token.getGeneralPos().equals("VB")){
					return false;
				}
			}
		}
		return true;
	}
	
	final private static String[] UNDETECTED_VERBS = { "show", "boomark", "rename", "run", "select", "post", "stop" };

	public boolean isAnAction (Token firstToken, Token secondToken) {

		if (firstToken.getPos().equals("VB") || firstToken.getPos().equals("VBP")) {
			return true;
		} else {
			if (secondToken != null) {

				if (firstToken.getPos().equals("RB")
						&& (secondToken.getPos().equals("VB") || secondToken.getPos().equals("VBP"))) {
					return true;
				}
			}
			if (Arrays.stream(UNDETECTED_VERBS).anyMatch(p -> firstToken.getLemma().equals(p))) {
				return true;
			}
		}
		return false;
	}
	
	final static String [] NOUNS_TERM={"page","error","exception","warning","message","grief","use"};
	
	public boolean hasANounTerm(List<Token> tokens){
		for (Token token : tokens) {
			if(token.getGeneralPos().equals("NN")){
				if (Arrays.stream(NOUNS_TERM).anyMatch(t -> t.equals(token.getLemma()))){
					return true;
				}
			}

		}

		return false;
	}
}
