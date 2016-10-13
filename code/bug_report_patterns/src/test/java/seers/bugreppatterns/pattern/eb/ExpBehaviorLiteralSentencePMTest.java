package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ExpBehaviorLiteralSentencePMTest extends BaseTest {

	public ExpBehaviorLiteralSentencePMTest() {
		pm = new ExpBehaviorLiteralSentencePM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"Expected Results: \nshortened the box to fit within the viewable screen, or opened it in the other\ndirection (up).",
				"Expected: Hit the space where the version inof is.",
				"### Expected behavior\nReaderPost model should have a new attribute that represents the sort order weight for a ReaderPost instance" };

		TestUtils.testSentences(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = {
				"My Expected Results are: \nshortened the box to fit within the viewable screen, or opened it in the other\ndirection (up).",
				"5 Result: you get some 'garbage' text instead of expected results:", "Expected behavior",
				"### Expected behaviour", "expected", "expected:",
				"Expected http://specs.openid.net/auth/2.0/identifier_select_ got https://secure.mp/s/anthony.mp/server" };
		TestUtils.testSentences(txts, pm, 0);

	}
}
