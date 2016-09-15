package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class CodeRefPMTest extends BaseTest {

	public CodeRefPMTest() {
		pm = new CodeRefPM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = { "Created attachment 56395\nInvalidated file" };
		TestUtils.testSentences(txts, pm, 1);

	}

	@Test
	public void testNegative() throws Exception {

		String[] txts = { "Created attachment 56395\n", "My code is nice", "Currently there is code in `/utils/daemon.",
				"I was able to checkout the code install go and run make in the root directory to make the build a few days ago." };
		TestUtils.testSentences(txts, pm, 0);
	}

}
