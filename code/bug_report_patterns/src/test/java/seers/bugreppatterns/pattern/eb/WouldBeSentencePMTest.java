package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class WouldBeSentencePMTest extends BaseTest {

	public WouldBeSentencePMTest() {
		pm = new WouldBeSentencePM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = { "The error message would be more helpful if it said:", 
				"It would be nice if there was a horizontal slider that let a person flip quickly to at least the general area."};
		TestUtils.testSentences(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = { "Ideally, any code using these duplicates would be checked and fixed if necessary",
				"Also, it would be impossible to map polymorphic associations",
				"I could manually handle the index values, but it would be so much easier to let Hibernate do it." };
		TestUtils.testSentences(txts, pm, 0);

	}

}
