package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class FrequencyAdverbPMTest extends BaseTest {

	public FrequencyAdverbPMTest() {
		pm = new FrequencyAdverbPM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"Ever since I turned the LogLevel from 'warn' to 'info', I've received thousands of the following messages in httpd-error.log",
				"Within the last hour the image that is normally posted with the feed started to be blocked.",
				"It is normally already uri-escaped by the browser, and double-escaping it will break the link.",
				"Module paths are always prepended with the the context path and a \"/\".",
				"The Concept.getName(Locale) method is called frequently." };

		TestUtils.testSentences(sentences, pm, 1);
	}

}
