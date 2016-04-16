package seers.bugreppatterns.main;

import seers.bugreppatterns.main.MainHRClassifier.Predictor;

public class ClassifierFeaturesRunner {

	public static void main(String[] args) throws Exception {

		String systems = "eclipse,facebook,firefox,httpd";
		String dataFolder = "test_data/data";
		// dataFolder = "test_data/data2";
		// systems = "eclipse";
		String[] grans = { "B", "P", "S" };
		Predictor predictionMethod = Predictor.OR_OPER;
		for (String gran : grans) {
			String[] args2 = { dataFolder, gran, systems, "test_data/output", predictionMethod.toString() };
			MainHRClassifier.main(args2);
		}
	}
}
