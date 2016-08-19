package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ErrorHowPMTest extends BaseTest {
	public ErrorHowPMTest() {
		pm = new ErrorHowPM();
	}

	 @Test
	    public void testNegative() throws Exception {
	        String[] negatives = {"Poor viewing in windows explorer"};

	        TestUtils.testSentences(negatives, pm, 0);
	    }
}
