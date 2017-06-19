package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ErrorNounPhrasePMTest extends BaseTest {

	public ErrorNounPhrasePMTest() {
		pm = new ErrorNounPhrasePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = { 
				"misleading", "Unhandled exception caught",
				"5 observe: once again you can set \"referenced projects\".", "Currently it is:", "incorrect",
				"update your status",
				"Attached is a zip file with a small maven project with unit-test that replicates the problem.",
				"Currently, you can select (in the problem filter):",
				"it will then authenticate with the above URL with http error 400 (Bad request) page after clicking on the  Allow  button.",
				"when I run this command, there is a crash",
				 "there is a crash",
				 "The reason is that, when you don't have permission, File.list() would return null."
				};

		TestUtils.testSentences(sentences, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = { 
				
				"Poor viewing", "Slow UI refresh", "the stack trace", "a lot of errors",
				"the missing entry", "==&gt; NPE:", "[remoteserviceadmin] RemoteReferenceNotFoundException",
				"Links.preview - session key exception", "Domain Mismatch XML parsing error",
				"QuerySyntaxException \"with-clause expressions did not reference from-clause element to which the with-clause was associated\"",
				"Hibernate 3.3.1.GA + Javassist issue 3.9.0.GA : java.lang.RuntimeException: by java.lang.NoClassDefFoundError: org/hibernate/proxy/HibernateProxy (OSGI Server)",
				"SqlGrammarException", 
				"a truncated file name", "fail", "6 crash", "6. crash",
//				"Crash on Nexus 4, ACV 1.4.1.4"
				};

		TestUtils.testSentences(sentences, pm, 1);
	}
}
