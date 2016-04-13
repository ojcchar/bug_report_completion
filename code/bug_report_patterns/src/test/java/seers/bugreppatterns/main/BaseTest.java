package seers.bugreppatterns.main;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class BaseTest {

	protected PatternMatcher pm;
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

				String pattern1 = sentence.get(8);
				String pattern2 = sentence.get(9);
				String pattern3 = sentence.get(10);

				// String instanceId = sentence.get(14);

				if (patternName.equalsIgnoreCase(pattern1) || patternName.equalsIgnoreCase(pattern2)
						|| patternName.equalsIgnoreCase(pattern3)) {
					// no titles
					// if (!instanceId.startsWith("0")) {
					testDataParagraph.add(paragraphTxt);
					testDataSentence.add(sentenceTxt);
					// }
				}
			});

		}

		if (testDataParagraph.isEmpty() || testDataSentence.isEmpty()) {
			System.err.println("No testing data for " + pm.getClass().getSimpleName());
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

		if (pm == null) {
			return;
		}

		System.out.println("Testing pattern: " + pm.getClass().getSimpleName());

		int numPasses = 0;
		for (int i = 0; i < testDataSentence.size(); i++) {
			String txt = testDataSentence.get(i);

			txt = txt.replaceFirst("(\\[.+\\] )(.+)", "$2");

			System.out.print("Testing (positive) " + i);
			List<Sentence> sentences = TextProcessor.processText(txt);

			Paragraph paragraph = new Paragraph("0");
			paragraph.setSentences(sentences);

			Sentence sentence = new Sentence("0", paragraph.getTokens());
			int m = pm.matchSentence(sentence);
			if (m != 1) {
				System.out.println("\n Fail for: \"" + txt + "\"");
				// pm.matchSentence(sentence);
			} else {
				System.out.println(" PASSED");
				numPasses++;
			}
		}

		if (numPasses != testDataSentence.size()) {
			fail("Only " + numPasses + " out of " + testDataSentence.size() + " tests passed!");
		}
	}

}
