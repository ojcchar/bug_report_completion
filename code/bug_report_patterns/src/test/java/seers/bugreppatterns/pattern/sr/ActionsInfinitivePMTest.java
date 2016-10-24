package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 9/12/16.
 */
public class ActionsInfinitivePMTest extends BaseTest {
	{
		pm = new ActionsInfinitivePM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] paragraphs = {

				"\"step1: open liber office and select liber draw\n" + "step2: Draw rectangle image\n"
						+ "step3: select the rectangular image and go to modify meanu\n" + "step4: click convert\n"
						+ "step5: click convert to polygon\n" + "step6: rectangle should get converted to polygon\"",
				"- creating new table\n" + "- fill Cell \"A1\" with i.e. \"Text1\"\n"
						+ "- autofill 10 lines down (Text1, Text2, Text3 ...)\n"
						+ "- insert comment to one cell, i.e. A5 (\"Text5\")\n" + "- show comment\n"
						+ "- insert comment to another cell, i.e. A7, and also show comment\n"
						+ "- hide line 7 (comment remains visible)\n" + "- copy whole table with context menu\n"
						+ "- now hide line 5 in new created table\n" + "- all comments double (see atachement)",
				"1. inside a method type \"new Runnable(\"\n" + "2. press Ctrl+SPACE\n"
						+ "==> no method comment for run()",
				"1:New a report,\n" + "2:Add a chart in the body.\n"
						+ "3:Select the chart, and switch to the script editor.\n"
						+ "4:Type \"abc\" , then save the report design.\n"
						+ "5:Watch the outline view, the script node of the chart show.\n"
						+ "6:Delete the \"abc\", then save the report design.",
				"1. check out a bundle from CVS, e.g. org.eclipse.core.filebuffers\n"
						+ "2. File > Export... > Deployable plug-ins and fragments\n" + "==> NPE:",
				"1 New Text Document.\n" + "2 Insert -> Footer -> Default Style.\n"
						+ "3 Table -> Insert Table -> 1x1.\n" + "4 Place cursor after table.\n"
						+ "5 Type 3 \"_\" and return (if Apply Border set in AutoCorrect Options).\n"
						+ "6 Place cursor in paragraph above line.\n" + "7 Type \"test\".\n"
						+ "8 Save, close, and reopen document.\n" + "Alternate:\n" + "5 Type \"test\".\n"
						+ "6 Format -> Paragraph -> Borders -> add bottom line in User-defined box.\n"
						+ "7 Save, close, and reopen document.\n" + "Result:\n"
						+ "\"test\" and line are overlapping each other.\n" + "There is also a red triangle.",
				"Open new text document.\n"
						+ "[2] Enable Direct Cursor with option &#x201C;Tabs&#x201D;: Tools > Options... > LibreOffice Writer > Formatting Aids > Direct cursor. Then &#x201C;OK&#x201D;\n"
						+ "[3] Display Nonprinting Characters in order to observe the result.\n"
						+ "[4] Choose &#x201C;Align right&#x201D;. &#x2192; [5a] or [5b]\n"
						+ "[5a] Click into the first third of the first line. Only a few tabs beginning in the third third to the end of the line are inserted.\n"
						+ "[5b] Click into the third third of the first line. A lot of tabs beginning in the first third to the end of the line are inserted."

		};
		TestUtils.testParagraphs(paragraphs, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] paragraphs = {

				"creating new table\n" + "fill Cell \"A1\" with i.e. \"Text1\"\n"
						+ "autofill 10 lines down (Text1, Text2, Text3 ...)\n"
						+ "insert comment to one cell, i.e. A5 (\"Text5\")\n" + "show comment\n"
						+ "insert comment to another cell, i.e. A7, and also show comment\n"
						+ "hide line 7 (comment remains visible)\n" + "copy whole table with context menu\n"
						+ "now hide line 5 in new created table\n" + "all comments double (see atachement)",
				"Steps to Reproduce:. 1 see beneath. 2. 3. ", "#### API Request with `docker commit`. ",
				"I have a CentOS 4.1 server running httpd-2.0.52-12.\nent.centos4 using client certificates for authentication.",
				"i am the developer of the application  a church from lebanon _ but why do i get an error box everytime i try to send a pic? \nthe error is 404. please reply asap. thanks in advance. another thing_ i tried to email u at the support email but it is not available.",
				"So if you're using suexec out of the box, you're pretty well hosed-up at this. point. I've been studying the suexec.c utility and it seems pretty tight the way. it is. I could hack it up but it seems a shame to have to resort to that. On the. other hand, my opinion is that its become so locked-down (and for good reason). that people DO look for ways around it, or else to modify it, to support this. one very common hosting configuration. Or else look for ways to \\cheat\" using. suid programs and the like to dart in, create a file the user can use-- or some. other trick, which likewise causes a number of security risks. So while its. possible to modify suexec.c to shoe-horn in support for the userdir/public_html. virtual host configuration, it doesn't seem to be the best solution available.\""

		};
		
		TestUtils.testParagraphs(paragraphs, pm, 0);
		
		@SuppressWarnings("unused")
		String[] knownFails = {
				"1 missing concominant build options. 2 Build OK. 3 httpd.conf LoadModule ...../mod_access.. so directive are missing. 4 Apache won't start because directives Order,Allow,Deny exists in httpd.conf. "		};
		
		@SuppressWarnings("unused")
		String[] trickyOnes = {
				"If you ever happen to want to link against installed libraries. in a given directory, LIBDIR, you must either use libtool, and. specify the full pathname of the library, or use the `-LLIBDIR'. flag during linking and do at least one of the following:. - add LIBDIR to the `LD_RUN_PATH' environment variable. during linking. - use the `-Wl,--rpath -Wl,LIBDIR' linker flag. - have your system administrator add LIBDIR to `/etc/ld.. so.conf'. "
		};
	}
}
