package seers.bugrepcompl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;

/**
 * Hello world!
 *
 */
public class MainBugsDownload {

	public static final String OUT_FOLDER = "out_folder";
	public static final String SYSTEMS = "systems";

	public static void main(String[] args) throws Exception {

		String fileIn = args[0];
		String outFolder = args[1];
		String[] systems = args[2].split(",");

		File outFold = new File(outFolder);
		outFold.mkdirs();

		List<List<String>> lines = readLines(fileIn);

		ThreadParameters params = new ThreadParameters();
		params.addParam(OUT_FOLDER, outFolder);
		params.addParam(SYSTEMS, Arrays.asList(systems));

		List<List<String>> linesData = lines.subList(1, lines.size());
		System.out.println(linesData.size());
		ThreadExecutor.executePaginated(linesData, BugsDownloader.class, params, 1);

	}

	private static List<List<String>> readLines(String fileIn) throws FileNotFoundException, IOException {
		CsvParser csvParser = new CsvParserBuilder().separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(fileIn), "Cp1252"), csvParser)) {
			return csvReader.readAll();
		}
	}
}
