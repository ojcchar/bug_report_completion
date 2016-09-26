package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class LeadsToPMTest extends BaseTest {

	public LeadsToPMTest() {
		pm = new LeadsToPM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"Opening of .docx files containing furigana results in the omission of any characters which have furigana associated with them.",
				"This block leads to other content to be pushed down in the plugin." };
		TestUtils.testSentences(sentences, pm, 1);
	}

}
