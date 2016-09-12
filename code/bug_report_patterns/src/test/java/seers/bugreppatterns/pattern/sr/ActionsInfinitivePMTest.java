package seers.bugreppatterns.pattern.sr;

import org.junit.Test;
import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 9/12/16.
 */
public class ActionsInfinitivePMTest extends BaseTest {
    {
        pm = new ActionsInfinitivePM();
    }

    @Test
    public void testPositive() throws Exception {
        String[] paragraphs = {"- platform-ui module loaded from head\n" +
                "- open the manifest editor for org.eclipse.ui\n" +
                "- select the editorActions extension point\n" +
                "- search for references from the context menu\n" +
                "- open the resulting matches several times\n" +
                "- a new editor is opened each time",
                "0. Fresh install; fresh workspace.\n" +
                        "1. Start eclipse.exe\n" +
                        "2. Exit eclipse (to make sure there is a platform.cfg)\n" +
                        "3. Add a link to an installed extension\n" +
                        "4. Start eclipse.exe\n" +
                        "5. Say Yes when it asks if you want to open update manager\n" +
                        "6. select all the incoming features and say you want them"};
        TestUtils.testParagraphs(paragraphs, pm, 1);
    }
}
