package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ActionsPresentPMTest extends BaseTest {

	public ActionsPresentPMTest() {
		pm = new ActionsPresentPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"1. Assuming all caches all empty, we run a query. The ids of the returned objects are stored in the query cache and the objects are stored in the L2 object cache. Great so far.\n"
						+ "2. Now, the query cache expires before the object cache does. The query is re-ran and the ids of the returned objects are cached in the query cache.\n"
						+ "3. The object cache expires. The query cache is still valid, so the ids are returned from the query cache. Unfortunately, the objects are no longer cached and hibernate fetches them all one-by-one, which takes forever.",
				"- If you leave the site, a popup will show up. You close it but it keeps coming back. You have no option but to kill Firef" };

		TestUtils.testParagraphs(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = {
				"Reproduction steps:\n1 create a projects with 2 branches derived from head. Modify a file in those branches (the same file).\n2 from CVS repositories view look for one of those branches, find a file, and compare it with another branch.\n3 you can notice that in your branch additional folder is generated. It seems to be a parent folder of compared resource.",
				"1 Each time the contextual spell checker is applied on a form, it will check for a HTML or XML language attribute for that form. If there isn't one, it will go up in the tree of the document, tag by tag, until it found one.\n2 It will select the appropriate dictionary of that language, if it's installed.\n3 If the dictionary is not installed, it will display a small message along the line of \"this form seems to ask for French text but you don't have the French dictionnary on your Firefox, do you want to install it?\" when and only when the spell checker is called (maybe some kind of icon with a \"more information\" bubble?).\n4 If no language attribute was found in the page rendered, then the default behavior comes back.",
				"A second set of tests conviced me that the problem is related to the mount point\n. I have placed a test directory tree on /data2/test.\n- When /data2/test is not a mount point, apache 2.0.59 starts and the configtest\nsucceed.\n- When I use /data2 as a mount point, apache 2.0.59 doesn't start and the\nconfigtest fails.",
				"Expected results:\n1- Calendar layout is mirrored to be \"RTL\"\n2- Calendar buttons behavior should be toggled (i.e pressing the left arrow next to the \"year\" should increase the displayed year)\n3- Year, Months, days are shown in Bidi languages (ex: Arabic in case of egyptian locale)",
				"Actual Results:\n1- Buttons behavior is not correct. \n2- Digits, days, months are not localized in the mirrored calendar (ex: names of days and month should be written in Arabic in case of using Egyptian locale)",
				"The Result:\n- posts created with app XXXXXXXX are returned\n- posts with an app_id equal to null are also returned (i.e.  app_id : null_ )",
				"- It contains no Cache-Control header\n   - and the request is placed shortly after 1. such that the document is\n     not expired already",
				"reasons\n- it does not make sense to select thing thing you are already looking at\n- if you open the list then press down arrow you skip over all items above the \ncurrent item\n- this is particularly bad when the editor is in single tab mode as the \nselection goes straight to the bottom",
				"Steps to Reproduce:\n1 you need nview tool to be enabled (the additional symbols in window frame)\n1 open demo_crash.htm, stored locally or on a server\n2 click to the link on top of the page 5-8 times\n3 firefox hangs and uses 100% of one core of your cpu\n4 when killing firefox now by task manager, another process will take 100%\n5 you can loop step 4 forever, finally you will have to reboot your machine\nActual Results:  \nSystem is not useable anymore after doing this test until rebooting the system.",
				"1. Create and publish a post\n2. A notification \"Post published\" appears\n3. Update that post\n4. A second notification \"Post published\" appears, the first one should be reused" };

		TestUtils.testParagraphs(txts, pm, 0);
	}
}
