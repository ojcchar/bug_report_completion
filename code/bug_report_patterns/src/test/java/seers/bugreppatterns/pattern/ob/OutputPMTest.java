package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class OutputPMTest extends BaseTest{

	public OutputPMTest(){
		pm = new OutputPM();
	}
	
	@Test
	public void testNegative() throws Exception {
		String[] negatives = {"Persistence.generateSchema - the output file contains duplicated DDL statements",
				"In this example the `strace` output shows that Entrypoint: [/bin/bash,-l]` was sent to the server, but inspecting the resulting image shows `Entrypoint: null`.",
				"Using LibreOffice Draw 4.1.0.4 in Fedora 19 x86 64bit or Widnows 7 64bit, EPS clipart is not output in printing or PDF export.",
				"Sample code to attach to a canvas page input field is below_ output display goes to elements with IDs keydown_ keyup_ and keypress.",
				"the correct output is: class com.ws.model.entities.Mensagem",
				"the result must be retrieved in 2 dimensional array of Object(Object[][])"
				};

		TestUtils.testSentences(negatives, pm, 0);
		
		@SuppressWarnings("unused")
		String[] trickySentences = {
				"sometimes favicons in url results are blank, need to use the favicon service",};
		
	}
}
