package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class KeepVerbPMTest extends BaseTest {
	public KeepVerbPMTest() {
		pm = new KeepVerbPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {};

		TestUtils.testSentences(sentences, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"Unfortunately the current implementation forces us to keep using the old unescaped URLs, and does not allow all valid (=\"encoded\"/\"escaped\") URIs.",
				"Images keep disappearing from profile boxes after like 30 minutes.",
				"Actual Results: The page loading wheel in the upper right hand corner keeps turning as if still page loading .",
				"You close it but it keeps coming back.",
				"where group is empty, even though I'm(vinita) in the user list, I keep getting \"\"user vinita not in right group\"\" when fastcgi is loaded and using AuthDBMType SDBM",
				"Which causes that hibernate scheema generator keeps appending to ddl file.",
				"It kept saying that there were no comments though." };

		TestUtils.testSentences(sentences, pm, 1);
	}
}
