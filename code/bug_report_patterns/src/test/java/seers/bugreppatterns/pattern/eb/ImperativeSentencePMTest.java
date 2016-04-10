package seers.bugreppatterns.pattern.eb;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class ImperativeSentencePMTest {

	ImperativeSentencePM pm = new ImperativeSentencePM();

	@Test
	public void testMatchSentence() throws Exception {
		String[] txts = { "Tell users that something is happening when queries take a long time",
				"Make Targets not automatically filled in for new Autotools project",
				"Allow Stream.Publish to include app_id for URL redirection",
				"Try to resolve the word, and if not then an I'm feeling lucky search of the word.",
				"Create a non-version specific entry (e.g. \"Mozilla Firefox\" without version)\r\nso the entry gets overwritten automatically",
				"Automatically uninstall the old Mozilla Firefox version before installing the\r\nnew one",
				"Please add application/x-stuffitx mime type for .sitx StuffitX files",
				"Expected Results:  \r\nDisplay the text properly.",
				"Expected Results:  \r\nopen the mail client specified in mozex", "Remove the old entry",
				"[DB] Define primary key constraint for cdo_id in cdo_objects table",
				"ab: optionally reduce resource usage (qsort and memory)",
				"[api] provide support for constructing task list subtask hierarchy from inward task relations" };

		for (int i = 0; i < txts.length; i++) {
			String txt = txts[i];

			System.out.print("Testing (positive) " + i);
			List<Sentence> sentences = TextProcessor.processText(txt);
			int m = pm.matchSentence(sentences.get(0));
			assertEquals(1, m);

			System.out.println(" PASSED");

		}
	}

}
