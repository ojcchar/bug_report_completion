package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class UsedToPMTest extends BaseTest {

	public UsedToPMTest() {
		pm = new UsedToPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {
				"Could not background jobs be used to do the filling, so that even if my workspace is big and I left it open on a package explorer it starts up faster?",

				"The following CGI script (post.cgi) can be used to accept the POST data:",
				"instead, the CWD command can be used to test if the entity is directory or not, Reducing the number of network transactions required for a directory listing.",

				"But the algorithm that tries to determine whether the table should be used to emulate the sequence lets the dialects that supports sequences but not pooled sequences to slip through.",
				"If Lobs in stateless sessions isn't supported, then at very least an error message to that effect should be thrown and the docs should be updated to reflect this, but I don't see why that should be the case, especially considering that both Lobs and stateless session both serve very similar purposes, they are for use when dealing with large datasets that may not fit in memory, and so could well be used together.",
				"When I use entityManager.persist(), it works fine as with actual instance passed to persist() is used to invoke callback method.",
				"With version4, key F9 is used to write formule or modify formula.",
				"Rephrase the whole thing to: \"A term that can be used to search for this concept, but that is not valid for display purposes (e.g. a common misspelling or a drug code name)",
				"The issue here is that if you want to do anything even vaguely research-related with a system, every patient should have a non-MRN ID that can be used to re-identify them later.", };

		TestUtils.testSentences(sentences, pm, 0);

		@SuppressWarnings("unused")
		String[] trickySentences = { "Command line used to start eclipse:",
				"Here is the **dockerfile** I used to create my gocd image", };
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"* no matter if you use annotation or mapping files, test will fail using javassist (it used to fail with previous version too)",
				"The Groovy module used to capture printed output and would return the evaluated result iff nothing was printed.",
				"Reader: change endpoint used to request single reader post" };

		TestUtils.testSentences(sentences, pm, 1);
	}
}
