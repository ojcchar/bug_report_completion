package seers.bugreppatterns.pattern.eb;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class WouldBeSentencePMTest {

	WouldBeSentencePM pm = new WouldBeSentencePM();

	@Test
	public void testMatchSentence() throws Exception {
		String[] txts = {
				"It would be great if all the links on the page could be disabled (to prevent multiple queried bogging the DB) and a message saying 'please wait' could be shown. ",
				"So it would be really super if the layout and images also proportionally enlarged (since otherwise I have to squint to read any text the might be on the image).",
				"It would be nice if I would have to enter all my installed JREs only once.",
				"However, it would be great if upon a double click there, the Project properties-Java Build Path-Libraries would open the concerned library where I could attach the source.",
				"This would be especially useful if the .class file is present in multiple .jar files because it then would bring to the one implementation that gets really used.",
				"So it would be really super if the layout and images also proportionally enlarged (since otherwise I have to squint to read any text the might be on the image). ",
				"Print setup allows changing what gets printed in the header or the footer, but it would be useful to have a checkbox in the print dialog indicating whether or not the header/footer should be printed at all.",
				"It would also be nice if when one included the \"--enable-static\" option one really ended up with a *STATIC* firefox (which would load faster than those that may require searching through many directories many times for shared object libraries).",
				"On Gentoo Linux, it would be nice if firefox, mozilla, nvu (or kompozer), seamonkey, etc. would all \"share\" the shared libraries -- but that isn't the way the package management and install processes operate.",
				"It might be nice if it opened a new tab in the position I clicked.",
				"This would be more convenient than dragging open a new tab group there and clicking its (pretty small) new tab button, and unlike the large global new tab button it would allow me to position the new tab right away.",
				"It would be nice if each about: page has a favicon :",
				"It would be ideal to either remove the security challenge all together or develop a better way to render it so it won t break the pages." };

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
