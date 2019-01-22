package seers.bugrepclassifier.nimbus;

import org.junit.Test;
import seers.bugrepcompl.entity.codingparse.LabeledBugReport;

import static org.junit.Assert.*;

public class NimbusClassifierMainTest {

    @Test
    public void identifyS2RSentences() throws Exception {

        NimbusClassifierMain classifier = new NimbusClassifierMain();
        final LabeledBugReport labeledBugReport =
                classifier.identifyS2RSentences("test_data/nimbus/android-mileage#3.1.1_64.xml");

        System.out.println(labeledBugReport);
    }


}