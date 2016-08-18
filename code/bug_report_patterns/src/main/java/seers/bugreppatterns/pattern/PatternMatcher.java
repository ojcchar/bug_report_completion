package seers.bugreppatterns.pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import seers.bugreppatterns.entity.Document;
import seers.bugreppatterns.entity.Paragraph;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public abstract class PatternMatcher {

	Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private Integer code;
	private String name;

	public static final String OB = "OB";
	public static final String EB = "EB";
	public static final String SR = "SR";

	public final String[] CONDITIONAL_TERMS = { "if", "upon", "when", "whenever", "whereas", "while" };
	public final String[] CONTRAST_TERMS = { "although", "but", "however", "nevertheless", "though", "yet" };

	public abstract int matchSentence(Sentence sentence) throws Exception;

	/**
	 * Finds the indexes of the tokens that *exactly* match any of the given terms, ignoring the case
	 * 
	 * @param terms
	 * @param tokens
	 * @return
	 */
	public ArrayList<Integer> findTermsInTokens(String[] terms, List<Token> tokens) {
		ArrayList<Integer> indexConditionalTerms = new ArrayList<Integer>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (Arrays.stream(terms).anyMatch(t -> token.getWord().equalsIgnoreCase(t))) {
				indexConditionalTerms.add(i);
			}
		}
		return indexConditionalTerms;
	}

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
