package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ExpBehaviorLiteralMultiSentencePMTest extends BaseTest {

	public ExpBehaviorLiteralMultiSentencePMTest() {
		pm = new ExpBehaviorLiteralMultiSentencePM();
	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = {
				"Expected: Both start and end range should be set as current date \nActual: Start date was set as current date and end range is mapped to immediate next date. And also Date time picker is shown for third time with current date set as default option." };
		TestUtils.testParagraphs(txts, pm, 0);
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = {
				"### Expected results\n\n`dmesg -c` in containers returns `Operation not permitted` and\n`dmesg` in host prints kernel ring buffer.",
				"### Expected behavior\nIt should be possible to open a saved draft in the editor and schedule it to be published at a future date." };
		TestUtils.testParagraphs(txts, pm, 1);
	}

}
