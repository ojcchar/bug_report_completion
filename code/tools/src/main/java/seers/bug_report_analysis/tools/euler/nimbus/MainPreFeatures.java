package seers.bug_report_analysis.tools.euler.nimbus;

import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import org.apache.commons.io.FilenameUtils;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.bugreppatterns.main.prediction.HeuristicsClassifier;
import seers.bugreppatterns.main.prediction.MainHRClassifier;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.AnyMatchPredictor;
import seers.bugreppatterns.pattern.predictor.LabelPredictor;
import seers.bugreppatterns.processor.SentenceParagraphFeaturesProcessor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainPreFeatures {

    static String inputDir = "C:\\Users\\ojcch\\Documents\\Repositories\\Git\\Android-Bug-Report-Reproduction" +
            "\\EulerEvaluation\\Data\\1_parsed_bug_reports_format2_prep";
    static String outputFolder = "C:\\Users\\ojcch\\Documents\\Repositories\\Git\\Android-Bug-Report-Reproduction" +
            "\\EulerEvaluation\\Data\\2_nimbus_pattern_features\\prefeatures";
    private static File patternsFile = new File("C:\\Users\\ojcch\\Documents\\Repositories\\Git" +
            "\\bug_report_completion\\code" +
            "\\bug_report_patterns\\patterns.csv");
    private static List<PatternMatcher> patterns;

    public static void main(String[] args) throws Exception {

        patterns = MainHRClassifier.loadPatterns(patternsFile);

        try (CsvWriter predictionWriter = createWriter("output-prediction");
             CsvWriter prefeaturesWriter = createWriter("output-pre-features");
             CsvWriter patternsPreFeaturesWriter = createWriter("output-patterns");
             CsvWriter featuresDefinitionsWriter = createWriter("feature-definitions", ' ');) {


            ThreadParameters params = new ThreadParameters();
            params.addParam(HeuristicsClassifier.PREDICTION_WRITER, predictionWriter);
            params.addParam(HeuristicsClassifier.PATTERNS, patterns);

            params.addParam(HeuristicsClassifier.PRE_FEATURES_WRITER, prefeaturesWriter);
            params.addParam(HeuristicsClassifier.PATTERN_PRE_FEATURES_WRITER, patternsPreFeaturesWriter);

            LabelPredictor predictor = new AnyMatchPredictor(patterns, null,
                    HeuristicsClassifier.CooccurringFeaturesOption.ONLY_INDIV,
                    false);
            params.addParam(HeuristicsClassifier.PREDICTOR, predictor);

            // ------------------------------------------------

            writeIndividualFeatures(featuresDefinitionsWriter);

            // ------------------------------------------------

            // titles
            String[] title = new String[]{"system", "bug_id", "instance_id", "is_ob", "is_eb", "is_sr"};
            predictionWriter.writeNext(Arrays.asList(title));


            final File[] files = new File(inputDir).listFiles(f -> f.getName().endsWith(".xml"));


            for (File file : files) {


                String baseFileName = FilenameUtils.getBaseName(file.getName());
                int i = baseFileName.lastIndexOf("_");
                String bugId = baseFileName.substring(i + 1);
                String appNameVersion = baseFileName.substring(0, i);

                int j = appNameVersion.lastIndexOf("#");

                String appName = appNameVersion.substring(0, j);

                //---------------------------
                ThreadParameters newParams = new ThreadParameters(params);
                newParams.addParam(HeuristicsClassifier.SYSTEM, appName);
                newParams.addParam(ThreadExecutor.ELEMENTS_PARAM, Collections.singletonList(file));
                SentenceParagraphFeaturesProcessor processor = new SentenceParagraphFeaturesProcessor(newParams, true);
                processor.executeJob();
            }

        }

    }

    private static void writeIndividualFeatures(CsvWriter featuresDefinitionsWriter) {
        for (PatternMatcher patternMatcher : patterns) {
            List<String> nextLine = Arrays
                    .asList(patternMatcher.getName(), patternMatcher.getCode().toString());
            featuresDefinitionsWriter.writeNext(nextLine);
        }
    }

    private static CsvWriter createWriter(String prefixFileName) throws IOException {
        return createWriter(prefixFileName, ';');
    }

    private static CsvWriter createWriter(String prefixFileName, char separator) throws IOException {
        return new CsvWriterBuilder(
                new FileWriter(outputFolder + File.separator +prefixFileName + "-SP-F.csv"))
                .separator(separator).quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
    }

}
