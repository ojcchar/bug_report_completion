package seers.bugreppatterns.main.features;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainFeaturesTest {

    @Test
    public void main() throws Exception {

        String goldSetFolderPrefix = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\pattern_features\\auto_no_code_data";
        String outputFolderPrefix = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\pattern_features" +
                "\\auto_no_code_data_features";
        String ourData = "y";

        String[] args = {goldSetFolderPrefix, outputFolderPrefix, ourData};
        MainFeatures.main(args);
    }
}