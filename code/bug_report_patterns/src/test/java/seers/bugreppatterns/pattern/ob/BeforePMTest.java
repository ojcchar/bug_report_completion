package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class BeforePMTest extends BaseTest {

	public BeforePMTest() {
		pm = new BeforePM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"Exception result: No matter how I drop the table slow, the cursor stay at the place where before drop.",
				"paper cut: when loading a link that is clicked, the previous page remains onscreen awhile before the screen is blanked out for the destination page.  During this time, you can sometimes click links on previous page and change directions, but not always.",
				"a) No where in the module is client_rmm initialised before being used.",
				"Uploading a file results in an enormous wait time before the page actually returns, and the data that is displayed is only a very small fraction of the total data transmitted from the file..",
				"The bug seems to be that it's trying to read input before attaching the HTTP_IN filter.",
				"Apache reports it is stopped before all child processes have stopped",
				"Persistent immutable and read-only entities are updated before being deleted",
				"Problem is in parsing - select clause is resolved on the end and then exists aliasPath for bid.item - group by is resolved before creating alias",
				"LO crashes if I open it to the start center and then press Cmd+O or use File -&gt; Open from the menu BEFORE doing anything else.",
				"Most of the time LibreOffice crashes before the splash screen is shown.",
				"This is causing attempts to load Save Handlers on commits to the DB (where the method name starts with 'save') on non OpenmrsObjects: this results in casting exceptions that will hault the thread in the AOP (RequiredDataAdvice) before the commit is ever executed.",
				"It appears the patient search triggers before the user finishes typing, and doesn't update the search results as they continue typing.",

		};
		TestUtils.testSentences(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = { "Note the leading '/' before the URL, it's obvously incorrect.",
				" When I open some sites in tabbed browsing mode, switching between tabs become very slowly,sometimes I must close current tab before switch to another.",
				"Actual result: If I drop slow, the cursor will stay in or before \"from\"",
				"[formatter] Linebreak before trailing statement semicolon not preserved",
				"fb:swf tags loaded in a dialog can switch content while page is loading (before scripts are initialized?)",
				"Before today s (5/1) push_ this worked fine but after the push my script yields a  foo is undefined  error.",
				"The problem is that the object FB is not initialized as it is supposed to before every canvas page.",
				"now since before the creation of the second container the command in 3 return the Ubuntu container and after it the Redis container return this seems to me like an ambiguity (and if you delete the redis you get the ubuntu again after the inspect command)",
				"Provide additional environment details (AWS, VirtualBox, physical, etc.): No virtualbox, I am trying on my host first before moving everything to a synology NAS.",
				"As long as those query strings may be big (depending on the number of join they contain) it would be nice to .intern() the sql queries before storing them in the EntityLoader.",
				"In this case, we should force the postInstantiation of the root entity persister before using sqlEntityIdByNaturalIdString (or we should at least generate its sqlEntityIdByNaturalIdString).",
				"The installation process showed bizarre characters before file extraction.",
				"When I search w/ the cohort builder for patients on any regimen on or before a certain date, I get the same number of results regardless of the date entered.",
				"For giggles, I removed the file extension from an image before uploading it to the media library.",

		};
		TestUtils.testSentences(txts, pm, 0);
	}

}
