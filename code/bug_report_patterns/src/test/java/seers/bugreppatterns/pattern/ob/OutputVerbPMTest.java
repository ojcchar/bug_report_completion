package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 8/11/16.
 */
public class OutputVerbPMTest extends BaseTest {
	{
		pm = new OutputVerbPM();
	}

	@Test

	public void testPositives() throws Exception {
		String[] txts = {
				"In this example the `strace` output shows that Entrypoint: [/bin/bash,-l]` was sent to the server, but inspecting the resulting image shows `Entrypoint: null`.", 
				"2 Microsoft security essentials will pop up with the attached screenshot"};
		TestUtils.testSentences(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = { "The output is\noriginal: http://host/my%20path/\nparsed:   http://host/my%2520path/",
				"Persistence.generateSchema - the output file contains duplicated DDL statements",
				"sometimes favicons in url results are blank, need to use the favicon service",
				"Using LibreOffice Draw 4.1.0.4 in Fedora 19 x86 64bit or Widnows 7 64bit, EPS clipart is not output in printing or PDF export.",
				"Sample code to attach to a canvas page input field is below_ output display goes to elements with IDs keydown_ keyup_ and keypress.",
				"the correct output is: class com.ws.model.entities.Mensagem",
				"the result must be retrieved in 2 dimensional array of Object(Object[][])" };
		TestUtils.testSentences(txts, pm, 0);
	}

}
