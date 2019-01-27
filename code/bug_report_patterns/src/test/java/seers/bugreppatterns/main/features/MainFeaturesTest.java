package seers.bugreppatterns.main.features;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainFeaturesTest {

    @Test
    public void main() throws Exception {

        /*String goldSetFolderPrefix = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\pattern_features_11082018\\auto_all_data";
        String outputFolderPrefix = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\pattern_features_11082018" +
                "\\auto_all_data_features";*/

      /*  String goldSetFolderPrefix = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\1_data\\3_pattern_features";
        String outputFolderPrefix = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\1_data\\3_pattern_features\\features";*/


        String goldSetFolderPrefix = "C:\\Users\\ojcch\\Documents\\Repositories\\Git\\Android-Bug-Report-Reproduction" +
                "\\EulerEvaluation\\Data\\2_nimbus_pattern_features\\prefeatures";
        String outputFolderPrefix = "C:\\Users\\ojcch\\Documents\\Repositories\\Git\\Android-Bug-Report-Reproduction" +
                "\\EulerEvaluation\\Data\\2_nimbus_pattern_features\\features";

        String ourData = "y";

        String[] args = {goldSetFolderPrefix, outputFolderPrefix, ourData};
        MainFeatures.main(args);
    }
}