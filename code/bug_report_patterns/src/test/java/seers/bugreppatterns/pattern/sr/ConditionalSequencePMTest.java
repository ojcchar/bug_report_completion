package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ConditionalSequencePMTest extends BaseTest {

	public ConditionalSequencePMTest() {
		pm = new ConditionalSequencePM();
	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = {
				"If you could make the tag linked= false  available_ then I could access the network name without the link." };
		TestUtils.testParagraphs(txts, pm, 0);

	}

}
