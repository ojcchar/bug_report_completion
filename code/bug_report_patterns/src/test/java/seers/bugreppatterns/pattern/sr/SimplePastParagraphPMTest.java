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
		String[] txts = {
		
				"I copied common/headerhtml to common/headerhtm, and added some text to header.htm to distinguish it from header.html.  Upon reloading the page (which calls for header.html), the file header.htm is loaded instead.",
				"I modified the default configs to point to my existing htdocs and cgi-bins, but using the default server root, to avoid conflict with the existing server.\n"+"I shut down the existing server and started up Apache 2 with \"apachectl startssl\"."				
		};
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
