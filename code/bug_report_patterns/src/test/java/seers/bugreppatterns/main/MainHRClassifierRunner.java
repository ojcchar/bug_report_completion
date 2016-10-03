package seers.bugreppatterns.main;

public class MainHRClassifierRunner {

	public static void main(String[] args) throws Exception {
		String systems = "eclipse,facebook,firefox,httpd,docker,hibernate,libreoffice,openmrs,wordpress-android";
		String dataFolder = "test_data/data";
		String pattersFile = "patterns.csv";
		// dataFolder = "test_data/data2";
		// systems = "eclipse";

		String[] granularities = { "B", "P", "S" };

		for (String g : granularities) {
			String[] args2 = { dataFolder, g, systems, "test_data/output",
					MainHRClassifier.Predictor.ANY_MATCH.toString(),
					pattersFile };
			MainHRClassifier.main(args2);
		}
	}

}
