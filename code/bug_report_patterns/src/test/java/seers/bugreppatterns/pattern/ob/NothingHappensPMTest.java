package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class NothingHappensPMTest extends BaseTest {
	public NothingHappensPMTest() {
		pm = new NothingHappensPM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] negs = { "Nothing happened"};

		TestUtils.testSentences(negs, pm, 1);
	}
}
