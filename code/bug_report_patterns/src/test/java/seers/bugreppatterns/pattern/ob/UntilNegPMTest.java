package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class UntilNegPMTest extends BaseTest{

	public UntilNegPMTest(){
		pm = new UntilNegPM();
	}
	
	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"the changes won't happen until they restart, and offer to restart now."};
		TestUtils.testSentences(txts, pm, 1);
	}
	
	@Test
	public void testNegatives() throws Exception {
		String [] txts = {"The ability to `docker load` xz-compressed images has been broken with docker 1.3.2 (it was working until docker 1.3.1 included).", 
				"this hangs until you hit ENTER",
				"(*)In this case the size of the this particular style must be reduced until to the right text is showed correctly.",
				"If a patient has typed something in that box, it shouldn't allow the form to be submitted until the matching patient names has been clicked in the box.",
				"When adding a regimen , the regimen does not appear , until the page is refreshed .",
				"However the post was still visible on the screen until the post list refreshed.",
				"I think the location bar should shrink until the size = icon size+ 2* border size // for left and right borders.",
				"i think you must set the localization bar until the dimension is not setted"};
		TestUtils.testSentences(txts, pm, 0);
	}
}
