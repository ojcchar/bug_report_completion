package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ObservePMTest extends BaseTest {
	public ObservePMTest() {
		pm = new ObservePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negs = {
				"When the performance hit is observed we notice that there are repeated continuous requests to our cross-domain receiver file (as passed into FB.init()).",
				"The Data Entry Statistics module (version 1.3) includes voided encounters and observations in the table of data entry statistics." };

		TestUtils.testSentences(negs, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] negs = {
				"We observed that in the free membership registration page all mandatory fields are not marked with *.",
				"However, I observe the same error with pure IPv4.",
				"Observed: Error response from daemon: Cannot start container ab59989666093c50b0f0b137096ff562c2cdd94f162b351b57a5fcd0217c7682: [8] System error: Invalid unit type.",
				"If you observe the output of execution of the TestCX class, you see an error to second user (Clayton) in return of method getClass().",
				"Observed: The refresh fails to grab the data and the stats page reflects a bunch of nonsense (/0 error)" };

		TestUtils.testSentences(negs, pm, 1);
	}

}
