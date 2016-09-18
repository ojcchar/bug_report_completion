package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class BecausePMTest extends BaseTest{

	public BecausePMTest(){
		pm = new BecausePM();
	}
	
	@Test
	public void testPositives() throws Exception {
		String[] txts = {"The commit failed because the command claimed that several files were not up-to-date. ",
					"- Within the editor, I couldn't open the new class but I could no longer create it again because it already existed",
					"UNABLE TO DEACTIVATE ACCOUNT BECAUSE NO SECURITY CHECK WORD APPEARS",
					"Again_ it posts to my wall_ but the link is broken because it adds an = to the end_ and changes the . to a _ in my URL",
					"For example_ Facebook s DOM parsing routines cannot find  head  when content type is set properly to XHTML_ because they are looking for upper-case  HEAD  as would be expected in an HTML document.",
					"It seems that the img parameter isn t recognized at all because the reference to the thumb image cannot be found in the resulting html embed code.",
					"The 100 (Continue) response is not forwarded at all, which avoids the connection termination problem mentioned above, but is also a MUST violation because all unsolicited 1xx responses must be forwarded.",
					"That fails if apr_ldap cannot be loaded, because result is still NULL then.",
					"The \"go get\" line now fails because without \"-d\" (download-only) it implies \"go install\", which cannot be run outside the GOPATH (which our symlinked directory is).",
					"I mark this issue as blocker, because indeed it is blocking me - I cannot sort my collections, and I believe it is validation of the JPA spec.",
					"The following tests create native SQL queries involving {{Simple}}, which fail using DB2 because the resulting SQL does not have the table name auto-quoted:",
					"It looks like the DB2 and SAPDB dialects fail in this category because of:",
					"When using the built-in hibernate collection handling, this succeeds; but when specifying the CustomList UserCollectionType in the model mapping, committing this transaction fails because it tries to INSERT [at least] a second row with an identical uniqueKey.",
					"After upgrade to 4.1.1, our application doesn't work anymore because the sqlEntityIdByNaturalIdString field of the entity persister is null while it shouldn't be.",
					"The current toolbars are very flexible, but they are painful to manage/edit because you cannot use drag and drop to do so.",
					"`dmesg -c` in container clears hosts's printk ring buffer because container has CAP_SYSLOG as default.",
					"1 If a cached document is outdated because it is expired, it will be requested again from Tomcat, which is completely correct.",
					};
		TestUtils.testSentences(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = {"There is only one consul node, on the same host (because I don't need the multi host network know)",
				"This would be especially useful if the .class file is present in multiple .jar files because it then would bring to the one implementation that gets really used.",
				"Perhaps this should be an optional, because it's functionally identical to a normal API call.",
				"Then because the Content-Length is not changed, when the request post data is received after the filter, it will be truncated to the original Content-Length size (i.e. would get param1=abcde in the previous example).",
				"Note: Installing `default-jdk` (1.6) on `ubuntu:precise` works fine, because it doesn't seem to use `fuse`."};
		TestUtils.testSentences(txts, pm, 0);
	}
	
}
