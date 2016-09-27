package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 9/26/16.
 */
public class ConditionalObservedBehaviorPMTest extends BaseTest {
    {
        pm = new ConditionalObservedBehaviorPM();
    }

    @Test
    public void testPositives() throws Exception {
        String[] texts = {
                "If I try to add this image to a gallery, the app crashes.",
                "When I visit stats, the progress indicator at the top of the screen below the title bar sometimes doesn't stop.",
        };

        TestUtils.testSentences(texts, pm, 1);
    }
}