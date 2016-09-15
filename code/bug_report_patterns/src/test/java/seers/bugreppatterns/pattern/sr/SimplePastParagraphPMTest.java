package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class SimplePastParagraphPMTest extends BaseTest {

	public SimplePastParagraphPMTest() {
		pm = new SimplePastParagraphPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = { "I ran: java -jar standalone-1.1.1.jar \n"
				+ "The log from the launch utility can be see here:\n" + "http://paste.pocoo.org/show/545980/\n"
				+ "I then tried manually from that utility: File.Launch-Browser.\n" + "the log is here:\n"
				+ "http://paste.pocoo.org/show/545982/" };
		TestUtils.testParagraphs(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = { "- Typed in an identifier for an interface that didn't exist.\n"
				+ "- Quick Fix (Ctrl-1), accidentally chose Create Class\n"
				+ "- I'm pretty sure I hit Enter twice but then was able to select Cancel\n"
				+ "- Within the editor, I couldn't open the new class but I could no longer create it again because it already existed\n"
				+ "- Checking the filesystem, the Java file was there\n" + "- Deleting the file fixed the problem" };
		TestUtils.testParagraphs(txts, pm, 0);
	}

}
