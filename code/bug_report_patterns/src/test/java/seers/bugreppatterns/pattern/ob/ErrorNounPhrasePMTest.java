package seers.bugreppatterns.pattern.ob;

import org.junit.Test;
import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ErrorNounPhrasePMTest extends BaseTest {

    public ErrorNounPhrasePMTest() {
        pm = new ErrorNounPhrasePM();
    }

    @Test
    public void testNegative() throws Exception {
        String[] negatives = {"5 observe: once again you can set \"referenced projects\".",
                "Currently it is:"};

        TestUtils.testSentences(negatives, pm, 0);
    }
}
