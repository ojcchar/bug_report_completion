package seers.bugreppatterns.pattern.ob;

import org.junit.Test;
import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class VerbErrorPMTest extends BaseTest {
    public VerbErrorPMTest() {
        pm = new VerbErrorPM();
    }

    @Test
    public void testNegatives() throws Exception {
        String[] n = {"Currently, you can select (in the problem filter):",
                // These two are NEG_ADJ_ADV, not VERB_ERROR
                "### Actual behavior\n" +
                        "Right now `timestamp`, being used as a sorting criteria, even when " +
                        "sometimes a value other than an actual timestamp is assigned to such " +
                        "`timestamp` attribute, which is misleading.",
                "Person.ageAge currently figures the age down to the hours, minutes, and seconds " +
                        "which returns the wrong age when the birthday time is after the onDate " +
                        "time."};

        TestUtils.testSentences(n, pm, 0);
    }
}
