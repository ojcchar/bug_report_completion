package seers.bugrepcompl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;

public class FileCopier {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String datalAllDir = args[0];
		String fileData = args[1];
		String outputDir = args[2];

		List<List<String>> lines = readLines(new File(fileData));
		File outDir = new File(outputDir);

		for (List<String> line : lines.subList(1, lines.size())) {
			String project = line.get(0);
			String issueId = line.get(1);

			File destDir = new File(outDir + File.separator + project);

			File srcFile = new File(datalAllDir + File.separator + project + File.separator + issueId + ".xml");
			FileUtils.copyFileToDirectory(srcFile, destDir);
		}

	}

	private static List<List<String>> readLines(File sampleFile) throws FileNotFoundException, IOException {
		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new FileReader(sampleFile), csvParser)) {
			return csvReader.readAll();
		}
	}

}
