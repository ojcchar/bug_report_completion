package seers.bugreppatterns.entity;

import java.util.ArrayList;
import java.util.List;

import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class Paragraph {

	private String id;
	private List<Sentence> sentences;

	public Paragraph(String id) {
		this.id = id;
		sentences = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public List<Sentence> getSentences() {
		return sentences;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSentences(List<Sentence> sentences) {
		this.sentences = sentences;
	}

	public void addSentence(Sentence sentence) {
		this.sentences.add(sentence);
	}

	public boolean isEmpty() {
		return sentences.isEmpty();
	}

	public List<Token> getTokens() {
		List<Token> tokens = new ArrayList<>();

		this.sentences.forEach(s -> {
			tokens.addAll(s.getTokens());
		});

		return tokens;

	}

	@Override
	public String toString() {
		return "Paragraph [id=" + id + ", sentences=" + sentences + "]";
	}

	public void addSentences(List<Sentence> sentences2) {
		this.sentences.addAll(sentences2);
	}

}
