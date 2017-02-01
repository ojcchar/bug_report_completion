package seers.bugrepcompl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.FileUtils;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;

public class SampleFileCreator {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String datalAllDir = args[0];
		String dataSamples = args[1];

		File dirSamples = new File(dataSamples);
		for (File sampleFile : dirSamples.listFiles()) {
			String fileName = sampleFile.getName().replace(".csv", "");
			if (!fileName.startsWith("sample_")) {
				continue;
			}

			String coder = fileName.split("_")[1];

			List<List<String>> lines = readLines(sampleFile);
			for (List<String> line : lines.subList(1, lines.size())) {
				String project = line.get(0);
				String issueId = line.get(1);

				File destDir = new File(dirSamples + File.separator + coder + File.separator + project);
				destDir.mkdirs();

				File srcFile = new File(datalAllDir + File.separator + project + File.separator + issueId + ".xml");
				FileUtils.copyFileToDirectory(srcFile, destDir);
			}

		}
	}

	private static List<List<String>> readLines(File sampleFile) throws FileNotFoundException, IOException {
		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(sampleFile), "Cp1252"),
				csvParser)) {
			return csvReader.readAll();
		}
	}

}
