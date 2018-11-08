package seers.bugreppatterns.main.preprocessing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum BugContentCategory {
    PROJ_TEMPLATE, SRC_CODE, EXEC_TRACES, PROG_LOGS, IDENTIFIER, EXCEPTION, URL;

    public static boolean isValidTag(String text) {
        return getTag(text) != null;
    }

    public static BugContentCategory getTag(String text) {
        Pattern p = Pattern.compile("^\\[(.+)\\]$");
        final Matcher matcher = p.matcher(text);
        if (matcher.find()) {
            final String val = matcher.group(1);
            try {
                return BugContentCategory.valueOf(val);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    public String getTagText() {
        return "[" + this + "]";
    }
}
