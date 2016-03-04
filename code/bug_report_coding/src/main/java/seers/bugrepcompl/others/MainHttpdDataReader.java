package seers.bugrepcompl.others;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.BugsDownloader;
import seers.bugrepcompl.entity.BugReport;
import seers.bugrepcompl.entity.BugzillaWrapper;

public class MainHttpdDataReader {

	public static void main(String[] args)
			throws FileNotFoundException, IOException, JAXBException, SAXException, ParserConfigurationException {
		String folderIn = args[0];
		String outFolder = args[1];

		File outFold = new File(outFolder);
		outFold.mkdirs();

		File foldIn = new File(folderIn);

		for (File file : foldIn.listFiles()) {
			System.out.println(file);
			String issueId = file.getName().replace(".xml", "");
			File outputFile = new File(FilenameUtils.separatorsToSystem(outFold + File.separator + issueId + ".xml"));

			BugzillaWrapper bugWr = XMLHelper.readXML(BugzillaWrapper.class, file);
			BugReport bugReport = BugsDownloader.getBugReport(bugWr);

			XMLHelper.writeXML(BugReport.class, bugReport, outputFile);
		}

	}
}
