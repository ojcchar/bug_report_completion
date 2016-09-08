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
		String[] negs = { "last friday 27th june this is not working_",
				"I think this status is reading the same as the RSS Feed_ so the Status tab and the RSS Feed don t update_ the RSS Feed is located at: http://www.new.facebook.com/feeds/api_messages.php",
				"Since `scratch` doesn't have anything in it, it's not possible to start it without error.",
				"Since menu is just a local variable, it is not updated when a menu is created in the MenuDetect event.",
				"Since the cascade operation does not use (nor should it) the ProgramWorkflowService.savePatientProgram() method, we miss out on the null-checks for the metadata fields, which means Hiberante tries to persist the PatientState object with null values for some required fields."};

		TestUtils.testSentences(negs, pm, 1);
	}
}
