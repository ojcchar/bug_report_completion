package seers.bugreppatterns.entity;

import java.util.ArrayList;
import java.util.List;

import seers.textanalyzer.entity.Sentence;

public class Document {
	private String id;
	private List<Paragraph> paragraphs;

	public Document(String id) {
		this.id = id;
		paragraphs = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setParagraphs(List<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	public void addParagraph(Paragraph paragraph) {
		paragraphs.add(paragraph);
	}

	public int getNumOfSentences() {
		int numOfSentences = 0;
		for (Paragraph paragraph : paragraphs) {
			numOfSentences += paragraph.getSentences().size();
		}
		return numOfSentences;
	}

	public int getNumOfTokensInSentences() {
		int numOfTokens = 0;
		for (Paragraph paragraph : paragraphs) {
			for (Sentence sentence : paragraph.getSentences()) {
				numOfTokens += sentence.getTokens().size();
			}
		}
		return numOfTokens;
	}

}
