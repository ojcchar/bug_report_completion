package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class MenuNavigationPMTest extends BaseTest {

	public MenuNavigationPMTest() {
		pm = new MenuNavigationPM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = {
				"Adding a batch file (.bat) as a helper application seems to break the Download Actions component (in Tools | Options | Downloads | View & Edit Actions)." };
		TestUtils.testSentences(txts, pm, 1);

	}

	@Test
	public void testNegative() throws Exception {

		String[] txts = {
				"In the CVS Repository Exploring perspective, if you select \"Compare With\" on a\n	branch or version, it eventually opens a \"Structured Compare\" window showing all\n	the changes.",
				"This means you cannot benefit from the Synchronize view's changeset\nsupport -- you can access diffs, but no commit log information.",
				"When I run an sql query, I cant´t see the number of updated rows (in insert,\nupdate, delete) or the number of selected rows (in select).",
				"The commit failed because the command claimed that several files were not up-to-date.",
				"However, it would be great if upon a double click there, the Project\nproperties-Java Build Path-Libraries would open the concerned library where I\ncould attach the source",
				"However_ the  New title set within....  is the title on the page as the fbml parser is ignoring the fb:js-string tag and using the title encapsulated within\n    &lt;fb:title&gt;Initial Title&lt;/fb:title&gt;\n    &lt;fb:js-string var= newtitle &gt;\n        &lt;fb:title&gt;New title set within fb js-string variable&lt;/fb:title&gt;\n    &lt;/fb:js-string&gt;",
				"When I ran htpasswd a Segment Fault occurr.", "| SSD | btrfs | -s btrfs | 8s |",
				"echo \"echo hi\" | docker run -i ubuntu | print" };

		TestUtils.testSentences(txts, pm, 0);

	}

}
