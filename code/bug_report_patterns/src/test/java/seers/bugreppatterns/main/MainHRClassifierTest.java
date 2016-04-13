package seers.bugreppatterns.main;

public class MainHRClassifierTest {

	// @Test
	public void testMain() throws Exception {
		String systems = "eclipse,facebook,firefox,httpd";
		String dataFolder = "test_data/data";
		// dataFolder = "test_data/data2";
		// systems = "eclipse";
		String[] args = { dataFolder, "S", systems, "test_data/output" };
		MainHRClassifier.main(args);
	}

}
