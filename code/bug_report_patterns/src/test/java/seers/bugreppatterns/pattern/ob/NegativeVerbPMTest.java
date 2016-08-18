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
        String[] negs = {"Currently the live-stream box has an initial message that states " +
                "update your status on this event .",
                "Currently, the login information is kept in the server, so each time you do a " +
                        "docker login,\nthe client requests the default username/email from the " +
                        "server, send send the new configuration to the docker server which " +
                        "communicates with the index.",
                "now since before the creation of the second container the command in 3 return " +
                        "the Ubuntu container and after it the Redis container return this seems " +
                        "to me like an ambiguity (and if you delete the redis you get the ubuntu " +
                        "again after the inspect command)",
                "Currently, if you try to use the MessageService to send an email, the body of " +
                        "the email is always set to \"text/plain\"."};

        TestUtils.testSentences(negs, pm, 0);
    }
}
