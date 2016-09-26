package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ConditionalCodeParagraphPMTest extends BaseTest {

	public ConditionalCodeParagraphPMTest() {
		pm = new ConditionalCodeParagraphPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {};
		TestUtils.testSentences(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = {
				"While merging changes from my stream into HEAD, I had to merge a couple of conflicts. When I was prompted to save, it took &gt;15 seconds to perform the save. The icon did not change to an hourglass and for a second I thought that Eclipse was dying a slow death." };
		TestUtils.testSentences(txts, pm, 0);
	}

}
