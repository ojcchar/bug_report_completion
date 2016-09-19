package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 4/18/16.
 */
public class ContinuousPresentPMTest extends BaseTest {

    public ContinuousPresentPMTest() {
        pm = new ContinuousPresentPM();
    }

    @Test
    public void testAdditionals() throws Exception {
        String[] txts = {
                "When the method {{list}} of a query ({{QueryImpl.java}}) is getting called, Hibernate delegates to {{SessionImpl.list()}}. After having loaded the list, {{SessionImpl.list()}} calls {{SessionImpl.afterOperation()}}, which calls {{jdbcContext.afterNontransactionalQuery()}} (as there is no active transaction). This leads to {{ConnectionManager.afterTransaction()}} which releases the JDBC connection. This is exactly what I expect to happen.",
                "Then this created docker images is used as the base image for another docker " +
                        "file where I do npm install and then remove the build tools like python, " +
                        "make and g++ after npm install. Following is the docker file\nFROM " +
                        "dockerbase:latest\nMAINTAINER xxx@xxx.com \nCOPY common /opt/project1324/" +
                        "common\nRUN /usr/bin/npm install\nRUN apk del python\nRUN apk del make\n" +
                        "RUN apk del g++",
                "I have an image whose size is roughly 800M.\nI start a container on it using\n" +
                        "`docker run -i -t &lt;myimage&gt;`\nThen i do `du -shx /` inside.",
                "I am authenticating using. $appapikey =  XXX ;\n$appsecret =  XXX ;\n$facebook = new Facebook($appapikey_ $appsecret);\n$fb_user\t=$facebook-&gt;get_loggedin_user();",
                "then calling. \n\n$details = $facebook-&gt;api_client-&gt;users_getInfo($fb_user_array( first_name _ last_name _ pic_square _ birthday_date _ sex _ username _ current_location ));"};

        TestUtils.testParagraphs(txts, pm, 1);

    }

    @Test
    public void testNegatives() throws Exception {
        String[] txts = {
                "I'm working on a pretty large file, authoring for PacktPub, and I'm running in to a pretty consistent issue.",};

        TestUtils.testParagraphs(txts, pm, 0);

    }
}
