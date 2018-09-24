package seers.bugreppatterns.main.golset;

import org.junit.Test;
import seers.bugreppatterns.main.validation.MainMatcher;

import java.io.File;

import static org.junit.Assert.*;

public class GoldSetGeneratorTest {

    @Test
    public void main() throws Exception {

        String codedDataFile = MainMatcher.fileCodedData;
        String outputFolderPrefix = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\generated_goldsets"+ File.separator;
        String davisDataPath = "C:\\Users\\ojcch\\Documents\\Repositories\\Git\\bug_report_completion\\code" +
                "\\bug_report_patterns\\all_data_only_bugs_davies.csv";
        String includeNonCodedInstances = "n";
        String bugsDataFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\replication_package_fse17\\1_data" +
                "\\1_coded_data\\1_pattern_labeled_data\\hibernate";
        String includeDaviesData = "n";

        String[] args={codedDataFile, outputFolderPrefix, davisDataPath, includeNonCodedInstances, bugsDataFolder, includeDaviesData};
        GoldSetGenerator.main(args);
    }
}