package seers.bugreppatterns.pattern.eb;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class NeedsToPMTest {

	NeedsToPM pm = new NeedsToPM();

	@Test
	public void testMatchSentence() throws Exception {
		String[] txts = { "1 Three way compare determination needs to be improvided in RemoteSyncElement.",
				"The FB.Facebook.init function needs to handle additional parameters beyond just the relative path to XT_receiver.htm or the relative path needs to be able to handle paths such as  ../XD_receiver.htm ",
				"This message and/or the word event needs to be able to be modifiyed by the developer.",
				"So it needs to perform uri escaping only on the filename part of each URL and\r\nthen add the \"?\" and query string to produce the link.",
				"This link\r\nneeds to be html-escaped instead of being uri-escaped as it is now.",
				"sometimes favicons in url results are blank, need to use the favicon service",
				"We need to convert that to:\r\n\r\nmoz-anno:favicon:http://www.mozilla.org/2005/made-up-favicon/308-1893059051",
				// "also, apache needs uri header conversation module"
		};

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
