package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ConditionalSequenceParagrahPMTest extends BaseTest {

	public ConditionalSequenceParagrahPMTest() {
		pm = new ConditionalSequenceParagrahPM();
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = {
				"What is happening is that even though that file is being updated with a new CRL file every hour, only a full restart (stop, start) of Apache makes it re-read the CRL!\n"
						+ "If we send a HUP, it doesn't re-read it.",
				"I was able to checkout the code install go and run make in the root directory to make the build a few days ago. Now when I run make in the root of the repo I get the error in [1]. I believe this commit is the source of the problem: https://github.com/dotcloud/docker/commit/37a78902db9e968d307d0c0325612e8bef20bc69"

		};

		TestUtils.testParagraphs(txts, pm, 0);

	}

}
