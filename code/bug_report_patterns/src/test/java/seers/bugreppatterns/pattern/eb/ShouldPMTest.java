package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ShouldPMTest extends BaseTest {

	public ShouldPMTest() {
		pm = new ShouldPM();
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = { "3 It's also not clear that the children nodes should be removed after the parent",
				"Note that this should be done without creating a new fragment for each tag."};
		TestUtils.testSentences(txts, pm, 0);
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = { "Seems like there are two things that should be addressed.", "We should document:",
				"If I create a new category from within the app it seems like it should be checked in the categories list by default." };
		TestUtils.testSentences(txts, pm, 1);
	}
}
