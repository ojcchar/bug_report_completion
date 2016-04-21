package seers.bugreppatterns.pattern.sr;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.main.BaseTest;

/**
 * Created by juan on 4/18/16.
 */
public class ContinuousPresentPMTest extends BaseTest {
	public ContinuousPresentPMTest() {
		pm = new ContinuousPresentPM();
	}

	@Test
	public void testAdditionals() throws Exception {
		String[] txts = {
				"I am authenticating using. $appapikey =  XXX ;\n$appsecret =  XXX ;\n$facebook = new Facebook($appapikey_ $appsecret);\n$fb_user\t=$facebook-&gt;get_loggedin_user();",
				"then calling. \n\n$details = $facebook-&gt;api_client-&gt;users_getInfo($fb_user_array( first_name _ last_name _ pic_square _ birthday_date _ sex _ username _ current_location ));" };

		boolean fail = false;
		for (int i = 0; i < txts.length; i++) {
			String txt = txts[i];
			Paragraph paragraph = parseParagraph(txt);
			int m = pm.matchParagraph(paragraph);
			if (m != 1) {
				System.out.println("\n Fail for (" + i + "): \"" + txt + "\"");
				fail = true;
			}
		}

		assertFalse(fail);
	}
}
