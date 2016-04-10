package seers.bugreppatterns.pattern.eb;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class ShouldPMTest {
	ShouldPM pm = new ShouldPM();

	@Test
	public void testMatchSentence() throws Exception {
		String[] txts = { "We should use the core project reference mechanism if possible.",
				"Either that or the offending files should be removed from the zip file and some comments added to an installation document somewhere.",
				"Sort by parent path should take method name into account",
				"On linux platforms, it should also look for atleast \"konqueror\" and \"firefox\" and launch internally.",
				"Console should scroll to last line, not end of document",
				" It should reactivate any matching editor instead of opening a new one.",
				"It should not be written to the log",
				"A workbook should be created on the import page; first tab should be the range \r\noptions and the second tab will be the filters",
				"I think we shouldn't use the name of a facet as part of its definition unless we're unrolling an acronym (like EJB).",
				"Under what circumstances on Linux should I expect to see this error, is the message a result of an caught exception or false return value from Java API call for File.delete() ?",
				"3.3 rc3\r\nAdding a java breakpoints that is already in the breakpoint list but disabled, should re-enable it",
				"We should move the initialization code from IntroduceParameterObjectRefactoring's constructor to checkInitialConditions(..) and just abort with fatal RefactoringStatus.",
				"[Accessibility] Text should read its READ_ONLY state",
				"When a user enters a text field, it should indicate whether it's read-only or not.",
				" When the text is read-only it should say, \"Read-only Text <<and start reading the text value>>\".",
				"In the UI the user should have the choice to enable or disable indexing rather than selecting from the list: \r\nfast / full / null",
				"Some example bundles should be re-versioned to get source reference headers generated",
				"Loading templates wizard should support:",
				"When I select another application in that drop down - this application stats should load on the graph. ",
				"According to the docs_ calling Stream.publish without attachments should update a user s status.",
				"Back in tab with empty history should close tab and activate referer tab.",
				"A different icon should be used for both livemarks and livemark items.",
				"\"Size\" should be changed to \"File Size\",",
				"and \"Loaded Size\" should be added to the General tab.",
				" Perhaps a tags should not honor onClick\r\nevents if there is an external URL in the href attribute",
				"Stumbed on this during development of a site where users should be\r\nable to drag links to desktop and fire from there (marketing decition, not mine).",
				"Thus, it should be configurable. ",
				"Furthermore, one should be able to query for a particular range of visit_count, not just > 0.",
				"So it shouldn't be difficult for the spellchecker to detect the appropriate language (and if something goes wrong, the user still can manually select one).",
				"and it should be:\r\n\r\nhttp://httpd.apache.org/docs-2.0/developer/API.html",
				"(At least) the P query parameter should be unescaped in general by \r\nmod_autoindex.",
				"Opening tag is LocationMatch, closing tag should be /LocationMatch",
				"It would seem that after installing apache on windows 2000, should you \r\nuninstall the application it seems to delete settings within windows and causes \r\nInternet Sharing to be lost within a network.",
				"The SCRIPT_NAME variable should be /cgi-bin/b2g_bug.cgi , but the mod_cgi send \r\nme \"/cgi-bin/b2g_bug.cgi/http:\".",
				"The PATH_INFO should be \"/http://www.ep66.idv.tw\" , but the mod_cgi send \r\nme \"/http:/www.ep66.idv.tw\". ",
				"This also works on headers \r\nthat normally shouldn't use multiple options (e.g, User-Agent).",
				"This is no major deal, but perhaps the docs should be \r\nupdated to indicate this behavior?",
				"Apache should not report that it has stopped if not all the child processes have\r\nstopped.",
				"httpd should check whether it is process group leader or not first.",
				"In theory \"httpd -DFOREGROUND -DNO_DETACH\" should make apache run as well behaved application, but we see that this does not work at all.",
				"The pasting should be allowed with the user's permission.",
				"Clear Recent History should disable items which are already cleared (Cookies, Visited Pages, Active Logins, Web Cache, Offline Website Data, Site-specific settings)",
				"But it should not crash, only ignore!", "browser should open.",
				"In short, MOUSE_OUT and ROLL_OUT flash events are fired when it shouldn't, details there:\r\nhttp://www.soundstep.com/blog/2010/03/02/firefox-3-6-mac-bug-with-mouseevent/",
				"I believe that, or Firefox is firing mouse-out events when it shouldn't (on a click event), or somehow the flash is removed then put in place again on a click, so the flash loses the focus and fires a mouse_out event.",
				"It should blank out the current page immediately after clicking a link or allow in all cases for another link to be clicked to change the loading destination.",
				"A user should have the right to suppress a popup if the website developer abuses it.",
				"In the test case, when loading it, you should not have spellcheck entries in the context menu in the first input. ",
				"The program should output 1 but it outputs null instead. ",
				"But I think if this is an invalid use case, we should display and error/exception some where to inform the user.",
				"Should:\r\n Have a Import.../Export... button (maybe even for multi selection)",
				"Specify the same class twice in a orm.xml file and you should recieve a validation error.",
				"but it should be",
				"getColor() should be called in paint, as it is done in super-class org.eclipse.draw2d.LineBorder.",
				"This should contain the targets found in the top-level Makefile.",
				"MergeDiffTab should not expand whole tree but only those elements related to selected differences.",
				"For example_ if you use the below code_ the title on the page should be  Initial Title  as the other fb:title tag is encapsulated within the fb:js-string.",
				"It should just return array()", "We believe that that url should be https instead of http.",
				"The plugin should recognize that users are not leaving the content site they are on.",
				"It should be  Publikování na mé Zdi",
				"The profile picture url returned is http://\r\nIt should be https:// to match the request.",
				"It should be https:// to match the request.",
				"these are public open events and their data should be accessible without a user access token.",
				"Shouldn't have to reset every time we want\r\nto print a page.",
				"Expected Results:  \r\nThe partially typed URL should have been preserved, ready for the user to type\r\nor paste some more.",
				"If a link cannot be resolved correct, an error should be generated.",
				"If a link cannot be resolved in time, an error should be generated.",
				"Regardless of cause, the user should be able to click on any other link, select\r\na bookmard, or RELOAD the page successfully.",
				"When Firebird is closed, there\r\nshould be no remaining zombie threads.",
				"I think the location bar should shrink until the size = icon size+ 2* border\r\nsize // for left and right borders.",
				" If this cannot be done, it\r\nshould simply pop up as it normally would.",
				"Expected Results:  \r\nit should have opened the .html file as the url was sent.",
				"If you get Properties using the contextual menu, on a image, command-w on the\r\nkeyboard should close the window.",
				"Expected Results:  \r\nThe window should have closed.",
				"Expected Results:  \r\nIt should not have crashed the OS and the bookmarks should never disappear.",
				"The frame should have shifted back one page instead of the whole site",
				"Expected Results:  \r\nTree view of XML file should have been displayed",
				"I think the second apr_global_mutex_lock() should be apr_global_mutex_unlock().",
				"ap_regexec function declaration arg1 should be const",
				"In httpd.h, arg1 of the ap_regexec function should be declared const.",
				"The code\r\nshould change from:", "Should apache check that the DNS name passes some basic checks?",
				"should be 20 characters", "The cut should be \r\nmade after 20 characters",
				"A building dependency on zlib-devel should be added in httpd.spec",
				"As allume \r\nsystems itself suggests, to prevent the user's browser showing all the binary to the user instead of \r\nbeginning the download a new mime type should be added to apache's mime types list.",
				"In the example configurations, the reference to \"AuthLDAPAuthoritative\"\r\nshould be changed to \"AuthzLDAPAuthoritative\" for httpd-2.2.",
				"Since the example configurations use \"require valid-user\", should\r\n\"AuthzLDAPAuthoritative on\" actually be \"AuthzLDAPAuthoritative off\" as\r\nsuggested from the mod_authnz_ldap documentation\r\n(http://httpd.apache.org/docs/2.2/mod/mod_authnz_ldap.html#reqvaliduser):",
				"Apache should make an attempt to print the date in the language requested by \r\nthe client.",
				"As far as I would say, translations\r\nfor errors should be made such that the end user can understand what is going\r\non.",
				"Apache should not report that it has stopped if not all the child processes have\r\nstopped.",
				"This should fail and if possible throw out which module isnt enabled.",
				"In other words, configtest should parse and check if modules are enabled and throw out appropriate failure message."

		};

		for (int i = 0; i < txts.length; i++) {
			String txt = txts[i];

			System.out.print("Testing (positive) " + i);
			List<Sentence> sentences = TextProcessor.processText(txt);
			int m = pm.matchSentence(sentences.get(0));
			assertEquals(1, m);

			System.out.println(" PASSED");

		}
	}

}
