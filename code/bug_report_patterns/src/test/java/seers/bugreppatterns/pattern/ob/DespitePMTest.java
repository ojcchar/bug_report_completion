package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class DespitePMTest extends BaseTest {

	public DespitePMTest() {
		pm = new DespitePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negatives = {
				"When saving a file (even if I only add a blank to it) I see a CPU consumption of 100% for about 1 - 2 sec on my machine.",
				"If the facebook user creates a new album ( even if there are no photo in it ) the api will work again_ returning all of the users photos and albums.",
				"Despite the JSON returning  likes  node_ the fields information still references a  fan_count  node.",
				"Even though I disable the sandbox mode for one of our apps the setting doesn t seem to affect the app." };

		TestUtils.testSentences(negatives, pm, 0);
	}
}
