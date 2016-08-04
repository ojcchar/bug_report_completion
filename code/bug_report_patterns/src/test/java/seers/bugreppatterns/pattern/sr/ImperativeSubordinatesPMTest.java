package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ImperativeSubordinatesPMTest extends BaseTest {

	public ImperativeSubordinatesPMTest() {
		pm = new ImperativeSubordinatesPM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = { "Open the url http://www.buyanorgan.net/test.xml" };
		TestUtils.testParagraphs(txts, pm, 1);

	}

}
