package seers.bugrepclassifier.nimbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.codingparse.LabeledBugReport;
import seers.bugrepcompl.entity.regularparse.ParsedBugReport;
import seers.bugrepcompl.entity.regularparse.ParsedDescriptionSentence;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class NimbusClassifierMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(NimbusClassifierMain.class);

    public LabeledBugReport identifyS2RSentences(String bugReportPath) throws Exception {
        final ParsedBugReport bugReport = XMLHelper.readXML(ParsedBugReport.class, bugReportPath);

        LOGGER.debug("# of sentences: " + bugReport.getDescription().getAllSentences().size());

        return identifyS2RSentences(bugReport);
    }

    public LabeledBugReport identifyS2RSentences(ParsedBugReport bugReport) throws Exception {

        //preprocess the bug report (each sentence)
        ParsedBugReport preprocessedBugReport = preprocessBugReport(bugReport);

        //compute the pattern features for the bug report

        //propagate the labels from the paragraphs to the sentences
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
