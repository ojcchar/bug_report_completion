package seers.nimbus.bugparser.sentence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class SentenceSplitter {

	private static StanfordCoreNLP pipeline;

	static {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit");
		pipeline = new StanfordCoreNLP(props);
	}

	static List<List<String>> splitIntoParagraphsAndSentences(String inputText) {

		List<List<String>> paragSentences = new ArrayList<>();

		// no input
		if (inputText == null || inputText.trim().isEmpty()) {
			return paragSentences;
		}

		// split the input into paragraphs
		List<String> paragraphs = Arrays.asList(inputText.split("\n\n"));

		for (String paragraph : paragraphs) {

			// empty paragraph
			if (paragraph.trim().isEmpty()) {
				continue;
			}

			// -----------------------------------

			List<String> sentences = new ArrayList<>();

			// split lines of this paragraph
			List<String> textLines = Arrays.asList(paragraph.split("\n"));
			for (String txtLine : textLines) {

				if (txtLine.trim().isEmpty()) {
					continue;
				}

				// -----------------------------

				// stack trace lines are not parsed
				if (isStackTraceLine(txtLine)) {
					sentences.add(txtLine);
				} else {

					// split the sentences of this line

					Annotation txtDoc = new Annotation(txtLine);
					pipeline.annotate(txtDoc);
					List<CoreMap> sentAnnotations = txtDoc.get(SentencesAnnotation.class);

					for (CoreMap sentAnn : sentAnnotations) {
						String plainSentence = sentAnn.toShorterString("Text");
						plainSentence = plainSentence.substring(plainSentence.indexOf("=") + 1,
								plainSentence.length() - 1);
						sentences.add(plainSentence);
					}
				}

			}

			// ------------------------------
			// Now, we do sentence merging because two different lines may
			// belong to the same sentence

			// -----------------------------
			// ** merge sentences by enumerations **

			List<String> newSentences = mergeSentenceByEnumerations(sentences);

			// -----------------------------
			// ** merge sentences by file extensions (iteratively) **

			List<String> newSentences2 = null;
			int previousLength2 = newSentences.size();
			do {
				if (newSentences2 != null) {
					previousLength2 = newSentences2.size();
					newSentences2 = mergeSentencesByFileExtension(newSentences2);
				} else {
					newSentences2 = mergeSentencesByFileExtension(newSentences);
				}
			} while (newSentences2.size() != previousLength2);

			// -----------------------------

			// ** merge sentences with multiple breaks

			List<String> newSentences3 = null;
			int previousLength3 = newSentences2.size();
			do {
				if (newSentences3 != null) {
					previousLength3 = newSentences3.size();
					newSentences3 = mergeSentencesByLineBreaks(newSentences3);
				} else {
					newSentences3 = mergeSentencesByLineBreaks(newSentences2);
				}
			} while (newSentences3.size() != previousLength3);

			// -----------------------------

			paragSentences.add(newSentences3);
		}
		return paragSentences;
	}

	private static List<String> mergeSentencesByLineBreaks(List<String> newSentences2) {
		List<String> newSentences3 = new ArrayList<>();

		for (int i = newSentences2.size() - 1; i >= 0; i--) {

			String sentence = newSentences2.get(i);

			String mergedSentence = sentence;

			if (i - 1 >= 0) {
				String previousSentence = newSentences2.get(i - 1);
				if (wereBrokenByNewLine(previousSentence, sentence)) {
					mergedSentence = previousSentence + " " + sentence;
					i--;
				}
			}

			newSentences3.add(0, mergedSentence);
		}
		return newSentences3;
	}

	private static boolean wereBrokenByNewLine(String previousSentence2, String sentence) {

		if (!previousSentence2.endsWith(".") && !previousSentence2.endsWith(":") && !previousSentence2.endsWith(";")
				&& !previousSentence2.endsWith(")") && sentence.matches("^[a-z].+") && !isStackTraceLine(sentence)
				&& !isEnumeration(sentence, ".+", false)) {
			return true;
		}
		return false;
	}

	private static List<String> mergeSentenceByEnumerations(List<String> sentences) {

		// If there is a split of a sentence into two by the "dot" of the
		// enumeration we want to merge these two

		// For example:
		// 2.
		// this is a step
		// =>
		// 2. this is a step

		List<String> newSentences = new ArrayList<>();

		for (int i = 0; i < sentences.size(); i++) {

			String sentence = sentences.get(i);

			String newSentence = sentence;

			if (isEnumeration(sentence, "", true) && i + 1 < sentences.size()) {

				String nextSentence = sentences.get(i + 1);
				if (!isEnumeration(nextSentence, "", true)) {
					// avoid cases like "2. a." --> it should be "2.a."
					String space = nextSentence.matches("^[a-zA-Z]\\. .+") ? "" : " ";
					newSentence = sentence + space + nextSentence;
					i++;
				}

			}

			newSentences.add(newSentence);
		}
		return newSentences;
	}

	private static List<String> mergeSentencesByFileExtension(List<String> newSentences) {

		// If there is a split of a sentence into two by the "dot" of the
		// file extension, then merge them

		// For example:
		// this is .
		// pdf file
		// =>
		// this is a .pdf file

		List<String> newSentences2 = new ArrayList<>();

		for (int i = newSentences.size() - 1; i >= 0; i--) {

			String sentence = newSentences.get(i);

			String mergedSentence = sentence;

			if (i - 1 >= 0) {
				String previousSentence = newSentences.get(i - 1);
				if (wereBrokenByFileExtension(previousSentence, sentence)) {
					mergedSentence = previousSentence + sentence;
					i--;
				}
			}

			newSentences2.add(0, mergedSentence);
		}
		return newSentences2;
	}

	private static boolean wereBrokenByFileExtension(String previousSentence, String sentence) {

		if (previousSentence.endsWith(".")
				&& Arrays.stream(commonFileExt).anyMatch(fileExt -> sentence.toLowerCase().equals(fileExt)
						|| sentence.toLowerCase().matches("^" + fileExt + "[\\s+|\\W+].*"))) {
			return true;
		}
		return false;
	}

	private static String[] commonFileExt = {
			// potentially most common
			"class", "java", "jar", "xml", "apk", "bin", "sql",
			// the rest
			"3dm", "3ds", "3g2", "3gp", "7z", "accdb", "ai", "aif", "app", "asf", "asp", "aspx", "avi", "bak", "bat",
			"bmp", "c", "cab", "cbr", "cer", "cfg", "cfm", "cgi", "com", "cpl", "cpp", "crdownload", "crx", "cs", "csr",
			"css", "csv", "cue", "cur", "dat", "db", "dbf", "dds", "deb", "dem", "deskthemepack", "dll", "dmg", "dmp",
			"doc", "docx", "drv", "dtd", "dwg", "dxf", "eps", "exe", "fla", "flv", "fnt", "fon", "gadget", "gam", "ged",
			"gif", "gpx", "gz", "h", "hqx", "htm", "html", "icns", "ico", "ics", "iff", "indd", "ini", "iso", "jpg",
			"js", "jsp", "key", "keychain", "kml", "kmz", "lnk", "log", "lua", "m", "m3u", "m4a", "m4v", "max", "mdb",
			"mdf", "mid", "mim", "mov", "mp3", "mp4", "mpa", "mpg", "msg", "msi", "nes", "obj", "odt", "otf", "pages",
			"part", "pct", "pdb", "pdf", "php", "pkg", "pl", "plugin", "png", "pps", "ppt", "pptx", "prf", "ps", "psd",
			"pspimage", "py", "rar", "rm", "rom", "rpm", "rss", "rtf", "sav", "sdf", "sh", "sitx", "sln", "srt", "svg",
			"swf", "swift", "sys", "tar", "targz", "tax2016", "tex", "tga", "thm", "tif", "tiff", "tmp", "toast",
			"torrent", "ttf", "txt", "uue", "vb", "vcd", "vcf", "vcxproj", "vob", "wav", "wma", "wmv", "wpd", "wps",
			"wsf", "xcodeproj", "xhtml", "xlr", "xls", "xlsx", "yuv", "zip", "zipx" };

	private static boolean isEnumeration(String sentence, String regSuffix, boolean checkLength) {

		if (sentence.length() <= 10 || !checkLength) {

			String text = sentence.trim();

			// cases like: 1. or 1.)
			if (text.matches("^\\d+\\.[\\)|\\]|\\}]?" + regSuffix)) {
				return true;
			} else
			// cases like: 1a.
			// ---------------
			if (text.matches("^\\d+[a-zA-Z]\\." + regSuffix)) {
				return true;

			} else
			// ---------------
			// cases like: step1.
			if (text.matches("^[a-zA-Z]+\\d+\\." + regSuffix)) {
				return true;
			}
		}
		return false;
	}

	private static List<String> stackTracesRegexes = Arrays.asList(
			// "([a-zA-Z0-9]+[\\.\\$])+<?[a-zA-Z0-9]+>?\\([a-zA-Z0-9]+\\.java:\\d+\\)",
			// "([a-zA-Z0-9]+[\\.\\$])+<?[a-zA-Z0-9]+>?\\(Native Method\\)",
			// "([a-zA-Z0-9]+[\\.\\$])+<?[a-zA-Z0-9]+>?\\(Unknown Source\\)",
			// "\\$([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
			"(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+<?[a-zA-Z0-9]+>?\\([a-zA-Z0-9]+\\.java:\\d+\\)",
			"(\\s+)?(at )?\\$([a-zA-Z0-9]+[\\.\\$])+<?[a-zA-Z0-9]+>?\\(Unknown Source\\)",
			"(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+<?[a-zA-Z0-9]+>?\\(Unknown Source\\)",
			"(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+<?[a-zA-Z0-9]+>?\\(Native Method\\)",
			"(\\s+)?(at )?\\$([a-zA-Z0-9]+[\\.\\$])+<?[a-zA-Z0-9]+>?\\(Native Method\\)", "Caused by\\: .+Exception",
			"([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+ \\. [a-zA-Z0-9]+ \\(\\d+\\)", "... \\d+ more");

	public static boolean isStackTraceLine(String txtLine) {
		return matchRegexes(txtLine, stackTracesRegexes);
	}

	private static boolean matchRegexes(String sent, List<String> regexes) {

		if (regexes == null) {
			return false;
		}

		boolean anyMatch = regexes.stream().anyMatch(regex -> sent.matches("(?s).*" + regex + ".*"));

		return anyMatch;
	}

}
