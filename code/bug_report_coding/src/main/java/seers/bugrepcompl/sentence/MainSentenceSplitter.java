package seers.bugrepcompl.sentence;

import java.util.List;

import seers.textanalyzer.TextPreprocessor;

public class MainSentenceSplitter {

	public static void main(String[] args) {

		TextPreprocessor pr = new TextPreprocessor();
		String text = "User-Agent:       Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.5a) Gecko/20030728 Mozilla Firebird/0.6.1\nBuild Identifier: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.5a) Gecko/20030728 Mozilla Firebird/0.6.1\n\nIf you enter part of a URL in the location bar, then switch to a different tab,\nthen switch back to the original tab, the text you typed in the location bar is\ngone.\n\nThis is annoying, especially when trying to paste together a URL from two lines\nof text, e.g.\n\n    http://bugzilla.mozilla.org/enter_bu\n    g.cgi?product=Firebird\n\nI would like to paste that URL (split across lines) into the location bar\nbelonging to a different tab, but I can't find any way to do this.  Every time I\nswitch to a different tab and back again, what I had previously typed in the bar\nis lost.\n\nReproducible: Always\n\nSteps to Reproduce:\n1. Start Firebird.\n2. C-t to open a new tab.  The second tab is now displayed.\n3. Type 'hello'.  This text appears in the location bar.\n4. Click on the header for the first tab to switch to that tab.\n5. Click on the header for the second tab to switch back to the second tab.\n\nActual Results:  \nWhen I switched back to the second tab, the text 'hello' I had typed was gone.\n\nExpected Results:  \nThe partially typed URL should have been preserved, ready for the user to type\nor paste some more.";
		List<List<String>> sentences = TextPreprocessor.tokenizeInSentences(text);

		for (List<String> s : sentences) {
			System.out.println(s);
		}

	}

}
