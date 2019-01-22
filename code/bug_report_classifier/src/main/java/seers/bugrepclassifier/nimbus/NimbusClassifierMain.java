package seers.bugrepclassifier.nimbus;

import seers.bugrepcompl.entity.codingparse.LabeledBugReport;
import seers.bugrepcompl.entity.regularparse.ParsedBugReport;
import seers.bugrepcompl.entity.regularparse.ParsedDescriptionSentence;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class NimbusClassifierMain {


    public LabeledBugReport identifyS2RSentences(ParsedBugReport bugReport) throws Exception {

        //preprocess the bug report (each sentence)
        ParsedBugReport preprocessedBugReport = preprocessBugReport(bugReport);

        //compute the pattern features for the bug report

        //proprogate the labels from the paragraphs to the sentences
        HashMap<String, List<Integer>> patternFeatureMap = getPatternFeatureMap(preprocessedBugReport);

        //classify
        HashMap<String, Boolean> classification = classify(preprocessedBugReport, patternFeatureMap);

        //assign the labels to the sentences
        LabeledBugReport labeledBugReport = null;
        for (ParsedDescriptionSentence sentence : bugReport.getDescription().getAllSentences()) {
        }

        return labeledBugReport;
    }

    private LinkedHashMap<String, List<Integer>> getPatternFeatureMap(ParsedBugReport preprocessedBugReport) {
        return new LinkedHashMap<>();
    }

    private HashMap<String, Boolean> classify(ParsedBugReport preprocessedBugReport,
                                              HashMap<String, List<Integer>> patternFeatureMap) {

        //call to a python script

        {
            //load the model

            //for each paragraph
            {

                //input preparation
                {
                    //get the sentences

                    //get the features for each sentence
                }

                //run the model

                //read the model output (a file)

            }
        }

        return null;
    }

    private ParsedBugReport preprocessBugReport(ParsedBugReport bugReport) {
        return bugReport;
    }
}
