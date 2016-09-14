package seers.bugreppatterns.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import seers.bugreppatterns.entity.Document;
import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public abstract class PatternMatcher {

	Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private Integer code;
	private String name;

	public static final String OB = "OB";
	public static final String EB = "EB";
	public static final String SR = "SR";

	public static final String[] CONDITIONAL_TERMS = { "if", "upon", "when", "whenever", "whereas", "while" };
	
	public static final Set<String> CONDITIONAL_TERMS_2 = JavaUtils.getSet("if", "upon", "when", "whenever", "whereas", "while" );
	
	public static final String[] CONTRAST_TERMS = { "although", "but", "however", "nevertheless", "though", "yet" };

	public abstract int matchSentence(Sentence sentence) throws Exception;

	/**
	 * Finds the indexes of the tokens whose lemmas *exactly* match any of the
	 * given terms, ignoring the case
	 * 
	 * @param terms
	 * @param tokens
	 * @return
	 */
	@Deprecated
	protected List<Integer> findLemmasInTokens(String[] terms, List<Token> tokens) {
		ArrayList<Integer> indexConditionalTerms = new ArrayList<Integer>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (SentenceUtils.matchTermsByLemma(terms, token)) {
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

	/**
	 * Divides the sentence into subsentences according to the indexes provided
	 * in separatorIndexes. The subSentences do not include the tokens given by
	 * the separatorIndexes.
	 * 
	 * @param sentence
	 * @param separatorIndexes
	 * @return
	 */
	protected List<Sentence> findSubSentences(Sentence sentence, List<Integer> separatorIndexes) {
		List<Sentence> subSentences = new ArrayList<Sentence>();
		if (separatorIndexes.isEmpty()) {
			subSentences.add(sentence);
		} else {
			for (int i = 0; i <= separatorIndexes.size(); i++) {
				int start = i == 0 ? 0 : separatorIndexes.get(i - 1) + 1;
				int end = i == separatorIndexes.size() ? sentence.getTokens().size() : separatorIndexes.get(i);
				if (end > start) {
					Sentence subSentence = new Sentence(sentence.getId(), sentence.getTokens().subList(start, end));
					subSentences.add(subSentence);
				}
			}
		}
		return subSentences;
	}

	/**
	 * checks if the sentence matches any of the patterns in the array of
	 * patterns
	 * 
	 * @param sentence
	 *            sentence to be analyzed
	 * @param patterns
	 *            patterns to match the sentence with
	 * @return true if the sentence matches any of the patterns, false otherwise
	 * @throws Exception
	 */
	public boolean sentenceMatchesAnyPatternIn(Sentence sentence, PatternMatcher[] patterns) throws Exception {
		for (PatternMatcher pm : patterns) {
			int match = pm.matchSentence(sentence);
			if (match == 1) {
				return true;
			}
		}
		return false;
	}
}
