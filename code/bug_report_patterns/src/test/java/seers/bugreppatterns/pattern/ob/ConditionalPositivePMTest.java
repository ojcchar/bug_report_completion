package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 8/10/16.
 */
public class ConditionalPositivePMTest extends BaseTest {
	{
		pm = new ConditionalPositivePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {};

		TestUtils.testSentences(sentences, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"When I use Shift+Tab to decrease indentation level of selected C source lines, these selected C source lines are scrolled up or down (depends on its position on screen), why?",
				"If someone creates a template which takes a sequence or an orderedset (or I think any type of collection) and if inside that sequence you have some invocation of templates, the result of the invocation will be considered as an \"ocl invalid result\".",
				"If I push an image called bmorearty/xyz and then I make a brand new image (not based on the first one) and run `docker push bmorearty/xyz` again, the registry allows it.",
				"When LibreOffice calc retrieves floating-point data from a LibreOffice database (odb file) while having a locale setting with comma as the decimal separator, a floating-point number from the database may get changed by calc (and therefore lost from the spreadsheet) into a date.",
				"When you attempt to search for a concept (with the search box) at the top of the view/edit concept screen, when you enter the search term, and click \"Search\" it returns to the main concept search screen, and yields no results.",
				"For example if a person was called Sarah Lee Smith and her first name is \"Sarah Lee\" then if you searched for Sarah Lee Smith you would not find her, you would need to leave out the Lee or have made Lee a middle name when it was created", 
				"However, if I go back to my bookmarks it shows the top of the bookmarks list, rather than being scrolled down to the bottom of the list, where I was before.  "		
		};

		TestUtils.testSentences(sentences, pm, 1);
	}
}
