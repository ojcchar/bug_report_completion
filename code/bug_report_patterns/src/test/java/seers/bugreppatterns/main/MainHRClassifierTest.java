package seers.bugreppatterns.main;

import org.junit.Test;

public class MainHRClassifierTest {

	@Test
	public void testMain() throws Exception {
		String systems = "eclipse,facebook,firefox,httpd,docker,hibernate,libreoffice,openmrs,wordpress-android";
		String dataFolder = "test_data/data";
		String pattersFile = "patterns.csv";
		// dataFolder = "test_data/data2";
		// systems = "eclipse";

		String[] granularities = { "B", "P", "S" };

		for (String g : granularities) {
			String[] args = { dataFolder, g, systems, "test_data/output",
					MainHRClassifier.Predictor.ANY_MATCH.toString(),
					pattersFile };
			MainHRClassifier.main(args);
		}
	}

}
