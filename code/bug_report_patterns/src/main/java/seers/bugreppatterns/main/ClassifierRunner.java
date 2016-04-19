package seers.bugreppatterns.main;

import seers.bugreppatterns.main.MainHRClassifier.Predictor;

public class ClassifierRunner {

	public static void main(String[] args) throws Exception {

		String systems = "eclipse,facebook,firefox,httpd";
		String dataFolder = "test_data/data";
		// dataFolder = "test_data/data2";
		// systems = "firefox";
		// String[] grans = { "B", "P", "S" };
		String[] grans = { "B" };
		// String[] grans = { "P" };
		Predictor predictionMethod = Predictor.TREE;
		String patternsFile = "patterns-prediction.csv";
		for (String gran : grans) {
			String[] args2 = { dataFolder, gran, systems, "test_data/output", predictionMethod.toString(),
					patternsFile };
			MainHRClassifier.main(args2);
		}
	}
}