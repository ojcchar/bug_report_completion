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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paragraph other = (Paragraph) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
