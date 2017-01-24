package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class LeadsToNegativePmTest extends BaseTest {

	public LeadsToNegativePmTest() {
		pm = new LeadsToNegativePm();
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				
				"using DDE and WWW_OpenURL to open a file results in a truncated file name",
				"Instead_ links are having  connect  appended which results in the wrong page being displayed.",
				"This leads to avatars being cut off.",
				"This small inconsistent behavior causes the user frustration not experienced in other browsers.",
				"But \"expires\" module isnt enabled in the host, and therefore results in \"500 Internal Server error\" when url is accessed via browser.",
				"results in an error message in the installer, that the server admin field is not set in httpd.conf and that the service cannot be started. ",
				"running a docker in docker will result in a not clean enviroment",
				"-> if the root persister is not postInstantiate()d yet (which is our case here), sqlEntityIdByNaturalIdString is null and so is the one of the concrete class leading to fatal error when trying to execute queries using the natural id.",
				"Issue HHH-3965 partly resolved this issue for non-nationalized characters by mapping CLOB to VARCHAR(MAX); however under the {SQLServer2008Dialect} which extends {SQLServer2005Dialect}, using the @Lob and @Nationalized annotations results in mapping to a NCLOB data type which doesn't exist for SQL Server.",
				"FILESAVE causes lots of artifacts and destroys VIEWING for embedded .svg",
				"This is causing attempts to load Save Handlers on commits to the DB (where the method name starts with 'save') on non OpenmrsObjects: this results in casting exceptions that will hault the thread in the AOP (RequiredDataAdvice) before the commit is ever executed.",
				"Exiting a patient from care causes \"not-null property references a null or transient value: org.openmrs.PatientState.dateCreated\"",
				"* When the query cache expires first, it won't cache the objects (as described), which will eventually lead to a situation where the query cache is valid and the object cache expired." 
				
		};
		TestUtils.testSentences(sentences, pm, 1);
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {

		};
		TestUtils.testSentences(sentences, pm, 0);
	}
}
