package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class SameProblemWhenPMTest extends BaseTest{

	
	public SameProblemWhenPMTest(){
		pm = new SameProblemWhenPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negatives = {"Bad processing hyperlink with anchor in DOCX","Note: This bug appears at the same time as bug54021.","When I test a boolean field that contain no value (neither YES nor NO) with &lt;&gt; 1 (I suppose it's the same than NOT YES, LO returns FALSE."};

		TestUtils.testSentences(negatives, pm, 0);
		
	}
}