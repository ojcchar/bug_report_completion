package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class NonTermPMTest extends BaseTest {

	public NonTermPMTest() {
		pm = new NonTermPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {
				"Ideally, any code using these duplicates would be checked and fixed if necessary (e.g., adding a non-duplicating message code if appropriate); however, I don't think this should keep us from correcting these duplicates, especially because we cannot use [Amanuens|http://amanuens.com] for translating our message bundles in trunk until these duplicates are removed.",
				"The issue here is that if you want to do anything even vaguely research-related with a system, every patient should have a non-MRN ID that can be used to re-identify them later.",
				"It would make the most sense if this tag and its contents simply disappeared when a non-friend/non-fan views an application tab.",
				"1 This is running as a cloud init so the installation should be non interactive, so how to avoid this prompt and force it to install with new configuration",
				"and none of the concept words are actually updated.",
		};

		TestUtils.testSentences(sentences, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"Accessing apps via https:// produces a warning about non-ssl content",
				"Going to the main non-SSL page led to a 403 error, even though permissions are set correctly and the server is running as user www group www, same as with previous Apache.",
				"Additionally, due to the fact that mod_cache uses the quick_handler hook, it interrupts (if deciding to return cached content) most down-stream modules so that they cannot make decisions about caching or non-caching content.",
				"I'm trying to create an image with docker builder, but the command \"ADD index.js in /src\" gives me the error message: Error build: The command [/bin/sh -c #(nop) ADD index.js in /src] returned a non-zero code: 255.",
				"An `rm -rf` of a non-empty directory that was created on a previous layer seems to fail consistently.",
				"This is way too slow to make it useful in any sort of non-trivial environment.",
				"The problem happens, whenever an alias gets shortened due to an non-letter character and the shortened alias is less than or equal to Dialect.getMaxAliasLenght().",
				"Issue HHH-3965 partly resolved this issue for non-nationalized characters by mapping CLOB to VARCHAR(MAX); however under the {SQLServer2008Dialect} which extends {SQLServer2005Dialect}, using the @Lob and @Nationalized annotations results in mapping to a NCLOB data type which doesn't exist for SQL Server.",
				"isOpenmrsObjectCollection returns true for collections of non-OpenmrsObject",
				"The error it gives is At least one non-empty name is required",
				"The thread safety issue is caused by having multiple threads that share a state in a non-thread safe {{HashSet}}. ",
				"PatientProgramValidator requires that all states in a patient program be in *non-retired* workflow in that program",
				"However, the validator only tests against non-retired workflows, which I believe is in error.",
				"Since some of these options definitely have a non-mandatory flavor when considering some compliance levels (aka JavaCore.COMPILER_PB_ASSERT_IDENTIFIER for 1.3 or 1.4), rendering the effective behavior in a concise documentation may turn out to be a bit challenging.",
				"We have a number of non-US users of our application (primarily Australia) and a large number of them encounter an untrusted security certificate warning when they access https://www.facebook.com/login.",
				"produces a prompt about a non secure content",
				"firefox default theme button text non center",
				"Using the default Firefox theme with Icons and Text, some of the text for the buttons are *slightly* non center, with the Stop button being the most noticable.",
				"Package libreoffice-common left non upgraded.",
				"This is causing attempts to load Save Handlers on commits to the DB (where the method name starts with 'save') on non OpenmrsObjects: this results in casting exceptions that will hault the thread in the AOP (RequiredDataAdvice) before the commit is ever executed.",
				"Observed: The refresh fails to grab the data and the stats page reflects a bunch of nonsense (/0 error)",
				"However, if I replace the 'USER nonroot' line with 'RUN /bin/su nonroot' everything is copacetic.",
				"Actual Results:  Striped firefox-bin and shared libraries end up in the firefox- directory while nonstriped shared libraries end up in the firefox-devel directory.",
				"In IE7_ I m getting a security warning  This page contains both secure and nonsecure items. ",
							
		};

		TestUtils.testSentences(sentences, pm, 1);
	}
}
