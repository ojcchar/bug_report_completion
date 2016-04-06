package seers.bugreppatterns.main;

import org.junit.Test;

public class MainHRClassifierTest {

	@Test
	public void testMain() throws Exception {
		String systems = "eclipse,facebook,firefox,httpd";
		systems = "firefox";
		String[] args = { "test_data/data", "S", systems, "test_data/output", "test_data/patterns/patterns_file.txt" };
		MainHRClassifier.main(args);
	}

}
