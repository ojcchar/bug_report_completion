package seers.bugreppatterns.main;

import java.io.File;

import seers.appcore.xml.XMLHelper;
import seers.bugreppatterns.entity.paragraph.BugReport;

public class MainReader {

	public static void main(String[] args) throws Exception {
		File f = new File("test_data/reader/firefox-576450-paragraphs.xml");

		BugReport b = XMLHelper.readXML(BugReport.class, f);

		System.out.println(b);

		System.out.println("--------------------------");

	}

}
