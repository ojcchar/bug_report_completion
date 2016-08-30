package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class LabeledListPMTest extends BaseTest {

	public LabeledListPMTest() {
		pm = new LabeledListPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"1 The user starts Eclipse on a workspace which already has a connection profile defined.  They have not set a password for the database.  The Data Source Explorer has not been activated, so the connection profiles have not been loaded.\n2 The user brings up the wizard to create a new JPA project, chooses the connection from the dropdown, and clicks connect.  This fails, because the password hasn't been set.\n3 They cancel the wizard, go to the DSE view and set the password, and connect to the database.\n4 They go back into the wizard to create a new JPA project, but the connection still fails, with an error indicating the password wasn't specified.",
				"- Typed in an identifier for an interface that didn't exist.\n- Quick Fix (Ctrl-1), accidentally chose Create Class\n- I'm pretty sure I hit Enter twice but then was able to select Cancel\n- Within the editor, I couldn't open the new class but I could no longer create it again because it already existed\n- Checking the filesystem, the Java file was there\n- Deleting the file fixed the problem" };

		TestUtils.testParagraphs(txts, pm, 1);
	}
}
