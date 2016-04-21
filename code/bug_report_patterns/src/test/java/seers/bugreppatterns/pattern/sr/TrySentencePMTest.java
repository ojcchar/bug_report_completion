package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class TrySentencePMTest extends BaseTest {

	public TrySentencePMTest() {
		pm = new TrySentencePM();
	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = { "If I try to open a .htm file it will report that it\r\ntried to open a ." };
		TestUtils.testSentences(txts, pm, 0);

	}

}
