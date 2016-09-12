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
        String[] paragraphs = {
                // These seem to be miscoded
                "1. inside a method type \"new Runnable(\"\n" +
                        "2. press Ctrl+SPACE\n" +
                        "==> no method comment for run()",
                "1. add some TODO comments in html files\n" +
                        "2. in task tags preference page, enable searching for task tags (make sure enabled for html content type)\n" +
                        "3. task tags are found and reported in tasks view\n" +
                        "4. go back to task tags preference page and in the filters tab, disable scanning html content type\n" +
                        "5. apply & clean and redetect tasks\n" +
                        "6. ** task tags are still in tasks view and marked in files (though any new todo comments are no longer turned into task tags)",
                "1. Visit Show/hide blogs area.\n" +
                        "2. While list is refreshing, untick any of the checkboxes.\n" +
                        "3. After the refresh completes, your tick will reset to being checked.",
                "1. Go to the Media Library\n" +
                        "2. Tap the `+` button to add a new picture\n" +
                        "3. Either select \"Capture\" or \"Select from local library\"\n" +
                        "4. Nothing happens: the picture is not uploaded, no Toast error",

                "1 New Text Document.\n" +
                        "2 Insert -> Footer -> Default Style.\n" +
                        "3 Table -> Insert Table -> 1x1.\n" +
                        "4 Place cursor after table.\n" +
                        "5 Type 3 \"_\" and return (if Apply Border set in AutoCorrect Options).\n" +
                        "6 Place cursor in paragraph above line.\n" +
                        "7 Type \"test\".\n" +
                        "8 Save, close, and reopen document.\n" +
                        "Alternate:\n" +
                        "5 Type \"test\".\n" +
                        "6 Format -> Paragraph -> Borders -> add bottom line in User-defined box.\n" +
                        "7 Save, close, and reopen document.\n" +
                        "Result:\n" +
                        "\"test\" and line are overlapping each other.\n" +
                        "There is also a red triangle.",
                "1. inside a method type \"new Runnable(\"\n" +
                        "2. press Ctrl+SPACE\n" +
                        "==> no method comment for run()"};
        TestUtils.testParagraphs(paragraphs, pm, 1);
    }
}
