package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class NegativeVerbPMTest extends BaseTest {

    public NegativeVerbPMTest() {
        pm = new NegativeVerbPM();
    }

    @Test
    public void testNegative() throws Exception {
        String[] negs = {"Currently the live-stream box has an initial message that states update your status on this event .",
                "Currently, the login information is kept in the server, so each time you do a docker login,\nthe client requests the default username/email from the server, send send the new configuration to the docker server which communicates with the index.",
                "now since before the creation of the second container the command in 3 return the Ubuntu container and after it the Redis container return this seems to me like an ambiguity (and if you delete the redis you get the ubuntu again after the inspect command)",
                "Currently, if you try to use the MessageService to send an email, the body of the email is always set to \"text/plain\".",
                "2.0M3 If a Node Mapping has two Feature Label Mappings, only the first is processed to resolve its View Pattern, the second retains its default text.",
                "When I use Shift+Tab to decrease indentation level of selected C source lines, these selected C source lines are scrolled up or down (depends on its position on screen), why?",
                "If the directory gets closed and opened again the right contents will show up.",
                "When the Delete operation has finished it will release the named lock and the List operation will be unblocked and it will put the volume in the cache without checking if it still exists after acquiring the lock on the name.",
                "Indeed, when I run `docker inspect xxx_default` I got:"};

        TestUtils.testSentences(negs, pm, 0);
    }
    
    @Test
	public void testPositive() throws Exception {
		String[] sentences = { "The floating-point data is then forever lost",
				"Documentation for Running a test is outdated",
				"However, when switching to an RTL interface (such as Hebrew) the buttons' icons are reversed.",
				"Digging in heap dumps to solve a memory leak problem I found that some memory was wasted by multiple instances of String holding the exact same sql query in the EntityLoader class (the 'sql' instance variable).",
				"The Browser is simply gone.",
				"This possibility has been gone since LO 3.6.",
				"From my own experience and those of my users I ve found that ad blocking software can block the Add Application page by default (e.g. IE7 Pro s Ad Blocker). ",
				"Container can kill docker daemon",
				"geo location gets cleared on updating the post",
				"5 Update/publish, the post geo tag gets cleared (verified on the site)"
				};

		TestUtils.testSentences(sentences, pm, 1);
	}
}
