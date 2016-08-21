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
                "### Actual behavior\n" +
                        "Right now `timestamp`, being used as a sorting criteria, even when " +
                        "sometimes a value other than an actual timestamp is assigned to such " +
                        "`timestamp` attribute, which is misleading.",
                "However when I moved a whole directory from one part of the webproject to " +
                        "another I found copies in place places, the new destination and the old " +
                        "source location."};

        TestUtils.testSentences(n, pm, 0);
    }
}
