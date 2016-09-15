package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ActionsPastPMTest extends BaseTest {

	public ActionsPastPMTest() {
		pm = new ActionsPastPM();
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = {
				"I ran: java -jar standalone-1.1.1.jar \n" + "The log from the launch utility can be see here:\n"
						+ "http://paste.pocoo.org/show/545980/\n"
						+ "I then tried manually from that utility: File.Launch-Browser.\n" + "the log is here:\n"
						+ "http://paste.pocoo.org/show/545982/",
				"steps to reproduce: \n" + "I ran: java -jar standalone-1.1.1.jar \n"
						+ "The log from the launch utility can be see here:\n" + "http://paste.pocoo.org/show/545980/\n"
						+ "I then tried manually from that utility: File.Launch-Browser.\n" + "the log is here:\n"
						+ "http://paste.pocoo.org/show/545982/" };
		TestUtils.testParagraphs(txts, pm, 0);
	}

}
