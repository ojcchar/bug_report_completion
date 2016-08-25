package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class AssumePMTest extends BaseTest {
	public AssumePMTest() {
		pm = new AssumePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negs = {
				"Now if you restart the Docker daemon, Docker thinks the container is gone (I assume because the PID is gone) and then it will start a new one but the old container cgroup still exists.",
				"I assume this wiki page just isn't maintained any more so maybe you want to link to a more recent plan?",
				"I am assuming this wiki page just isn't maintained any more so maybe you want to link to a more recent plan?",
				"Docker is not assuming that there is a container" };

		TestUtils.testSentences(negs, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] negs = { "Docker is assuming that there is a container", };

		TestUtils.testSentences(negs, pm, 1);
	}

}
