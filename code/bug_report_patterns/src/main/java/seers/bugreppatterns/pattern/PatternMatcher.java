package seers.bugreppatterns.pattern;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import seers.bugreppatterns.entity.Document;
import seers.bugreppatterns.entity.Paragraph;
import seers.textanalyzer.entity.Sentence;

public abstract class PatternMatcher {

	Logger LOGGER = LoggerFactory.getLogger(this.getClass());
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
			try {
				numMatches += matchParagraph(p);
			} catch (Exception e) {
				LOGGER.error(
						"Error for bug " + bugReport.getId() + ", paragraph: " + p.getId() + ": " + e.getMessage());
				throw e;
			}
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		PatternMatcher other = (PatternMatcher) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
