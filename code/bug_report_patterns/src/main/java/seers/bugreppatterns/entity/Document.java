package seers.bugreppatterns.entity;

import java.util.ArrayList;
import java.util.List;

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

}
