package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 8/15/16.
 */
public class TimeAdverbPositivePMTest extends BaseTest {
	{
		pm = new TimeAdverbPositivePM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {

				"With the introduction of the function: ap_register_rewrite_mapfunc every Apache Module Writer can now create its own internal RewriteMap.", };
		TestUtils.testSentences(sentences, pm, 1);
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {
				"I then went back to the same short form to edit the identifer and saved again, but going back to the form again there are now two identifiers (of the same type) with the old value and the new value respectively." };
		TestUtils.testSentences(sentences, pm, 0);
	}
}
