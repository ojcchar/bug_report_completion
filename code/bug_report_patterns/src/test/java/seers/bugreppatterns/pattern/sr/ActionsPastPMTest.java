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
						+ "http://paste.pocoo.org/show/545982/",
				"Hi_ we ve submitted our new apps on April 20th and it still shows that directory status is approval pending.. We ve checked on Forum that it ll take maximum a week to be approved but our apps still waiting to be approved although it is over 7 days.. 2 apps we submitted before was approved within 3 days_ so we think that it may caused by some sort of error or bug.. Please_ check our apps again and let us know if there is any problem or mistake we made on it.. Thanks.. ",
				"apache can't get UTF-8 get string when i request with <img \nsrc=\\http://a.com/bbsdata/ÇÑ±ÛÆÄÀ�?.jpg\"> tag.. (test2.html is a request document which data contains metatag set to utf-8). \"",

		};
		TestUtils.testParagraphs(txts, pm, 0);
	}

}
