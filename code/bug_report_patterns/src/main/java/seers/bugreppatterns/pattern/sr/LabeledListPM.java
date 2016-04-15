package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class LabeledListPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}
	
	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		List<Sentence> sentences = paragraph.getSentences();
		String text="";
		int stepNumber=0;
		int verbSentenceNumber=0;
		if (sentences.size() > 1) {
			//verify if there is a numbered(bullet) sequence in the paragraph
			for (Sentence sentence : sentences) {
				List<Token>tokens=sentence.getTokens();
				text = TextProcessor.getStringFromLemmas(sentence);
				if (text.matches("^(\\d+|\\-).+")) {
					stepNumber++;
				}else if(tokens.size()>1 && tokens.get(0).getGeneralPos().equals("VB")){
					verbSentenceNumber++;
				}
			}
			if (verbSentenceNumber>0 || stepNumber>0){
				//the paragraph will start with a sentence likes steps to reproduce
				Sentence sentence = sentences.get(0);
				text=TextProcessor.getStringFromLemmas(sentence);
				if (text.matches("reproducible.*")) {
					sentence = sentences.get(1);
					text = TextProcessor.getStringFromLemmas(sentence);
				}
				//case like Repro steps:, reported in the following steps, steps to reproduce, what i have tried:
				boolean b  = (text.matches(".*(following|repro) step.*"))
						|| (text.matches(".*?(step)? ?to repro.*"))
						|| (text.equals("step :")) || (text.equals("str :"))
						|| (text.endsWith("have try :")) || (text.contains("here be the step"));
				if(b==false){
					sentence=sentences.get(1);
					b=(text.matches(".*(following|repro) step.*"))
							|| (text.matches(".*?(step)? ?to repro.*"))
							|| (text.equals("step :")) || (text.equals("str :"))
							|| (text.endsWith("have try :"))|| (text.contains("here be the step"));
				}
				if(b){
					return 1;
				}
				
			}
			if(stepNumber>0){
				return 1;
			}
		}
		
		return 0;
	}
}

