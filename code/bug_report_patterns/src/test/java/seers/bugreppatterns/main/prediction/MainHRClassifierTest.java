package seers.bugreppatterns.main.prediction;

import org.junit.Test;

public class MainHRClassifierTest {

    @Test
    public void main() throws Exception {


        /*String dataFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\replication_package_fse17\\1_data" +
                "\\1_coded_data\\0_labeled_data";*/
       /* String dataFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\replication_package_fse17" +
                "\\1_data\\2_preprocessed_data\\4_content_tagging_preprop2_labels_fixed-10302018";
        String outputFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\pattern_features_11082018\\auto_all_data";

        String bugGoldSetPath = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\replication_package_fse17\\1_data" +
                "\\1_coded_data\\2_labeled_data_summary.csv";
        String granularity = "SP-F";

        String systems = "docker,eclipse,facebook,firefox,hibernate,httpd,libreoffice,openmrs,wordpress-android";*/
        String dataFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\1_data" +
                "\\1_preprocessed_data\\1_content_tagging_prep-01102019";
        String outputFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\1_data\\3_pattern_features";
        String bugGoldSetPath = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\1_data\\bug_list.csv";
        String systems = "eclipse,facebook,firefox,libreoffice,openmrs,wordpress-android,argouml,jedit,openoffice";


        String granularity = "SP-F";
        String predictionMethod = HeuristicsClassifier.Predictor.ANY_MATCH.toString();
        String pattersFile = "patterns.csv";
        String configFolder = "";
        String cooccurrOption = HeuristicsClassifier.CooccurringFeaturesOption.ONLY_INDIV.toString();
        String addCooccuringPatternsForPrediction = "n";
        String cooccurFileSuffix = "";

        String[] args = {dataFolder, granularity, systems, outputFolder, predictionMethod, pattersFile, configFolder,
                cooccurrOption, bugGoldSetPath, addCooccuringPatternsForPrediction, cooccurFileSuffix};
        MainHRClassifier.main(args);
    }
}