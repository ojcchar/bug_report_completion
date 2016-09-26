package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class FoundPMTest extends BaseTest {
	public FoundPMTest() {
		pm = new FoundPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negs = {
				"It seems to be looking for a random number generator, but I can't seem to figure out how to get it find one.",
				"Finally, 'openssl verify' does not find anything amiss with the CA chains.",
				"But i can not find the tag of it's image (5.6.22), any reason why this info is not part of \"docker inspect\" cmd result ?",
				"It seems can't find correct driver now",
				"I cannot find where this constraint is created, but it is present in all of my 1.8.2 instances.",
				"For example if a person was called Sarah Lee Smith and her first name is \"Sarah Lee\" then if you searched for Sarah Lee Smith you would not find her, you would need to leave out the Lee or have made Lee a middle name when it was created",
				"Found Similar People yields excessive results when adding a new person",
				"The file /http://download.eclipse.org/updates/info/siteInfo.html cannote be found",
				"This should contain the targets found in the top-level Makefile.",
				"Domain class not found when creating JAXBContext", "App page giving Page Not Found errors suddenly",
				"It seems that the img parameter isn t recognized at all because the reference to the thumb image cannot be found in the resulting html embed code.",
				"The user gets a notification_ however no comment is found.",
				"If no browser is open it will only open a blank page, and if a window is open, it will say that the file ...htm could not be found." };

		TestUtils.testSentences(negs, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] negs = {};

		TestUtils.testSentences(negs, pm, 1);
	}

}
