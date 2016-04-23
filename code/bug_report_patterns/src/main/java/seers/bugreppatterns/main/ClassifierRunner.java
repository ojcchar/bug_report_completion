package seers.bugreppatterns.main;

import java.io.File;

import seers.bugreppatterns.main.MainHRClassifier.Predictor;

public class ClassifierRunner {

	public static void main(String[] args) throws Exception {

		String systems = "eclipse,facebook,firefox,httpd";
		String dataFolder = "test_data" + File.separator + "data";
		String outputFolder = "test_data" + File.separator + "output";
		// systems = "firefox";
		// String[] grans = { "B", "P", "S" };
		String[] granularities = { "S" };
		Predictor predictionMethod = Predictor.COMBIN;
		String patternsFile = "patterns-prediction.csv";
		for (String gran : granularities) {
			String[] args2 = { dataFolder, gran, systems, outputFolder, predictionMethod.toString(), patternsFile };
			MainHRClassifier.main(args2);
		}
	}
}
