package seers.bugreppatterns.main.statistics;

import org.apache.commons.io.FileUtils;
import seers.appcore.csv.CSVHelper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatternDataExtractorForStats {

    static String inputFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\pattern_features_11082018" +
            "\\auto_all_data\\output-patterns-SP-F.csv";

    public static void main(String[] args) throws IOException {

        StringBuilder builder = new StringBuilder();
        final List<List<String>> lines = CSVHelper.readCsv(inputFolder, false);

        for (List<String> line : lines) {

            final String sys = line.get(0);
            final String bug = line.get(1);
            final String sent = line.get(2);
            final String parg = sent.split("\\.")[0];
            List<String> patterns = new ArrayList<>();
            if (line.size() > 3) {
                patterns = line.subList(3, line.size());
            } else {
                patterns.add("");
                patterns.add("");
            }

            for (int i = 0; i < patterns.size(); i++) {

                if (i % 2 == 1) continue;
                String pattern = patterns.get(i);

                final List<String> fields = Arrays.asList(sys, bug,
                        String.join(",", sys, bug),
                        parg,
                        String.join(",", sys, bug, parg),
                        sent,
                        String.join(",", sys, bug, sent),
                        pattern,
                        getType(pattern)
                );
                final String newLine = String.join("\";\"", fields);
                builder.append("\"");
                builder.append(newLine);
                builder.append("\"");
                builder.append("\n");


            }
        }


        File outFile = Paths.get(new File(inputFolder).getParent(), "patterns_table.csv").toFile();
        FileUtils.write(outFile, builder.toString(), Charset.defaultCharset());


    }

    private static String getType(String pattern) {
        if (pattern.contains("_OB_")) return "ob";
        if (pattern.contains("_EB_")) return "eb";
        if (pattern.contains("_SR_")) return "s2r";
        return "o";
    }
}
