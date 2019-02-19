package seers.bugreppatterns.main.preprocessing;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BugContentCategoryTest {

    @Test
    public void isValidTag() {

        boolean validTag = BugContentCategory.isValidTag("[PROJ_TEMPLATE]");
        assertTrue(validTag);


        validTag = BugContentCategory.isValidTag("PROJ_TEMPLATE]");
        assertFalse(validTag);

        validTag = BugContentCategory.isValidTag("  [PROJ_TEMPLATE]");
        assertFalse(validTag);
    }
}