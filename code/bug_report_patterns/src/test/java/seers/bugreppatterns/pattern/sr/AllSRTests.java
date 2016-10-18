package seers.bugreppatterns.pattern.sr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ActionsInfinitivePMTest.class, ActionsMultiPMTest.class, ActionsPastPMTest.class,
		ActionsPresentPMTest.class, ActionsSeparatorPMTest.class, AfterPMTest.class, ByActionPMTest.class,
		CodeRefPMTest.class, ConditionalThenSequencePMTest.class, CondSequenceParagrahPMTest.class,
		ContinuousPresentPMTest.class, ContinuousPresentSentencePMTest.class, HaveSequencePMTest.class,
		ImperativeSequencePMTest.class, ImperativeSubordinatesPMTest.class, LabeledListPMTest.class,
		LabeledParagraphPMTest.class, MenuNavigationPMTest.class, MenuSelectPMTest.class, PurposeActionPMTest.class,
		SimplePastParagraphPMTest.class, SimplePastPMTest.class, SimplePresentSubordinatesPMTest.class,
		ToReproParagraphPMTest.class, TriggerSentencePMTest.class, TrySentencePMTest.class,
		WhenAfterSentencePMTest.class, ActionsPresentPerfectPMTest.class, ConditionalCodeParagraphPMTest.class,
		LabeledCodeFragmentsPMTest.class, ConditionalObservedBehaviorPMTest.class, ConditionalSequencePMTest.class })
public class AllSRTests {

}
