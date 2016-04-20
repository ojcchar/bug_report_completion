package seers.bugreppatterns.pattern.sr;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class MenuNavigationPMTest extends BaseTest {

	public MenuNavigationPMTest() {
		pm = new MenuNavigationPM();
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
				"When I ran htpasswd a Segment Fault occurr." };

		boolean fail = false;
		for (int i = 0; i < txts.length; i++) {
			String txt = txts[i];
			List<Sentence> sentences = TextProcessor.processText(txt);
			int m = pm.matchSentence(new Sentence("0", TextProcessor.getAllTokens(sentences)));
			if (m != 0) {
				System.out.println("\n Fail for (" + i + "): \"" + txt + "\"");
				fail = true;
			}
		}

		assertFalse(fail);
	}

}
