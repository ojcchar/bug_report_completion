package seers.bugreppatterns.main;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class BaseTest {

	protected PatternMatcher pm;
	protected boolean testSentence = true;
	protected String patternName;
	protected List<String> testDataParagraph = new ArrayList<>();
	protected List<String> testDataSentence = new ArrayList<>();

	@Before
	public void beforeTest() throws Exception {
		if (pm == null) {
			System.out.println("No pattern matcher!");
			return;
		}

		if (patternName == null) {
			findPatternName();

			if (patternName.startsWith("P_")) {
				testSentence = false;
			}
			loadData();
		}
	}

	private void loadData() throws IOException {
		CsvParser csvParser = new CsvParserBuilder().separator(';').multiLine(true).build();
		try (CsvReader csvReader = new CsvReader(new FileReader(MainMatcher.fileAssignment), csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			allLines.forEach(sentence -> {
				String paragraphTxt = sentence.get(3);
				String sentenceTxt = sentence.get(4);

				String pattern1 = sentence.get(8).trim();
				String pattern2 = sentence.get(9).trim();
				String pattern3 = sentence.get(10).trim();

				String[] patternsNoTesting = sentence.get(14).split(",");

				boolean addSentence = true;
				boolean addParagraph = true;
				if (Arrays.stream(patternsNoTesting).anyMatch(pat -> pat.trim().equalsIgnoreCase(patternName))) {
					if (testSentence) {
						addSentence = false;
						addParagraph = true;
					} else {
						addSentence = true;
						addParagraph = false;
					}
				}

				// String instanceId = sentence.get(14);

				if (patternName.equalsIgnoreCase(pattern1) || patternName.equalsIgnoreCase(pattern2)
						|| patternName.equalsIgnoreCase(pattern3)) {
					// no titles
					// if (!instanceId.startsWith("0")) {
					if (addParagraph) {
						testDataParagraph.add(paragraphTxt);
					}
					if (addSentence) {
						testDataSentence.add(sentenceTxt);
					}
					// }
				}
			});

		}

		if (testDataParagraph.isEmpty() || testDataSentence.isEmpty()) {
			throw new RuntimeException("No testing data for " + pm.getClass().getSimpleName());
		}

		// assertFalse("No testing data!", testDataParagraph.isEmpty());
		// assertFalse("No testing data!", testDataSentence.isEmpty());
	}

	private void findPatternName() throws IOException {
		List<String> allPatterns = FileUtils.readLines(new File("patterns.csv"));

		for (String patternNameClassIndex : allPatterns) {

			String[] split = patternNameClassIndex.split(" ");

			String patternNameClass = split[0];

			String[] split2 = patternNameClass.split(";");
			String pName = split2[0];
			String className = split2[1];

			String simpleName = pm.getClass().getSimpleName();
			if (simpleName.equals(className)) {
				patternName = pName;
				return;
			}

		}

		throw new RuntimeException("Pattern not found!");

	}

	@Test
	public void testMatchSentence() throws Exception {

		if (pm == null || !testSentence) {
			return;
		}

		System.out.println();
		System.out.println("Testing pattern (labeled sentences): " + pm.getClass().getSimpleName());

		int numPasses = 0;
		for (int i = 0; i < testDataSentence.size(); i++) {
			String txt = testDataSentence.get(i);

			txt = txt.replaceFirst("(\\[.+\\] )(.+)", "$2");
			txt = txt.replace("\"\"", "\"").replace("&lt;", "<").replace("&gt;", ">").replace("&apos;", "'")
					.replace("&amp;", "&").replace("&quot;", "\"");

			try {

				Sentence sentence = SentenceUtils.parseSentence("o", txt);
				int m = pm.matchSentence(sentence);
				if (m != 1) {
					System.out.println("\n Fail for (" + i + "): \"" + txt + "\"");
					// pm.matchSentence(sentence);
				} else {
					numPasses++;
				}
			} catch (Exception e) {
				System.out.println("\n Fail for (" + i + "): \"" + txt + "\"");
				System.out.println();
				e.printStackTrace();
			}
		}

		if (numPasses != testDataSentence.size()) {
			fail("Only " + numPasses + " out of " + testDataSentence.size() + " tests passed!");
		} else {
			System.out.println("Success: " + numPasses + " cases passed!");
		}
	}

	@Test
	public void testMatchParagraph() throws Exception {

		if (pm == null || testSentence) {
			return;
		}

		System.out.println();
		System.out.println("Testing pattern (labeled paragraphs): " + pm.getClass().getSimpleName());

		int numPasses = 0;
		for (int i = 0; i < testDataParagraph.size(); i++) {
			String txt = testDataParagraph.get(i);

			txt = txt.replaceFirst("(\\[.+\\] )(.+)", "$2");
			txt = txt.replace("\"\"", "\"").replace("&lt;", "<").replace("&gt;", ">").replace("&apos;", "'")
					.replace("&amp;", "&").replace("&quot;", "\"");

			Paragraph paragraph = parseParagraph(txt);

			int m = pm.matchParagraph(paragraph);
			if (m == 0) {
				System.out.println("\n Fail for (" + i + "): \"" + txt + "\"");
				// pm.matchSentence(sentence);
			} else {
				numPasses++;
			}
		}

		if (numPasses != testDataParagraph.size()) {
			fail("Only " + numPasses + " out of " + testDataParagraph.size() + " tests passed!");
		} else {
			System.out.println("Success: " + numPasses + " cases passed!");
		}
	}

	public static Paragraph parseParagraph(String txt) {

		Paragraph paragraph = new Paragraph("0");
		String[] lines = txt.split("\n");
		for (String line : lines) {

			if (line.isEmpty()) {
				continue;
			}

			// bullets normalization
			if (line.matches("(\\W*)\\d((\\.\\))|\\.|\\)|,|-)(.*)")) {
				line = line.replaceFirst("((\\.\\))|\\.|\\)|,|-)\\s*", " ");
			}

			List<Sentence> sentences = TextProcessor.processText(line, true);

			paragraph.addSentences(sentences);
		}

		return paragraph;

	}

}
