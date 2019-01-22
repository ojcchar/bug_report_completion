package seers.bugrepclassifier.nimbus;

import seers.bugrepcompl.entity.codingparse.LabeledBugReport;
import seers.bugrepcompl.entity.regularparse.ParsedBugReport;

import java.util.HashMap;
import java.util.List;

public class NimbusClassifierMain {


    public LabeledBugReport identifyS2RSentences(ParsedBugReport bugReport) throws Exception {

        //preprocess the bug report (each sentence)
        ParsedBugReport preprocessedBugReport = preprocessBugReport(bugReport);

        //compute the pattern features for the bug report

        //proprogate the labels from the paragraphs to the sentences
        HashMap<String, List<Integer>> patternFeatureMap = null;

        //
        HashMap<String, Boolean> classification = classify(preprocessedBugReport, patternFeatureMap);

        return null;
    }

    private HashMap<String, Boolean> classify(ParsedBugReport preprocessedBugReport,
                                              HashMap<String, List<Integer>> patternFeatureMap) {

        //call to a python script

        return null;
    }

    private ParsedBugReport preprocessBugReport(ParsedBugReport bugReport) {
        return null;
    }
}
