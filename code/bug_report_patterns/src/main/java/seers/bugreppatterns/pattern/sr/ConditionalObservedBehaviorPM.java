package seers.bugreppatterns.pattern.sr;

import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.ob.ButNegativePM;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;

import java.util.List;

/**
 * Matcher for S_SR_COND_OBS.
 */
public class ConditionalObservedBehaviorPM extends StepsToReproducePatternMatcher {
    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        List<Sentence> clauses = SentenceUtils.extractClauses(sentence);

        int conditionalClausePosition = findConditionalClausePosition(clauses);

        if(conditionalClausePosition!=-1){
            // Find negative sentences starting at the end
            for (int i = clauses.size() - 1; i > conditionalClausePosition; i--) {
                if(isNegative(clauses.get(i))){
                    return 1;
                }
            }
        }

        return 0;

//        if (clauses.size() <= 1) {
//            return 0;
//        }
//
//        // Make sure the first sentence is conditional
//        List<Integer> conditionalsInFirst =
//                SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS, clauses.get(0).getTokens());
//
//        if (!conditionalsInFirst.isEmpty()) {
//            SimpleTenseChecker tenseChecker =
//                    new SimpleTenseChecker(JavaUtils.getSet("VBD", "VBP"),
//                            JavaUtils.getSet("input"));
//            for (int i = 1; i < clauses.size() - 1; i++) {
//                Sentence clause = clauses.get(i);
//                if (tenseChecker.countNumClauses(clause) <= 0) {
//                    return 0;
//                }
//            }
//
//            if (isNegative(clauses.get(clauses.size() - 1))) {
//                return 1;
//            }
//        }
//
//        return 0;
    }

    private int findConditionalClausePosition(List<Sentence> clauses) {
        for (int i = 0; i < clauses.size(); i++) {
            Sentence clause = clauses.get(i);
            List<Integer> conditionals =
                    SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS, clause.getTokens());

            if (!conditionals.isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    private boolean isNegative(Sentence sentence) throws Exception {
        for (PatternMatcher pm : ButNegativePM.NEGATIVE_PMS) {
            if (pm.matchSentence(sentence) == 1) {
                return true;
            }
        }

        return false;
    }
}
