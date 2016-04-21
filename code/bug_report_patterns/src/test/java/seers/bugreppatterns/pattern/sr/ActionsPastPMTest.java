package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ActionsPastPMTest extends BaseTest {

	public ActionsPastPMTest() {
		pm = new ActionsPastPM();
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = {
//				"Reproduction steps:\n1 create a projects with 2 branches derived from head. Modify a file in those branches (the same file).\n2 from CVS repositories view look for one of those branches, find a file, and compare it with another branch.\n3 you can notice that in your branch additional folder is generated. It seems to be a parent folder of compared resource.",
//				"1 Each time the contextual spell checker is applied on a form, it will check for a HTML or XML language attribute for that form. If there isn't one, it will go up in the tree of the document, tag by tag, until it found one.\n2 It will select the appropriate dictionary of that language, if it's installed.\n3 If the dictionary is not installed, it will display a small message along the line of \"this form seems to ask for French text but you don't have the French dictionnary on your Firefox, do you want to install it?\" when and only when the spell checker is called (maybe some kind of icon with a \"more information\" bubble?).\n4 If no language attribute was found in the page rendered, then the default behavior comes back.",
//				"A second set of tests conviced me that the problem is related to the mount point\n. I have placed a test directory tree on /data2/test.\n- When /data2/test is not a mount point, apache 2.0.59 starts and the configtest\nsucceed.\n- When I use /data2 as a mount point, apache 2.0.59 doesn't start and the\nconfigtest fails.",
//				"Expected results:\n1- Calendar layout is mirrored to be \"RTL\"\n2- Calendar buttons behavior should be toggled (i.e pressing the left arrow next to the \"year\" should increase the displayed year)\n3- Year, Months, days are shown in Bidi languages (ex: Arabic in case of egyptian locale)",
//				"Actual Results:\n1- Buttons behavior is not correct. \n2- Digits, days, months are not localized in the mirrored calendar (ex: names of days and month should be written in Arabic in case of using Egyptian locale)",
//				"The Result:\n- posts created with app XXXXXXXX are returned\n- posts with an app_id equal to null are also returned (i.e.  app_id : null_ )",
//				"- It contains no Cache-Control header\n   - and the request is placed shortly after 1. such that the document is\n     not expired already",
//				"reasons\n- it does not make sense to select thing thing you are already looking at\n- if you open the list then press down arrow you skip over all items above the \ncurrent item\n- this is particularly bad when the editor is in single tab mode as the \nselection goes straight to the bottom",
//				"Steps to Reproduce:\n1 you need nview tool to be enabled (the additional symbols in window frame)\n1 open demo_crash.htm, stored locally or on a server\n2 click to the link on top of the page 5-8 times\n3 firefox hangs and uses 100% of one core of your cpu\n4 when killing firefox now by task manager, another process will take 100%\n5 you can loop step 4 forever, finally you will have to reboot your machine\nActual Results:  \nSystem is not useable anymore after doing this test until rebooting the system.",
//				"Reproducible: Always\n1. start IE with an authenticating proxy configured\n2. access a URL requiring proxy authentication\n3. type proxy userID then hit TAB\n4. type proxy password then hit ENTER\nActual Results:  \nDialog remains. Password textbox is selected, cursor is flashing at end of password.", 
"1. add some TODO comments in html files\n2. in task tags preference page, enable searching for task tags (make sure enabled for html content type)\n3. task tags are found and reported in tasks view\n4. go back to task tags preference page and in the filters tab, disable scanning html content type\n5. apply &amp; clean and redetect tasks\n6. ** task tags are still in tasks view and marked in files (though any new todo comments are no longer turned into task tags)"				
		};

		TestUtils.testParagraphs(txts, pm, 0);
	}

	

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
//				"Reproduction steps:\n1 create a projects with 2 branches derived from head. Modify a file in those branches (the same file).\n2 from CVS repositories view look for one of those branches, find a file, and compare it with another branch.\n3 you can notice that in your branch additional folder is generated. It seems to be a parent folder of compared resource.",
//				"1 Each time the contextual spell checker is applied on a form, it will check for a HTML or XML language attribute for that form. If there isn't one, it will go up in the tree of the document, tag by tag, until it found one.\n2 It will select the appropriate dictionary of that language, if it's installed.\n3 If the dictionary is not installed, it will display a small message along the line of \"this form seems to ask for French text but you don't have the French dictionnary on your Firefox, do you want to install it?\" when and only when the spell checker is called (maybe some kind of icon with a \"more information\" bubble?).\n4 If no language attribute was found in the page rendered, then the default behavior comes back.",
//				"A second set of tests conviced me that the problem is related to the mount point\n. I have placed a test directory tree on /data2/test.\n- When /data2/test is not a mount point, apache 2.0.59 starts and the configtest\nsucceed.\n- When I use /data2 as a mount point, apache 2.0.59 doesn't start and the\nconfigtest fails.",
//				"Expected results:\n1- Calendar layout is mirrored to be \"RTL\"\n2- Calendar buttons behavior should be toggled (i.e pressing the left arrow next to the \"year\" should increase the displayed year)\n3- Year, Months, days are shown in Bidi languages (ex: Arabic in case of egyptian locale)",
//				"Actual Results:\n1- Buttons behavior is not correct. \n2- Digits, days, months are not localized in the mirrored calendar (ex: names of days and month should be written in Arabic in case of using Egyptian locale)",
//				"The Result:\n- posts created with app XXXXXXXX are returned\n- posts with an app_id equal to null are also returned (i.e.  app_id : null_ )",
//				"- It contains no Cache-Control header\n   - and the request is placed shortly after 1. such that the document is\n     not expired already",
//				"reasons\n- it does not make sense to select thing thing you are already looking at\n- if you open the list then press down arrow you skip over all items above the \ncurrent item\n- this is particularly bad when the editor is in single tab mode as the \nselection goes straight to the bottom",
//				"Steps to Reproduce:\n1 you need nview tool to be enabled (the additional symbols in window frame)\n1 open demo_crash.htm, stored locally or on a server\n2 click to the link on top of the page 5-8 times\n3 firefox hangs and uses 100% of one core of your cpu\n4 when killing firefox now by task manager, another process will take 100%\n5 you can loop step 4 forever, finally you will have to reboot your machine\nActual Results:  \nSystem is not useable anymore after doing this test until rebooting the system.",
//				"Reproducible: Always\n1. start IE with an authenticating proxy configured\n2. access a URL requiring proxy authentication\n3. type proxy userID then hit TAB\n4. type proxy password then hit ENTER\nActual Results:  \nDialog remains. Password textbox is selected, cursor is flashing at end of password.",
				"- Typed in an identifier for an interface that didn't exist.\n- Quick Fix (Ctrl-1), accidentally chose Create Class\n- I'm pretty sure I hit Enter twice but then was able to select Cancel\n- Within the editor, I couldn't open the new class but I could no longer create it again because it already existed\n- Checking the filesystem, the Java file was there\n- Deleting the file fixed the problem"
		};

		TestUtils.testParagraphs(txts, pm, 1);
	}
}
