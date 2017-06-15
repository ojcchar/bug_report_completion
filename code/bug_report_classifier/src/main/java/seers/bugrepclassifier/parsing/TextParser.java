package seers.bugrepclassifier.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import seers.bugrepcompl.entity.regularparse.BugReportDescription;
import seers.bugrepcompl.entity.regularparse.DescriptionParagraph;
import seers.bugrepcompl.entity.regularparse.DescriptionSentence;

public class TextParser {

	private StanfordCoreNLP pipeline;

	public TextParser() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse");
		pipeline = new StanfordCoreNLP(props);
	}

	public BugReportDescription parseText(String bugDescription) {

		ArrayList<ArrayList<String>> paragraphSplit = splitText(bugDescription);

		return createParsedDescription(paragraphSplit);

	}

	private ArrayList<ArrayList<String>> splitText(String bugDescription) {
		// parse paragraphs
		List<String> paragraphs = new ArrayList<>();
		String[] txtSplit = bugDescription.split("\n\n");
		for (int i = 0; i < txtSplit.length; i++) {
			String split = txtSplit[i];
			if (split.isEmpty()) {
				continue;
			}
			paragraphs.add(split);
		}

		ArrayList<ArrayList<String>> paragraphSplit = splitIntoParagraphsAndSentences(paragraphs);
		return paragraphSplit;
	}

	private ArrayList<ArrayList<String>> splitIntoParagraphsAndSentences(List<String> paragraphs) {

		// --------------------------------
		// based on Jing's code:

		ArrayList<ArrayList<String>> paragraphSplit = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < paragraphs.size(); i++) {

			String paragraph = paragraphs.get(i);
			String[] parSplit = paragraph.split("\n");

			// split paragraph by row, to check if there exists some special
			// case
			boolean flag = false;

			if (paragraph.toLowerCase().startsWith("steps to reproduce")) {
				flag = true;
			} else {
				for (String s : parSplit) {
					// pattern 1.) 1. 1) -
					if (s.matches("(\\W*)\\d((\\.\\))|\\.|\\)|,|-)(.*)") || s.matches("(\\W*)(-|\\*)(.*)")) {
						flag = true;
						break;
					}
				}
			}

			boolean errorflag = false;
			for (String s : parSplit) {
				if (s.matches("(\\W*)(at )(.*)")) {
					errorflag = true;
					break;
				}
			}

			ArrayList<String> parseSentences = new ArrayList<String>();
			// if paragraph contains special patterns, corenlp will process
			// line by line.
			// Otherwise, process the paragraph directly
			if (flag == true) {
				for (String s : parSplit) {
					parseSentences.add(s);
				}
			} else {
				parseSentences.add(paragraph);
			}

			ArrayList<String> splittedSentence = new ArrayList<String>();

			// paragraph contains error code such as:at
			// java.lang.Object.wait(Native Method) will not be processed by
			// corenlp
			if ((errorflag == true || paragraphs.get(i).length() > 700) && flag == false) {
				for (String s : parSplit) {
					splittedSentence.add(s);
				}
				paragraphSplit.add(splittedSentence);
				// Common.pause(" ");
				continue;
			}

			for (int sid = 0; sid < parseSentences.size(); sid++) {
				String parseSentence = parseSentences.get(sid);
				if (parseSentence.startsWith("java.") || parseSentence.startsWith("	at ")
						|| parseSentence.startsWith("\"") || parseSentence.startsWith("org.apache")) {// parseSentence.matches("(\\W*)(at
																										// )(\\w+\\.){3,}(.*)")){
//					System.out.println("NOT processing: " + parseSentence);
					splittedSentence.add(parseSentence);
					if (sid == parseSentences.size() - 1) {
						paragraphSplit.add(splittedSentence);
					}
					continue;
				}

				if (parseSentence.matches("(\\W*)\\d((\\.\\))|\\.|\\)|,|-)(.*)")) {
					parseSentence = parseSentence.replaceFirst("((\\.\\))|\\.|\\)|,|-)\\s*", " ");
					// System.out.println(parseSentence);
				}
				// System.out.println(parseSentence);
				Annotation document = new Annotation(parseSentence);

				pipeline.annotate(document);

				List<CoreMap> sentence = document.get(SentencesAnnotation.class);

				for (CoreMap s : sentence) {
					// CoreMap s = sentence.get(0);
					String plainSentence = s.toShorterString("Text");
					plainSentence = plainSentence.substring(plainSentence.indexOf("=") + 1, plainSentence.length() - 1);

					splittedSentence.add(plainSentence);

				}

				if (sid == parseSentences.size() - 1) {
					paragraphSplit.add(splittedSentence);
				}
			}
		}
		return paragraphSplit;
	}

	private BugReportDescription createParsedDescription(ArrayList<ArrayList<String>> paragraphSplit) {

		BugReportDescription desc = new BugReportDescription();

		List<DescriptionParagraph> paragraphs = new ArrayList<>();
		for (int i = 1; i <= paragraphSplit.size(); i++) {

			ArrayList<String> sentenceSplit = paragraphSplit.get(i - 1);
			if (sentenceSplit.isEmpty()) {
				continue;
			}

			// -----------------------

			DescriptionParagraph paragraph = new DescriptionParagraph();
			paragraph.setId(i + "");

			List<DescriptionSentence> sentences = new ArrayList<>();

			for (int j = 1; j <= sentenceSplit.size(); j++) {
				DescriptionSentence sent = new DescriptionSentence();
				sent.setId(i + "." + j);
				sent.setValue(sentenceSplit.get(j - 1));
				sentences.add(sent);
			}

			paragraph.setSentences(sentences);
			paragraphs.add(paragraph);

		}

		if (paragraphs.isEmpty()) {
			return null;
		}

		desc.setParagraphs(paragraphs);

		return desc;
	}

	public seers.bugrepcompl.entity.patterncoding.BugReportDescription parseText2(String description) {
		ArrayList<ArrayList<String>> paragraphSplit = splitText(description);

		return createParsedCodingDescription(paragraphSplit);
	}

	private seers.bugrepcompl.entity.patterncoding.BugReportDescription createParsedCodingDescription(
			ArrayList<ArrayList<String>> paragraphSplit) {
		seers.bugrepcompl.entity.patterncoding.BugReportDescription desc = new seers.bugrepcompl.entity.patterncoding.BugReportDescription();

		List<seers.bugrepcompl.entity.patterncoding.DescriptionParagraph> paragraphs = new ArrayList<>();
		for (int i = 1; i <= paragraphSplit.size(); i++) {

			ArrayList<String> sentenceSplit = paragraphSplit.get(i - 1);
			if (sentenceSplit.isEmpty()) {
				continue;
			}

			// -----------------------

			seers.bugrepcompl.entity.patterncoding.DescriptionParagraph paragraph = new seers.bugrepcompl.entity.patterncoding.DescriptionParagraph();
			paragraph.setId(i + "");

			List<seers.bugrepcompl.entity.patterncoding.DescriptionSentence> sentences = new ArrayList<>();

			for (int j = 1; j <= sentenceSplit.size(); j++) {
				seers.bugrepcompl.entity.patterncoding.DescriptionSentence sent = new seers.bugrepcompl.entity.patterncoding.DescriptionSentence();
				sent.setId(i + "." + j);
				sent.setValue(sentenceSplit.get(j - 1));
				sentences.add(sent);
			}

			paragraph.setSentences(sentences);
			paragraphs.add(paragraph);

		}

		if (paragraphs.isEmpty()) {
			return null;
		}

		desc.setParagraphs(paragraphs);

		return desc;
	}

}
