package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class CouldPMTest extends BaseTest {

	public CouldPMTest() {
		pm = new CouldPM();
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = { "(The command \"written\" couldn`t be processed on the memory.)",
				"WARN - InitializationFilter.doPost(485) |2009-05-27 23:43:44,964| Implementation ID could not be set.",
				"My app (actually the app for a client of us) is approved now but it is not showing up in any of the category pages of the directory either could you find it via search.",
				"I searched the bug database and the documentation but could not find any hint.",
				"The closest related bug I could find was 39243.",
				"I could manually handle the index values, but it would be so much easier to let Hibernate do it."};
		TestUtils.testSentences(txts, pm, 0);
	}

}
