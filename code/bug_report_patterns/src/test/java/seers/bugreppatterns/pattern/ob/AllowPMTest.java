package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class AllowPMTest extends BaseTest {
	public AllowPMTest() {
		pm = new AllowPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {
				"Unfortunately the current implementation forces us to keep using the old unescaped URLs, and does not allow all valid (=\"encoded\"/\"escaped\") URIs.",
				" +++ This bug was initially created as a clone of Bug #305858 +++ &gt; Apart from the NPE there seems to be no good reason why null should not be &gt; allowed.",
				"There's a problem with relaxing scheduling rules, and allowing some builders to return null. ",
				"It does not allow for enter or <br>", "I wanted the t=title to allow me to show a  sharing title .",
				"It seems like IE and Safari are not allowing the authentication to take place in the iframe.",
				"When user first registers our application and is redirected to the  Request Permission  page_ it will then authenticate with the above URL with http error 400 (Bad request) page after clicking on the  Allow  button.",
				"The  Edit  link underneath the tabs appears as if it will allow the tabs to be reordered.",
				"You can  drag  the tab up or down_ but it will not allow placement.",
				"Edit Tabs on Pages not allowing reordering",
				"user gets really long error message that wont allow 'copy &amp; paste'   :-(",
				"Expected Results:  allowed popups",
				"Firefox forgets which sites are allowed to create popups after extended use. complete restart temporarily fixes problem",
				"popups occuring form one site on the allow list (www.we-love-anime.com/forum great anime forum...) on an average of 1 every one to two minutes.",
				"Some place there must be something to change that will allow me to use my new email address.",
				"The pasting should be allowed with the user's permission.",
				"Pasting in rich text editor context menus is not allowed.",
				"when i have access control like &lt;LocationMatch ^/engine/request/&gt; Order Deny,Allow Allow from all" };

		TestUtils.testSentences(sentences, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"When focus is in the editor, which allows the refresh action to be executed, the refresh action in debugger views is enabled as a side effect.",
				"it does allow to merge" };

		TestUtils.testSentences(sentences, pm, 1);
	}

}
