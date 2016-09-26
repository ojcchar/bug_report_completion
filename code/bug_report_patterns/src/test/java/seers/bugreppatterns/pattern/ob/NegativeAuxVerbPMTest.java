package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class NegativeAuxVerbPMTest extends BaseTest {

	public NegativeAuxVerbPMTest() {
		pm = new NegativeAuxVerbPM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] negs = {
				"Users are reporting that they can’t access notifications.",
				"With Direct Cursor option &#x201C;Tabs and spaces&#x201D; the behaviour is equal and hence also not correct.",
				"With Direct Cursor option &#x201C;Tabs and spaces&#x201D; the behaviour is equal and also not correct.",
				"something is making it not work on my system.",
				"Just like with [PostgreSQL|https://hibernate.atlassian.net/browse/HHH-10693], MySQL [does not support all nationalized types|http://dev.mysql.com/doc/refman/5.7/en/string-types.html] (e.g. NCLOB).",
				"After login_ the user is redirected to the web-based Facebook home page and not the url specified in the redirect parameter.",
				"Even after providing the input as I , it is stuck and not proceeding.",
				"Apparently that only happens onResume, but since the app sometimes \"starts\" onResume, that's maybe not related.",
				"last friday 27th june this is not working_", "which is not legal.",
				"I think this status is reading the same as the RSS Feed_ so the Status tab and the RSS Feed don t update_ the RSS Feed is located at: http://www.new.facebook.com/feeds/api_messages.php",
				"Since `scratch` doesn't have anything in it, it's not possible to start it without error.",
				"Since menu is just a local variable, it is not updated when a menu is created in the MenuDetect event.",
				"Since the cascade operation does not use (nor should it) the ProgramWorkflowService.savePatientProgram() method, we miss out on the null-checks for the metadata fields, which means Hiberante tries to persist the PatientState object with null values for some required fields."

		};

		TestUtils.testSentences(negs, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] negs = { "Very hard to reproduce and not sure what to look for when it happens.",
				"I changed the MTU to 1500 and not solved." };

		TestUtils.testSentences(negs, pm, 0);
	}
}
