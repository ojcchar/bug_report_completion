package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class LabeledCodeFragmentsPM extends StepsToReproducePatternMatcher {

	private static final Set<String> OTHER_LABEL_TOKENS = JavaUtils.getSet("example", "e.g.", "persistence.xml");

	private static final Set<String> CODE_SYMBOLS = JavaUtils.getSet("-LCB-", "-RCB-", "-LRB-", "-RRB-", "-LSB-",
			"-RSB-", ";", "*", "/", "%", "+", "-", "<", ">", "=", "!", "&", "^", "|", "?", ":", "\"", "'", "--", "++");

	private static final Set<String> JAVA_KEYWORDS = JavaUtils.getSet("abstract", "continue", "for", "new", "switch",
			"assert", "default", "goto", "package", "synchronized", "boolean", "do", "if", "private", "this", "break",
			"double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum",
			"instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final",
			"interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float",
			"native", "super", "while");

	private static final Set<String> DOCKER_KEYWORDS = JavaUtils.getSet("add", "copy", "env", "expose", "label",
			"maintainer", "user", "workdir", "volume", "stopsignal");

	private static final Set<String> DOCKER_COMMAND_KEYWORDS = JavaUtils.getSet("attach", "build", "commit", "connect",
			"cp", "create", "demote", "diff", "disconnect", "dockerd", "events", "exec", "export", "history", "images",
			"import", "info", "init", "inspect", "join", "join-token", "kill", "leave", "load", "login", "logout",
			"logs", "ls", "network", "node", "pause", "port", "promote", "ps", "pull", "push", "rename", "restart",
			"rm", "rmi", "run", "save", "scale", "search", "service", "start", "stats", "stop", "swarm", "tag", "top",
			"unpause", "update", "version", "volume", "wait");

	private static final Set<String> SQL_KEYWORDS = JavaUtils.getSet("alter", "begin", "between", "boolean", "case",
			"char", "column", "commit", "constraint", "create", "cursor", "date", "declare", "decimal", "delete",
			"distinct", "fetch", "foreign", "from", "function", "having", "inner", "insert", "into", "join", "like",
			"outer", "procedure", "primary", "rollback", "select", "table", "timestamp", "update", "varchar");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		// not enough sentences
		if (paragraph.getSentences().size() < 2) {
			return 0;
		}

		// --------------------------------------------------

		// check the label with code terms

		Sentence sentence1 = paragraph.getSentences().get(0);
		List<Token> stnc1Tokens = sentence1.getTokens();

		if (SentenceUtils.findLemmasInTokens(ConditionalCodeParagraphPM.NOUNS_TERM, stnc1Tokens).isEmpty()
				&& SentenceUtils.findLemmasInTokens(OTHER_LABEL_TOKENS, stnc1Tokens).isEmpty()) {
			return 0;
		}
		// --------------------------------------------------

		List<Sentence> otherSentences = paragraph.getSentences().subList(1, paragraph.getSentences().size());

		// count the # of tokens for the remaining sentences
		int sentenceNumTokens = 0;
		for (int i = 0; i < otherSentences.size(); i++) {
			Sentence sentence = otherSentences.get(i);
			
			sentenceNumTokens += sentence.getTokens().size();
		}
		// --------------------------------------------------

		// check for code
		Set<?>[] keywords = { JAVA_KEYWORDS, DOCKER_KEYWORDS, SQL_KEYWORDS, DOCKER_COMMAND_KEYWORDS };

		for (Set<?> keywordSet : keywords) {
			@SuppressWarnings("unchecked")
			List<Integer> keyWordsMatched = checkForCode(otherSentences, (Set<String>) keywordSet);
			float ratio = ((float) keyWordsMatched.size()) / sentenceNumTokens;
			if (ratio > 0.2F) {
				return 1;
			}
		}

		// check for xml
		int numTags = checkForXML(otherSentences);
		float ratio = ((float) numTags) / sentenceNumTokens;
		if (ratio > 0.2F) {
			return 1;
		}

		return 0;
	}

	private int checkForXML(List<Sentence> otherSentences) {
		int numTags = 0;
		for (int i = 0; i < otherSentences.size(); i++) {
			Sentence sentence = otherSentences.get(i);

			long numTagTokens = sentence.getTokens().stream().filter(tok ->
			// case: <persistence-tag name="..." >
			tok.getLemma().matches("<[\\w-]+ .*>") ||
			// cases: <tag> or </tag>
					tok.getLemma().matches("</?[\\w-]+>")).count();
			numTags += numTagTokens;
		}
		return numTags;
	}

	private List<Integer> checkForCode(List<Sentence> otherSentences, Set<String> keywords) {
		List<Integer> keyWordsMatched = new ArrayList<>();

		for (int i = 0; i < otherSentences.size(); i++) {
			Sentence sentence = otherSentences.get(i);

			// find the tokens matching the keywords
			List<Integer> idxs = SentenceUtils.findLemmasInTokens(keywords, sentence.getTokens());
			keyWordsMatched.addAll(idxs);

			// find the tokens matching the symbols
			idxs = SentenceUtils.findLemmasInTokens(CODE_SYMBOLS, sentence.getTokens());
			keyWordsMatched.addAll(idxs);

		}
		return keyWordsMatched;
	}

}
