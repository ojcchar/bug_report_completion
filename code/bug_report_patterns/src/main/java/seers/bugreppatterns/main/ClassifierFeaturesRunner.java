package seers.bugreppatterns.main;

public class ClassifierFeaturesRunner {

	public static void main(String[] args) throws Exception {

		String systems = "eclipse,facebook,firefox,httpd";
		String dataFolder = "test_data/data";
		// dataFolder = "test_data/data2";
		// systems = "eclipse";
		String[] grans = { "B"
				// , "P", "S"
		};
		for (String gran : grans) {
			String[] args2 = { dataFolder, gran, systems, "test_data/output" };
			MainHRClassifier.main(args2);
		}
	}
}
