package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ConditionalNegativePMTest extends BaseTest {

	public ConditionalNegativePMTest() {
		pm = new ConditionalNegativePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {
				"If I push an image called bmorearty/xyz and then I make a brand new image (not based on the first one) and run `docker push bmorearty/xyz` again, the registry allows it.",
				"If a blog doesn't have any posts, the post list still shows \"Load more posts\" at the top.", };

		TestUtils.testSentences(sentences, pm, 0);

		@SuppressWarnings("unused")
		String[] trickySentences = {
				"If a blog doesn't have any posts the post list still shows \"Load more posts\" at the top.",
				"When LibreOffice calc retrieves floating-point data from a LibreOffice database (odb file) while having a locale setting with comma as the decimal separator, a floating-point number from the database may get changed by calc (and therefore lost from the spreadsheet) into a date.", };
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"If only the portrait-sized photo has been downloaded, rotating into landscape will clear the screen and cause the landscape-sized photo to be downloaded.",
				"For example, when adding a new patient, Fred Flintstone, it comes up with 229 results in our system, including many names which are not even remotely similar.",
				"When the Delete operation has finished it will release the named lock and the List operation will be unblocked and it will put the volume in the cache without checking if it still exists after acquiring the lock on the name.",
				"Basically, in VolumeStore when there's a Delete operation holding the named lock for a volume and a List operation has just finished listing volumes, but hasn't attempted to add them to the cache yet the list operation will try to acquire the named lock and block",
				"When a directory or a file is requested in mod_proxy_ftp, and the user has not hinted what kind of resource it is(usual), the ftp client in mod_proxy_ftp tries the command SIZE first, and depending on its output, CWD or RETR.",
				"when you have a filename which consists of multibyte characters, this is cut by mod_autoindex also after 20 bytes.",
				"If you reload the page at http://dg.kendersoft.org a few times (or surf to links on the left) you will eventually see that the navigation and main black tables will stick to the inside edge of the 'main' table. ",
				"When user first registers our application and is redirected to the  Request Permission  page_ it will then authenticate with the above URL with http error 400 (Bad request) page after clicking on the  Allow  button.",
				"when i use FOP to generate my report it took 3 second but in birt it took 45 seconds that alot of difference 45 seconds exclude birt engine start up and the report already optimize in layout to avoid slowness only needed field are bind over the xml to the data binding and tables",
				"However when I moved a whole directory from o ne part of the webproject to another I found copies in place places, the new destination and the old source location.",
				"If the changes are saved to void the address, and a reason is entered in the reason voided box that appears under the person name, later when you click to void the patient name, the reason voided box is already populated with the reason you entered for voiding the address.", };
		TestUtils.testSentences(sentences, pm, 1);

		@SuppressWarnings("unused")
		String[] trickySentences = {
				"2 If they are using Netscape 4.79 they get a \"network error\" OK dialog box come up, even though the page they were trying to navigate to actually comes up beneath." };
	}

}
