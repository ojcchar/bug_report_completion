package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class AttachRefPMTest extends BaseTest {

	public AttachRefPMTest() {
		pm = new AttachRefPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {
				"However, it would be great if upon a double click there, the Project properties-Java Build Path-Libraries would open the concerned library where Ic ould attach the source.",
				"1 Unmapped data is currently lost (left in the extension map) and could be preserved, say, by attaching it to the model using stereotypes.",
				" use the attached report design",
				"Sample code to attach to a canvas page input field is below_ output display goes to elements with IDs keydown_ keyup_ and keypress.",
				"1 If the the text of the field description of the attachemet inclue   or \\   or &amp;quot; ALWAYS is rendered as &amp;quot; in the stream of the user",
				"According to the docs_ calling Stream.publish without attachments should update a user s status.",
				"As of approximately April 2nd_ statuses aren t updated if Stream.publish is called with action links_ even if there are no attachments.",
				"When requesting publish_stream permission_ the heading in the dialog reads  Publikování na mé Zdí  (see attachment).",
				"Each page is supposed to have a html and/or xml language attribute attached, and each html/xml element within it can cascade the language attribute from it or have it's own lang attribute.",
				"When select a file to attach to my current gmail message, Mozilla closes immediately with no warning or message.",
				"Running gmail - compose mail - attach file/browse - select a file and Mozilla closes",
				"The bug seems to be that it's trying to read input before attaching the  HTTP_IN filter.",
				"Apparently things are working fine apart from the message since `docker ps -a | grep attach` gives me back no results.",
				"If start is failed then attach will hang forever.", "Windows CLI: Ctrl+C terminates attached session",
				"Sending kill signal to the Linux process (like `top`) while attached to a container (either with `attach`/`exec` or `run -i -t`) from Windows CLI terminates the docker.exe.",
				"[attach] invalid memory address or nil pointer", "Please see the attached code for an example.",
				"Attached is a zip file with a small maven project with unit-test that replicates the problem.",
				"example project attached",
				"I have attached a zipped, self-contained Eclipse Project that contains a minimal test case.",
				"2 Microsoft security essentials will pop up with the attached screenshot",
				"Created attachment 56395 validated file", "Created attachment 64804 Excel test file with macro",
				"Created attachment 65781 EPS Example, ODT Writer, PDF Export, Screenshot",
				"Created attachment 68489 Example of a file", "An example of a file is attached.",
				"load attachment 46728 from Bug 37212 in LibO 4.4 master and compare with Excel or Excel Viewe",
				"Possibly related to this is that if I try to edit a concept that has an obs attached to its concept name id, then I get a \"cannot delete name\" error.",
				"Attached are two patches: one fixed the getAge method and one added test cases for the fix.",
				"MediaGridFragment can crash on callbacks when fragment isn't attached", };
		TestUtils.testSentences(sentences, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = { "They'll generate NPEs in ActualTrimDropTarget (I've attached my log).",
				"[Regression] ReportServiceException is thrown out when preview attached report in Web Viewer",
				"Problem description: translation of \"Manage configurations\" button label is truncated (word \"konfiguracjami\" is truncated) - please see attached screenshot.",
				"planet.eclipse.org looks all wacky (see attached screenshot) while planeteclipse.org looks the same as it always did.",
				"See 2 attached screen shots - the first one is for egit (incorrect displaying) and the second one is gitX (correct displaying).",
				"The attached test case project shows a situation where using ScrollableResults does not hydrate a proxy object upon join fetch where-as using the JPA getResultList method does.",
				"The example/test case attached demonstrates the problem; the HibernateCollectionTest succeeds (and uses a model without the UserCollectionType internally), the UserCollectionTest fails with exactly the same operations, but the UserCollectionType (/jpaSandbox/src/main/java/de/soflimo/sandbox/model/impl/CustomList.java) specified in the mapping.",
				"The following two files attached shows an abnormal and breakage of table rendering under LO 3.4 rc2",
				"When loading the attached DOCX into LO, and saving back to DOCX - the exported DOCX is corrupted and cannot be opened by MS Word.",
				"The crash report is attached.",
				"In the attached test.odb, in the mainform (form) when opened in windows 7, the table has black fonts instead of whites.",
				"3 If we keep on clicking the next button or the prev button till the list is finished, an error is popped(screenshot attached)." };

		TestUtils.testSentences(sentences, pm, 1);
	}

}
