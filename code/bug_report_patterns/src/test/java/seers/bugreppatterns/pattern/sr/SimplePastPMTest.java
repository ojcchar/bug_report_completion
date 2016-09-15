package seers.bugreppatterns.pattern.sr;

import org.junit.Test;
import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.sr.SimplePastPM;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 9/14/16.
 */
public class SimplePastPMTest extends BaseTest {
    {
        pm = new SimplePastPM();
    }

    @Test
    public void testPositive() throws Exception {
        String[] texts = {"I just tried out the Patient Summary module in a demo and I got the stack trace below."};

        TestUtils.testSentences(texts, pm, 1);
    }
}