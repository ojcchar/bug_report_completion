package seers.bugreppatterns.main.prediction;

import org.apache.commons.lang3.EnumUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainHRClassifierTest {

    @Test
    public void main() throws Exception {


        String dataFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\replication_package_fse17\\1_data" +
                "\\2_preprocessed_data\\0_code_removal_only";
        String granularity = "SP-F";
        String systems = "docker,eclipse,facebook,firefox,hibernate,httpd,libreoffice,openmrs,wordpress-android";
        String outputFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\pattern_features\\auto_no_code_data";
        String predictionMethod = HeuristicsClassifier.Predictor.ANY_MATCH.toString();
        String pattersFile = "patterns.csv";
        String configFolder = "";
        String cooccurrOption =
                HeuristicsClassifier.CooccurringFeaturesOption.ONLY_INDIV.toString();
        String goldSetPath = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\generated_goldsets\\gold-set-B.csv";
        String addCooccuringPatternsForPrediction = "n";
        String cooccurFileSuffix = "";

        String[] args= {dataFolder, granularity, systems,outputFolder, predictionMethod, pattersFile, configFolder,
                cooccurrOption, goldSetPath, addCooccuringPatternsForPrediction, cooccurFileSuffix};
        MainHRClassifier.main(args);
    }
}