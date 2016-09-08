package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ShouldPMTest extends BaseTest {

	public ShouldPMTest() {
		pm = new ShouldPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"If I create a new category from within the app it seems like it should be checked in the categories list by default." };
		TestUtils.testSentences(txts, pm, 1);
	}
}
