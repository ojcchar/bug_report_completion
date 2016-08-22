package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ExpBehaviorLiteralMultiSentencePMTest extends BaseTest {

	public ExpBehaviorLiteralMultiSentencePMTest() {
		pm = new ExpBehaviorLiteralMultiSentencePM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = {
				"### Expected results\n\n`dmesg -c` in containers returns `Operation not permitted` and\n`dmesg` in host prints kernel ring buffer.",
				"### Expected behavior\nIt should be possible to open a saved draft in the editor and schedule it to be published at a future date." };
		TestUtils.testParagraphs(txts, pm, 1);
	}

}
