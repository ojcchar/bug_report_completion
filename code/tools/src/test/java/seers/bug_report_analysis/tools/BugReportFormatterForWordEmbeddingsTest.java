package seers.bug_report_analysis.tools;

import org.joda.time.DateTime;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BugReportFormatterForWordEmbeddingsTest {

    @Test
    public void getTagValue() {

        String content = "<Issue>\n" +
                "\t<title>MAJOR Brush issue </title>\n" +
                "\t<bugID> Issue #10 </bugID>\n" +
                "\t<url>https://github.com/simtr/The-Powder-Toy/issues/10</url>";
        String val = BugReportFormatterForWordEmbeddings.getTagValue(content, "title");

        assertEquals("MAJOR Brush issue", val);

        //-------------

        content = "\n" +
                "\t<number_commters>2</number_commters>\n" +
                "\t<openTime>Thu May 05 21:23:35 CST 2016</openTime>\n" +
                "\t<closedTime>Thu May 05 22:03:09 CST 2016</closedTime>\n" +
                "\t<description>";
        val = BugReportFormatterForWordEmbeddings.getTagValue(content, "openTime");

        assertEquals("Thu May 05 21:23:35 CST 2016", val);
    }


    @Test
    public void parseDate() throws ParseException {

        String content = "Thu May 05 21:23:35 CST 2016";
        String pattern = "EEE MMM dd HH:mm:ss zzz yyyy";
        DateTime val = BugReportFormatterForWordEmbeddings.parseDate(content, pattern);

        assertNotNull(val);
    }
}