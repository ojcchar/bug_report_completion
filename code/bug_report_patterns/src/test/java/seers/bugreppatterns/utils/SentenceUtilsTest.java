package seers.bugreppatterns.utils;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class SentenceUtilsTest {

	@Test
	public void testExtractClausesByPairsClauseSeparators() {

		Set<ImmutablePair<String, String>> clauseSeparators = JavaUtils.getPairSet("->");
		// System.out.println(clauseSeparators);
		String text = "Tap on change date range->select range option";
		Sentence sentence = TextProcessor.processText(text).get(0);
		List<Sentence> subSentences = SentenceUtils.extractClausesByPairsClauseSeparators(sentence, clauseSeparators);

		// for (Sentence sentence2 : subSentences) {
		// System.out.println(sentence2);
		// }
		assertEquals(2, subSentences.size());
	}

}
