package seers.bugreppatterns.main.prediction;

import java.io.File;

import seers.bugreppatterns.main.prediction.HeuristicsClassifier.CooccurringFeaturesOption;
import seers.bugreppatterns.main.prediction.HeuristicsClassifier.Predictor;

public class ClassifierRunner {

	public static void main(String[] args) throws Exception {

		String systems = "eclipse,facebook,firefox,httpd,docker,hibernate,libreoffice,openmrs,wordpress-android";
		// String systems = "docker";
		String dataFolder = "test_data" + File.separator + "data";
		String outputFolder = "test_data" + File.separator + "output2";
		String configFolder = "test_data";
		String pattersFile = "patterns_no_OB.csv";
		CooccurringFeaturesOption cooccurrOption = CooccurringFeaturesOption.ONLY_COOCCURRING;
		String goldSetPath = "gold-set-B.csv";
		String addCooccuringPatternsForPrediction = "y";
		String cooccurFileSuffix = "rules";

		// String[] granularities = { "B", "P", "S" };

		String[] granularities = { "B" };

		for (String g : granularities) {
			String[] args2 = { dataFolder, g, systems, outputFolder, Predictor.COOCCUR.toString(), pattersFile,
					configFolder, cooccurrOption.toString(), goldSetPath, addCooccuringPatternsForPrediction,
					cooccurFileSuffix };
			MainHRClassifier.main(args2);
		}
	}

}
