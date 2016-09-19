package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class SimplePresentPMTest extends BaseTest {
    public SimplePresentPMTest() {
        pm = new SimplePresentPM();
    }

    @Test
    public void testNegative() throws Exception {
        String[] sentences = {};

        TestUtils.testSentences(sentences, pm, 0);
    }

    @Test
    public void testPositive() throws Exception {
        String[] sentences = {
                "created sql is (old parser) :",
                "\"Apache caches response to a request with no-store\ncache-control directive.\"",
                "Close all editors brings up hierarchy of object",
                "Extension point reference matches open new editor each time",
                "Default Button in Initialization Wizard is \"Back\" (again)"
        };

        TestUtils.testSentences(sentences, pm, 1);
    }
}
