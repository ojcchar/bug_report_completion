package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ConditionalThenSequencePMTest extends BaseTest {

	public ConditionalThenSequencePMTest() {
		pm = new ConditionalThenSequencePM();
	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = {
				"If you could make the tag linked= false  available_ then I could access the network name without the link." };
		TestUtils.testParagraphs(txts, pm, 0);

	}

}
