package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class SimplePresentSubordinatesPMTest extends BaseTest {

	public SimplePresentSubordinatesPMTest() {
		pm = new SimplePresentSubordinatesPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				// "- I do a Java Search and sort the results by Parent Path"
				// "i create an application and i set it in a tab fan page. "
				"On the Tomcat / mod_jk side I use:" };

		TestUtils.testSentences(txts, pm, 1);
	}

}
