package seers.bugreppatterns.pattern;

import java.util.List;

import seers.bugreppatterns.entity.Document;
import seers.bugreppatterns.entity.Paragraph;
import seers.textanalyzer.entity.Sentence;

public abstract class PatternMatcher {

	private Integer code;
	private String name;

	public static final String OB = "OB";
	public static final String EB = "EB";
	public static final String SR = "SR";

	public abstract int matchSentence(Sentence sentence) throws Exception;

	public int matchParagraph(Paragraph paragraph) throws Exception {
		return defaultMatchParagraph(paragraph);
	}

	public int matchDocument(Document bugReport) throws Exception {

		int numMatches = 0;
		List<Paragraph> paragraphs = bugReport.getParagraphs();
		for (Paragraph p : paragraphs) {
			numMatches += matchParagraph(p);
		}
		return numMatches;
	}

	protected int defaultMatchParagraph(Paragraph paragraph) throws Exception {
		List<Sentence> sentences = paragraph.getSentences();

		int numMatches = 0;
		for (Sentence sentence : sentences) {
			numMatches += matchSentence(sentence);
		}

		return numMatches;
	}

	public abstract String getType();

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
