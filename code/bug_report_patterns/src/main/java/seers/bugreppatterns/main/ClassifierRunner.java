package seers.bugreppatterns.main;

import java.io.File;

public class ClassifierRunner {

	public static void main(String[] args) throws Exception {

//		String systems = "eclipse,facebook,firefox,httpd,docker,hibernate,libreoffice,openmrs,wordpress-android";
		String systems = "docker";
		String dataFolder = "test_data" + File.separator + "data";
		String outputFolder = "test_data" + File.separator + "output";

		String pattersFile = "patterns.csv";

		// String[] granularities = { "B", "P", "S" };

		String[] granularities = { "B" };

		for (String g : granularities) {
			String[] args2 = { dataFolder, g, systems, outputFolder, MainHRClassifier.Predictor.ANY_MATCH.toString(),
					pattersFile };
			MainHRClassifier.main(args2);
		}
	}
	
}
