package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class PositiveConditionalPMTest extends BaseTest {

	public PositiveConditionalPMTest() {
		pm = new PositiveConditionalPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {

		};

		TestUtils.testSentences(sentences, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"With a certain configuration of Virtual hosts with SSL Engine enabled, Apache beliefs the client is connecting to server port 80 with protocol HTTP, while in reality the client is connected to server port 443 with protocol HTTPS",
				"Configure exits when using --enable-mods-shared=all on mod_auth_digest",
				"EDITING: Comments double when copying Sheets",
				"And as a result Provider.getName() returns null if the provider points to a voided person."

		};
		TestUtils.testSentences(sentences, pm, 1);
	}
}
