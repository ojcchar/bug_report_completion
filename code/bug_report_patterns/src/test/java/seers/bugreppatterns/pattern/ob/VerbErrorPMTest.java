package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class VerbErrorPMTest extends BaseTest {
	public VerbErrorPMTest() {
		pm = new VerbErrorPM();
	}

	@Test
	public void testNegatives() throws Exception {
		String[] n = { "Currently, you can select (in the problem filter):",
				"### Actual behavior\n" + "Right now `timestamp`, being used as a sorting criteria, even when "
						+ "sometimes a value other than an actual timestamp is assigned to such "
						+ "`timestamp` attribute, which is misleading.",
				"However when I moved a whole directory from one part of the webproject to "
						+ "another I found copies in place places, the new destination and the old "
						+ "source location.",
				"If you try to refresh this page on the browser you have to wait about 2 minutes "
						+ "\nto get the page on your browser.",
				"When I set LogLevel to debug, debug level log messages from mod_ldap are going\n"
						+ "to stdout rather than my ErrorLog.",
				"it returns with no error", "it retruns with no name" };

		TestUtils.testSentences(n, pm, 0);
	}

	@Test
	public void testPositives() throws Exception {
		String[] n = {
				"I installed the new Apache 2.0.40, and when I try to restart the service I get an error saying: Cannot load C:/php-4.2.2-Win32/sapi/php4apache2.dll into server: The specified procedure could not be found.",
				"it returns with error"};

		TestUtils.testSentences(n, pm, 1);
	}
}
