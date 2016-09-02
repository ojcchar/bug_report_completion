package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class WhyPMTest extends BaseTest {

	public WhyPMTest() {
		pm = new WhyPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"I can't understand why the outgoing changes didn't appear in the first place since the files were decorated properly in the navigator.",
				"Why is it that when I go from my home page to a profile page and then back to the home page using the upper right links_ I get a URL that looks something like http://www.facebook.com/profile.php?id=14832273&amp;ref=nf#/home.php",
				"i am the developer of the application a church from lebanon _ but why do i get an error box everytime i try to send a pic?",
				"Why do the spellchecker doesn't use html/xml language code of the page rendered to automatically select the appropriate dictionary on a page?",
				"Now the question: Why doesn't the query cache update the objects returned in step 2?" };

		TestUtils.testSentences(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = {
				"If Lobs in stateless sessions isn't supported, then at very least an error message to that effect should be thrown and the docs should be updated to reflect this, but I don't see why that should be the case, especially considering that both Lobs and stateless session both serve very similar purposes, they are for use when dealing with large datasets that may not fit in memory, and so could well be used together.",
				" +++ This bug was initially created as a clone of Bug #305858 +++ &gt; Apart from the NPE there seems to be no good reason why null should not be &gt; allowed." };
		TestUtils.testSentences(txts, pm, 0);
	}

}
