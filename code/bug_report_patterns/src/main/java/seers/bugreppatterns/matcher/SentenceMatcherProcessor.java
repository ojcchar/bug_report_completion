package seers.bugreppatterns.matcher;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.CodedDataEntry;
import seers.bugreppatterns.entity.xml.BugReport;
import seers.bugreppatterns.entity.xml.DescriptionParagraph;
import seers.bugreppatterns.entity.xml.DescriptionSentence;
import seers.bugreppatterns.main.validation.MainMatcher;
import seers.bugreppatterns.main.validation.MainMatcher.GoldSetClasses;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class SentenceMatcherProcessor extends ThreadProcessor {

	private List<CodedDataEntry> sentences;

	@SuppressWarnings("unchecked")
	public SentenceMatcherProcessor(ThreadParameters params) {
		super(params);
		sentences = params.getParam(List.class, ThreadExecutor.ELEMENTS_PARAM);
	}

	@Override
	public void executeJob() throws Exception {
		for (CodedDataEntry sentence : sentences) {

			boolean error = checkSentencePattern(sentence);

			if (error) {
				continue;
			}

			String project = sentence.project;
			String bugId = sentence.bugId;
			String fileToRead = "test_data" + File.separator + "data" + File.separator + project + "_parse"
					+ File.separator + bugId + ".xml.parse";

			try {
				BugReport bug = XMLHelper.readXML(BugReport.class, fileToRead);

				checkSentence(sentence, bug);
			} catch (Exception e) {
				LOGGER.error("Error for file: " + fileToRead, e);
			}

		}
	}

	private boolean checkSentencePattern(CodedDataEntry sentence) {

		String project = sentence.project;
		String bugId = sentence.bugId;
		String instanceId = sentence.entryId;
		String paragraphTxt = sentence.paragraphTxt;
		String sentenceTxt = sentence.sentenceTxt;

		if (paragraphTxt.trim().isEmpty()) {

			MainMatcher.conflictsWriter.writeNext(
					Arrays.asList(new String[] { "paragraph", project, bugId, instanceId, "paragraph empty" }));
			return true;
		}

		String cl1 = sentence.isObsBehavior ? "x" : "";
		String cl2 = sentence.isExpecBehavior ? "x" : "";
		String cl3 = sentence.isStepsToRepro ? "x" : "";

		if ((cl1 + cl2 + cl3).trim().isEmpty()) {

			MainMatcher.conflictsWriter.writeNext(
					Arrays.asList(new String[] { "sentence", project, bugId, instanceId, "no class assigned" }));
			return true;
		}

		String pattern1 = sentence.pattern1;
		String pattern2 = sentence.pattern2;
		String pattern3 = sentence.pattern3;
		String pattern4 = sentence.pattern4;

		if ((pattern1 + pattern2 + pattern3 + pattern4).trim().isEmpty()) {
			MainMatcher.conflictsWriter
					.writeNext(Arrays.asList(new String[] { "sentence", project, bugId, instanceId, "need pattern" }));
		} else {

			if (sentenceTxt.trim().isEmpty()) {
				if (pattern1.trim().startsWith("S_") || pattern2.trim().startsWith("S_")
						|| pattern3.trim().startsWith("S_") || pattern4.trim().startsWith("S_")) {
					MainMatcher.conflictsWriter.writeNext(Arrays
							.asList(new String[] { "sentence", project, bugId, instanceId, "pattern assignment" }));
					return true;
				}
			} else {
				if (!(pattern1.trim().startsWith("S_") || pattern2.trim().startsWith("S_")
						|| pattern3.trim().startsWith("S_") || pattern4.trim().startsWith("S_"))) {
					MainMatcher.conflictsWriter.writeNext(Arrays.asList(
							new String[] { "sentence", project, bugId, instanceId, "possible pattern assignment" }));
				}
			}
		}

		return false;
	}

	private void checkSentence(CodedDataEntry sentence, BugReport bug) {

		String project = sentence.project;
		String bugId = sentence.bugId;
		String instanceId = sentence.entryId;
		String paragraphTxt = sentence.paragraphTxt;
		String sentenceTxt = sentence.sentenceTxt;

		String cl1 = sentence.isObsBehavior ? "x" : "";
		String cl2 = sentence.isExpecBehavior ? "x" : "";
		String cl3 = sentence.isStepsToRepro ? "x" : "";

		paragraphTxt = paragraphTxt.replace("\"\"", "\"").replace("&lt;", "<").replace("&gt;", ">")
				.replace("&apos;", "'").replace("&amp;", "&").replace("&quot;", "\"");
		sentenceTxt = sentenceTxt.replace("\"\"", "\"").replace("&lt;", "<").replace("&gt;", ">").replace("&apos;", "'")
				.replace("&amp;", "&").replace("&quot;", "\"");

		if (sentenceTxt != null && !sentenceTxt.trim().isEmpty()) {

			processSentence(bug, project, bugId, instanceId, sentenceTxt, cl1, cl2, cl3);
		} else {
			processParagraph(bug, project, bugId, instanceId, paragraphTxt, cl1, cl2, cl3);
		}
	}

	private void processParagraph(BugReport bug, String project, String bugId, String sentenceId, String paragraphTxt,
			String cl1, String cl2, String cl3) {
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
				} else {

					try {
						String[] split = paragraphTxt.split("\n");
						String firstTxt = split[0];
						String lastTxt = split[split.length - 1];

						// to tackle the normalization of lists 1. asdfas --> 1
						// asdfas
						if (firstTrim.length() > 4) {
							firstTrim = firstTrim.substring(4, firstTrim.length());
						}

						if (firstTxt.trim().endsWith(firstTrim) && lastTxt.trim().endsWith(lastTrim)) {

							parId = p.getId();
							break;
						}
					} catch (IndexOutOfBoundsException e) {
					}
				}
			}
		}

		if (parId != null) {
			MainMatcher.matchedWriterParagraphs
					.writeNext(Arrays.asList(new String[] { project, bugId, sentenceId, parId, cl1, cl2, cl3 }));

			// no titles for the gold set
			if (!parId.equals("0")) {

				updateGoldSets(project, bugId, parId, cl1, cl2, cl3);
			}
		} else {
			MainMatcher.conflictsWriter
					.writeNext(Arrays.asList(new String[] { "paragraph", project, bugId, sentenceId, "mismatch" }));
		}
	}

	private void processSentence(BugReport bug, String project, String bugId, String sentenceId, String sentenceTxt,
			String cl1, String cl2, String cl3) {
		String parId = null;
		String sentId = null;

		String title = bug.getTitle().trim();

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
				} else {
					first = sentences2.stream().filter(s -> {
						String trim = sentenceTxt.trim();
						String trim2 = s.getValue().trim();
						return trim2.toLowerCase().contains(trim.toLowerCase());
					}).findFirst();
					if (first.isPresent()) {
						parId = p.getId();
						sentId = first.get().getId();
						break;
					} else {
						List<Sentence> stncs = TextProcessor.processText(sentenceTxt);
						String stringSentc = TextProcessor.getStringFromSentences(stncs);
						first = sentences2.stream().filter(s -> {
							List<Sentence> stncs2 = TextProcessor.processText(s.getValue());
							String stringSentc2 = TextProcessor.getStringFromSentences(stncs2);
							return stringSentc2.toLowerCase().contains(stringSentc.toLowerCase());
						}).findFirst();
						if (first.isPresent()) {
							parId = p.getId();
							sentId = first.get().getId();
							break;
						}
					}
				}
			}
		}

		if (parId != null && sentId != null) {
			MainMatcher.matchedWriterSentences.writeNext(
					Arrays.asList(new String[] { project, bugId, sentenceId, parId, sentId, cl1, cl2, cl3 }));

			// no titles for the gold set
			if (!parId.equals("0")) {

				MainMatcher.goldSetWriterSentences
						.writeNext(Arrays.asList(new String[] { project, bugId, sentId, cl1, cl2, cl3 }));

				updateGoldSets(project, bugId, parId, cl1, cl2, cl3);
			}

		} else {
			MainMatcher.conflictsWriter
					.writeNext(Arrays.asList(new String[] { "sentence", project, bugId, sentenceId, "mismatch" }));
		}
	}

	private void updateGoldSets(String project, String bugId, String parId, String cl1, String cl2, String cl3) {
		//
		// if ("155526".equalsIgnoreCase(bugId)) {
		// List<String> asList = Arrays.asList(new String[] { project, bugId,
		// parId, cl1, cl2, cl3 });
		// System.out.println(asList);
		// }

		MainMatcher.GoldSetParagraph key = new MainMatcher.GoldSetParagraph(project, bugId, parId);
		GoldSetClasses classesParagraph = MainMatcher.goldSetParagraphs.get(key);
		if (classesParagraph == null) {
			classesParagraph = new MainMatcher.GoldSetClasses();
		}

		classesParagraph.ob = classesParagraph.ob.isEmpty() ? cl1 : classesParagraph.ob;
		classesParagraph.eb = classesParagraph.eb.isEmpty() ? cl2 : classesParagraph.eb;
		classesParagraph.sr = classesParagraph.sr.isEmpty() ? cl3 : classesParagraph.sr;

		MainMatcher.goldSetParagraphs.put(key, classesParagraph);

		// ------------------------------------

		MainMatcher.GoldSetDoc key2 = new MainMatcher.GoldSetDoc(project, bugId);
		GoldSetClasses classesDoc = MainMatcher.goldSetDocs.get(key2);
		if (classesDoc == null) {
			classesDoc = new MainMatcher.GoldSetClasses();
		}

		classesDoc.ob = classesDoc.ob.isEmpty() ? cl1 : classesDoc.ob;
		classesDoc.eb = classesDoc.eb.isEmpty() ? cl2 : classesDoc.eb;
		classesDoc.sr = classesDoc.sr.isEmpty() ? cl3 : classesDoc.sr;

		MainMatcher.goldSetDocs.put(key2, classesDoc);
	}

}
