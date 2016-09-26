package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ButNegativePMTest extends BaseTest {

	public ButNegativePMTest() {
		pm = new ButNegativePM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = { "BUT, type entry into NAV bar, hit ENTER -- no response.", "But I can't do this.",
				"I can, however, remove the mistake identifier via the long form by voiding it.",

				"Sometimes, the Variables view shows constants although the option is turned off.",
				"A new outgoing task with the name \"New Task\" is created although a different summary was cloned.",
				"when i use FOP to generate my report it took 3 second but in birt it took 45 seconds that alot of difference 45 seconds exclude birt engine start up and the report already optimize in layout to avoid slowness only needed field are bind over the xml to the data binding and tables",
				"My home page is Google and after it loads the page the wheel stops turning (good) but if I go to any web site then close that web site's tab to return to Google the page loading wheel never stops as if the page is still trying to load and it is not (I don't think).",
				"After the configuration I've started it and it's running but if I want to connect to the server my client tells me that it's unable to connect to the remote host.",
				"In this example the `strace` output shows that `\"Entrypoint\": [\"/bin/bash\"-l\"]` was sent to the server, but inspecting the resulting image shows `\"Entrypoint\": null`.",
				"* I was expecting `--quiet` to suppress that output, but it only removes the output from commands run in containers, not the rest of the client's output.",
				"However, when I create a new Bestellung object, then the column for @OrderColumn isn't updated.",
				"Issue HHH-3965 partly resolved this issue for non-nationalized characters by mapping CLOB to VARCHAR(MAX); however under the {SQLServer2008Dialect} which extends {SQLServer2005Dialect}, using the @Lob and @Nationalized annotations results in mapping to a NCLOB data type which doesn't exist for SQL Server.",
				"When saving to ODT a modified document from any other format (DOC or RTF being most often with me) the Save As dialog doesn't pre-fill the 'File name' field with the original DOC filename leaving it empty, but only if no other ODT documents being already in the destination folder present.",
				"I tried to untick the box \"Always with current page\" (for that is what I want) to see if the funtion is inverted but the tick remains in place, as if not changed at all.",
				"However, the validator only tests against non-retired workflows, which I believe is in error.",
				"I then went back to the same short form to edit the identifer and saved again, but going back to the form again there are now two identifiers (of the same type) with the old value and the new value respectively." };
		TestUtils.testSentences(sentences, pm, 1);

		@SuppressWarnings("unused")
		String[] failingSentences = {
				"However, if I go back to my bookmarks it shows the top of the bookmarks list, rather than being scrolled down to the bottom of the list, where I was before.  " };
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = { "But it does move -- it moves 125 pixels down from where it was originally." };
		TestUtils.testSentences(sentences, pm, 0);
	}
}
