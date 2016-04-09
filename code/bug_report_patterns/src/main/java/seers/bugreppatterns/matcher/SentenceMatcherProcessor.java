package seers.bugreppatterns.matcher;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.appcore.xml.XMLHelper;
import seers.bugreppatterns.entity.xml.BugReport;
import seers.bugreppatterns.entity.xml.DescriptionParagraph;
import seers.bugreppatterns.entity.xml.DescriptionSentence;

public class SentenceMatcherProcessor extends ThreadProcessor {

	private List<List<String>> sentences;

	@SuppressWarnings("unchecked")
	public SentenceMatcherProcessor(ThreadParameters params) {
		super(params);
		sentences = params.getParam(List.class, ThreadExecutor.ELEMENTS_PARAM);
	}

	@Override
	public void executeJob() throws Exception {
		for (List<String> sentence : sentences) {

			boolean error = checkSentencePattern(sentence);

			if (error) {
				continue;
			}

			String project = sentence.get(0);
			String bugId = sentence.get(1);
			String fileToRead = "test_data\\data\\" + project + "_parse\\" + bugId + ".xml.parse";

			try {
				BugReport bug = XMLHelper.readXML(BugReport.class, fileToRead);

				checkSentence(sentence, bug);
			} catch (Exception e) {
				LOGGER.error("Error for file: " + fileToRead, e);
			}

			// System.out.println(fileToRead);
		}
	}

	private boolean checkSentencePattern(List<String> sentence) {

		String project = sentence.get(0);
		String bugId = sentence.get(1);
		String sentenceId = sentence.get(2);
		String paragraphTxt = sentence.get(3);
		String sentenceTxt = sentence.get(4);

		if (paragraphTxt.trim().isEmpty()) {
			String msg = Arrays.toString(new String[] { "paragraph empty", project, bugId, sentenceId });
			LOGGER.error(msg);
			return true;
		}

		String pattern1 = sentence.get(8);
		String pattern2 = sentence.get(9);
		String pattern3 = sentence.get(10);

		if ((pattern1 + pattern2 + pattern2).trim().isEmpty()) {
			String msg = Arrays.toString(new String[] { "need pattern:", project, bugId, sentenceId });
			LOGGER.warn(msg);
		} else {

			if (sentenceTxt.trim().isEmpty()) {
				if (pattern1.trim().startsWith("S_") || pattern2.trim().startsWith("S_")
						|| pattern3.trim().startsWith("S_")) {
					String msg = Arrays.toString(new String[] { "pattern assignment:", project, bugId, sentenceId });
					LOGGER.error(msg);
					return true;
				}
			} else {
				if (!(pattern1.trim().startsWith("S_") || pattern2.trim().startsWith("S_")
						|| pattern3.trim().startsWith("S_"))) {
					String msg = Arrays.toString(new String[] { "pattern assignment:", project, bugId, sentenceId });
					LOGGER.warn(msg);
				}
			}
		}

		return false;
	}

	private void checkSentence(List<String> sentence, BugReport bug) {

		String project = sentence.get(0);
		String bugId = sentence.get(1);
		String sentenceId = sentence.get(2);
		String paragraphTxt = sentence.get(3);
		String sentenceTxt = sentence.get(4);

		paragraphTxt = paragraphTxt.replace("\"\"", "\"");
		sentenceTxt = sentenceTxt.replace("\"\"", "\"");

		if (sentenceTxt != null && !sentenceTxt.trim().isEmpty()) {

			processSentence(bug, project, bugId, sentenceId, sentenceTxt);
		} else {
			processParagraph(bug, project, bugId, sentenceId, paragraphTxt);
		}
	}

	private void processParagraph(BugReport bug, String project, String bugId, String sentenceId, String paragraphTxt) {
		String parId = null;
		String title = bug.getTitle();

		if (paragraphTxt.trim().equalsIgnoreCase(title)) {
			parId = "0";
		} else {
			List<DescriptionParagraph> paragraphs = bug.getDescription().getParagraphs();
			for (DescriptionParagraph p : paragraphs) {

				List<DescriptionSentence> sentences2 = p.getSentences();
				DescriptionSentence first = sentences2.get(0);
				DescriptionSentence last = sentences2.get(sentences2.size() - 1);

				String firstTrim = first.getValue().trim();
				String lastTrim = last.getValue().trim();

				// to tackle the normalization of lists 1. asdfas --> 1 asdfas
				if (lastTrim.length() > 4) {
					lastTrim = lastTrim.substring(4, lastTrim.length());
				}

				if (paragraphTxt.trim().startsWith(firstTrim) && paragraphTxt.trim().endsWith(lastTrim)) {

					parId = p.getId();
					break;
				}
			}
		}

		if (parId != null) {
			String msg = Arrays.toString(new String[] { "paragraph:", project, bugId, sentenceId, "->", parId });
			System.out.println(msg);
		} else {

			String msg = Arrays.toString(new String[] { "paragraph:", project, bugId, sentenceId, "mismatch" });
			System.out.println(msg);
		}
	}

	private void processSentence(BugReport bug, String project, String bugId, String sentenceId, String sentenceTxt) {
		String parId = null;
		String sentId = null;

		String title = bug.getTitle();

		if (sentenceTxt.trim().equalsIgnoreCase(title)) {
			parId = "0";
			sentId = "0.1";
		} else {

			List<DescriptionParagraph> paragraphs = bug.getDescription().getParagraphs();
			for (DescriptionParagraph p : paragraphs) {

				List<DescriptionSentence> sentences2 = p.getSentences();
				Optional<DescriptionSentence> first = sentences2.stream()
						.filter(s -> sentenceTxt.trim().equalsIgnoreCase(s.getValue().trim())).findFirst();
				if (first.isPresent()) {
					parId = p.getId();
					sentId = first.get().getId();
					break;
				}
			}
		}

		if (parId != null && sentId != null) {
			String msg = Arrays.toString(new String[] { "sentence:", project, bugId, sentenceId, "->", parId, sentId });
			System.out.println(msg);
		} else {
			String msg = Arrays.toString(new String[] { "sentence:", project, bugId, sentenceId, "mismatch" });
			System.out.println(msg);
		}
	}

}
