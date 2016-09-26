package seers.bugrepcompl.others;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FilenameUtils;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.BugReport;

public class MainFacebookDataReader {

	public static void main(String[] args) throws FileNotFoundException, IOException, JAXBException {
		String fileIn = args[0];
		String outFolder = args[1];

		File outFold = new File(outFolder);
		outFold.mkdirs();

		List<List<String>> lines = readLines(fileIn);

		for (List<String> line : lines.subList(1, lines.size())) {
			String issueId = line.get(0);
			// String title = line.get(7);
			// String description = line.get(22);
			String title = line.get(7).replace("\\n", "\n");
			String description = line.get(22).replace("\\n", "\n");

			File outputFile = new File(FilenameUtils.separatorsToSystem(outFold + File.separator + issueId + ".xml"));
			BugReport obj = new BugReport(issueId, title, description);
			XMLHelper.writeXML(BugReport.class, obj, outputFile);
		}

	}

	private static List<List<String>> readLines(String fileIn) throws FileNotFoundException, IOException {
		CsvParser csvParser = new CsvParserBuilder().separator(',').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(fileIn), "Cp1252"), csvParser)) {
			return csvReader.readAll();
		}
	}
}
