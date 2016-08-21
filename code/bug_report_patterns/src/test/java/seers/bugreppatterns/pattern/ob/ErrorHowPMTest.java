package seers.bugreppatterns.pattern.ob;

import org.junit.Test;
import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ErrorHowPMTest extends BaseTest {
    public ErrorHowPMTest() {
        pm = new ErrorHowPM();
    }

    @Test
    public void testNegative() throws Exception {
        String[] negs = {"this error comes up",
                "Actual result:\nException is thrown out.",
                "The issues causing these errors to be reported are also causing all tests in " +
                        "TestPluginMainValidator to fail.",
                "A javascript error shows up_ and the whole control is gone.",
                "The thread safety issue is caused by having multiple threads that share a state " +
                        "in a non-thread safe {{HashSet}}.",
                "The following error is reported in the error log:"};

        TestUtils.testSentences(negs, pm, 0);
    }
}
