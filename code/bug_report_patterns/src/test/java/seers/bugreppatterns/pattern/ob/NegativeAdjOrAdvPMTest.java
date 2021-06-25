package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class NegativeAdjOrAdvPMTest extends BaseTest {

	public NegativeAdjOrAdvPMTest() {
		pm = new NegativeAdjOrAdvPM();
	}

	@Test
	public void testNegattive() throws Exception {
		String[] sentences = {
				"The reason is that, when you don't have permission, File.list() would return null."
		};

		TestUtils.testSentences(sentences, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"The plus symbol is gone",
				"\"Image\" is negative int for container in /containers/json?all=1 output",
				"Base shows things as they should be, and calc shows them messed up.",
				"Getting each element of an array twice submitting form using POST",
				"TP125: other projects referenced twice (1G83V7V)",
				"For some reason, the function ti_sysbios_hal_Hwi_getHookContext__E is being implemented twice in the generated source file.",
				"Facebook.streamPublish JS is encoding quotes twice", "It seems to report the error twice.",
				"Firefox crashed twice in a week and brought the operating system down (Windows2000). ",
				"As Nick Kew says in response to bug 31226, \"AddOutputFilterbyType is known to be broken.\"",
				"But since 4.3.0 the functionality appears to have been moved to: {{org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl.getResources(String)}} using a {{HashSet}} introducing an undesirable random element into the ordering.",
				"Since some of these options definitely have a non-mandatory flavor when considering some compliance levels (aka JavaCore.COMPILER_PB_ASSERT_IDENTIFIER for 1.3 or 1.4), rendering the effective behavior in a concise documentation may turn out to be a bit challenging.",
				
		};

		TestUtils.testSentences(sentences, pm, 1);
	}
}
