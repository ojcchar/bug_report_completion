package seers.bugreppatterns.main.preprocessing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.routines.UrlValidator;

import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.regularparse.BugReport;
import seers.bugrepcompl.entity.regularparse.DescriptionParagraph;
import seers.bugrepcompl.entity.regularparse.DescriptionSentence;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class BugPreprocessor extends ThreadProcessor {

	private String inputFolder;
	private String outputFolder;
	private List<TextInstance> bugs;
	private boolean lemmatization = false;
	private static boolean stopwordRemoval = false;
	private static List<String> stopWords;

	public BugPreprocessor(ThreadParameters params) {
		super(params);

		inputFolder = params.getStringParam("inputFolder");
		outputFolder = params.getStringParam("outputFolder");
		lemmatization = params.getParam(Boolean.class, "lemmatization");
		stopwordRemoval = params.getParam(Boolean.class, "stopwordRemoval");
		stopWords = params.getListParam(String.class, "stopWords");
		bugs = params.getListParam(TextInstance.class, ThreadExecutor.ELEMENTS_PARAM);
	}

	@Override
	public void executeJob() throws Exception {
		for (TextInstance bug : bugs) {

			try {
				System.out.println("Processing bug " + bug);
				BugReport bugNotPreprocessed = readBug(inputFolder, bug);
				BugReport bugPreprocessed = preprocessBug(bugNotPreprocessed);
				writeBug(bugPreprocessed, outputFolder, bug);
			} catch (Exception e) {
				System.err.println("Error for bug " + bug);
				e.printStackTrace();
			}

		}
	}

	private BugReport readBug(String inputFolder, TextInstance bug) throws Exception {
		String filepath = inputFolder + File.separator + bug.getProject() + "_parse" + File.separator + bug.getBugId()
				+ ".xml.parse";
		BugReport xmlBug = XMLHelper.readXML(BugReport.class, filepath);
		return xmlBug;
	}

	private void writeBug(BugReport bugPreprocessed, String outputFolder, TextInstance bug) throws Exception {
		File projectFolder = new File(outputFolder + File.separator + bug.getProject() + "_parse");
		if (!projectFolder.exists()) {
			projectFolder.mkdir();
		}

		File outputFile = new File(projectFolder.getAbsolutePath() + File.separator + bug.getBugId() + ".xml.parse");
		XMLHelper.writeXML(BugReport.class, bugPreprocessed, outputFile);

	}

	private BugReport preprocessBug(BugReport bugNotPreprocessed) {

		BugReport copyBug = new BugReport(bugNotPreprocessed);

		// ------------------------------------------------
		// Title

		Sentence titlePreprocessed = preprocessSentence("0", copyBug.getTitle());
		if (titlePreprocessed == null) {
			titlePreprocessed = new Sentence("0", new ArrayList<>());
		}
		String terms = getSentenceTxt(titlePreprocessed);
		copyBug.setTitle(terms);

		// ------------------------------------------------
		// Description

		List<DescriptionParagraph> paragraphs = copyBug.getDescription().getParagraphs();
		for (DescriptionParagraph descriptionParagraph : paragraphs) {
			preprocessParagraph(descriptionParagraph);
		}

		// ------------------------------------------------

		return copyBug;
	}

	private void preprocessParagraph(DescriptionParagraph descriptionParagraph) {

		List<DescriptionSentence> sentences = descriptionParagraph.getSentences();

		if (sentences == null) {
			return;
		}
		
		for (int i = 0; i < sentences.size(); i++) {
			DescriptionSentence descriptionSentence = sentences.get(i);

			String sentenceTxt = descriptionSentence.getValue();

			if (isJavaStackTraceLine(sentenceTxt)) {
				descriptionSentence.setValue("");
				continue;
			}

			Sentence stncPreprocessed = preprocessSentence(descriptionSentence.getId(), sentenceTxt);
			if (stncPreprocessed == null) {
				continue;
			}
			String terms = getSentenceTxt(stncPreprocessed);

			descriptionSentence.setValue(terms);

		}

	}

	private String getSentenceTxt(Sentence stncPreprocessed) {
		String terms;
		if (lemmatization) {
			terms = TextProcessor.getStringFromLemmasAndPos(stncPreprocessed);
		} else {
			terms = TextProcessor.getStringFromTermsAndPos(stncPreprocessed, true);
		}
		return terms;
	}

	public static boolean isJavaStackTraceLine(String sentenceTxt) {
		return sentenceTxt.matches("(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)");
	}

	public static Sentence preprocessSentence(String sentenceId, String sentenceTxt) {

		Sentence sentence = SentenceUtils.parseSentence(sentenceId, sentenceTxt);

		if (sentence == null) {
			return null;
		}

		Sentence preprocesedStnce = new Sentence(sentence.getId());
		for (Token token : sentence.getTokens()) {

			// System.out.println(token);
			if (stopwordRemoval && isStopWord(token)) {
				continue;
			}

			if (isNumber(token)) {
				continue;
			}

			if (isSpecialChar(token) && !isPunctuation(token)) {
				continue;
			}

			if (isURL(token)) {
				token = new Token("URL_TOK", "NN", "NN", "URL_TOK", "URL_TOK");
			}

			preprocesedStnce.addToken(token);
		}

		return preprocesedStnce;
	}

	private static boolean isStopWord(Token token) {
		boolean isStopWord = TextProcessor.isStopWord(stopWords, token.getLemma(), token.getPos());
		return isStopWord;
	}

	private static boolean isPunctuation(Token token) {
		return token.getWord().matches("[&,.;:]");
	}

	public static boolean isURL(Token token) {
		String url = token.getWord();
		
		//-----------------------------------------

		if (url.endsWith("]")) {
			url = url.substring(0, url.length() - 2);
		} else if (url.endsWith("]:")) {
			url = url.substring(0, url.length() - 3);
		}
		
		//-----------------------------------------

		UrlValidator urlValidator = new UrlValidator();
		boolean valid = urlValidator.isValid(url);
		if (!valid) {
			valid = urlValidator.isValid(url);
		}
		
		//-----------------------------------------
		
		return valid;
	}

	public static boolean isSpecialChar(Token token) {
		return token.getWord().matches("[^A-Za-z0-9]") || token.getWord().equals("``") || token.getWord().equals("''")
				|| TextProcessor.isParenthesis(token.getWord())
				|| token.getWord().matches("[\\Q$&+,:;=?@#|'<>.^*()%!-][}{\\E]++");
	}

	public static boolean isNumber(Token token) {
		return NumberUtils.isNumber(token.getWord()) || token.getWord().matches("^\\d[\\.,][\\d[\\.,]]+")
				|| token.getWord().matches("^[$&+,:;=?@#|'<>.^*()%!-\\[\\]\\{\\}]\\d[\\d[\\.,]]+");
	}
}
