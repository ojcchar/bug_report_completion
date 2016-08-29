package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class NegativeAdjOrAdvPMTest extends BaseTest {

	public NegativeAdjOrAdvPMTest() {
		pm = new NegativeAdjOrAdvPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {};

		TestUtils.testSentences(sentences, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = { "Getting each element of an array twice submitting form using POST",
				"TP125: other projects referenced twice (1G83V7V)",
				"For some reason, the function ti_sysbios_hal_Hwi_getHookContext__E is being implemented twice in the generated source file.",
				"Facebook.streamPublish JS is encoding quotes twice", "It seems to report the error twice.",
				"Firefox crashed twice in a week and brought the operating system down (Windows2000). " };

		TestUtils.testSentences(sentences, pm, 1);
	}
}
