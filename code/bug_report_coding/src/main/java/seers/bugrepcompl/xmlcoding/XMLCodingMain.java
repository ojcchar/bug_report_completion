package seers.bugrepcompl.xmlcoding;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.codingparse.BugReport;

public class XMLCodingMain {

//	static HashSet<String> allowedCoders = new HashSet<String>(
//			Arrays.asList(new String[] { "andi", "juan", "laura", "fiorella", "jing", "oscar" }));
	static HashSet<String> allowedCoders = new HashSet<String>(
	Arrays.asList(new String[] { "oscar", "lau", "ana", "daniel", "alejo" }));
//	static HashSet<String> allowedCoders = new HashSet<String>(
//			Arrays.asList(new String[] { "alex" }));
	
	static boolean copyOriginal = false;

	public static void main(String[] args) throws Exception {

//		String inFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/sample/andi_students/complete_sample_seers50.csv";
//		String xmlFilesDir = "C:/Users/ojcch/Documents/Dropbox/Research/BUG_REPORT_PROJECT_JING/Data/final_coding";
//		String outFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/coded_data/alex_xml";

		String inFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/sample/andi_students/old_data_coding.csv";
		String xmlFilesDir = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/old_data";
		String outFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/sample_old_data";

		HashMap<String, List<TextInstance>> sample1 = readSample(inFile, false);
		HashMap<String, List<TextInstance>> sample2 = readSample(inFile, true);

		processSample(outFolder, sample1, "originals", xmlFilesDir);
		processSample(outFolder, sample2, "replacements", xmlFilesDir);

	}

	private static void processSample(String outFolder, HashMap<String, List<TextInstance>> sample1, String sampleName,
			String xmlFilesDir) throws Exception {

		// ------------------------------------------

		File sampleDir = new File(outFolder + File.separator + sampleName);
		sampleDir.mkdir();

		// ------------------------------------------

		Set<String> coders = sample1.keySet();

		for (String coder : coders) {

			System.out.println("Processing " + coder);

			try {
				File coderDir = new File(sampleDir.getAbsolutePath() + File.separator + coder);
				coderDir.mkdir();

				File bugsDir = new File(coderDir.getAbsolutePath() + File.separator + "bugs");
				bugsDir.mkdir();

				File parsedBugsDir = new File(coderDir.getAbsolutePath() + File.separator + "bugs_parsed");
				parsedBugsDir.mkdir();

				List<TextInstance> bugList = sample1.get(coder);

				for (TextInstance bug : bugList) {
					copyOriginal(bug, bugsDir, xmlFilesDir);
					createParsed(bug, parsedBugsDir, xmlFilesDir);
				}
			} catch (Exception e) {
				System.err.println("Error with " + coder);
				throw e;
			}

		}

	}

	static HashMap<TextInstance, seers.bugrepcompl.entity.shortcodingparse.BugReport> bugCache = new HashMap<>();

	private static void createParsed(TextInstance bug, File parsedBugsDir, String xmlFilesDir) throws Exception {

		try {
			seers.bugrepcompl.entity.shortcodingparse.BugReport bugReport2 = bugCache.get(bug);
			if (bugReport2 == null) {

				String bugFile = xmlFilesDir + File.separator + "bugs_parsed" + File.separator + bug.getProject()
						+ "_parse" + File.separator + bug.getBugId() + ".xml.parse";
				BugReport bugReport = XMLHelper.readXML(BugReport.class, bugFile);

				bugReport2 = bugReport.toBugReport2();

				bugCache.put(bug, bugReport2);
			}

			File projFolder = new File(parsedBugsDir + File.separator + bug.getProject());
			projFolder.mkdir();

			File outputFile = new File(projFolder + File.separator + bug.getBugId() + ".parse.xml");
			XMLHelper.writeXML(seers.bugrepcompl.entity.shortcodingparse.BugReport.class, bugReport2, outputFile);
		} catch (Exception e) {
			System.err.println("Error for bug " + bug);
			throw e;
		}

	}

	private static void copyOriginal(TextInstance bug, File bugsDir, String xmlFilesDir) throws IOException {
		
		if (!copyOriginal) {
			return;
		}

		File srcFile = new File(xmlFilesDir + File.separator + "bugs" + File.separator + bug.getProject()
				+ File.separator + bug.getBugId() + ".xml");
		File destFile = new File(
				bugsDir + File.separator + bug.getProject() + File.separator + bug.getBugId() + ".xml");

		FileUtils.copyFile(srcFile, destFile);
	}

	public static HashMap<String, List<TextInstance>> readSample(String path, boolean replacementEntries)
			throws IOException {
		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(path), "Cp1252"),
				csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			HashMap<String, List<TextInstance>> entries = new HashMap<>();

			allLines.subList(1, allLines.size()).forEach(line -> {

				String replacement = line.get(4);

				String project = line.get(0);
				String bugId = line.get(1);
				String coder1 = line.get(2);
				String coder2 = line.get(3);

				TextInstance instance = new TextInstance(project, bugId, "0");

				if (replacementEntries) {
					if (!replacement.trim().isEmpty()) {
						addInstance(entries, coder1, instance);
						addInstance(entries, coder2, instance);
					}
				} else if (replacement.trim().isEmpty()) {

					addInstance(entries, coder1, instance);
					addInstance(entries, coder2, instance);
				}
			});

			return entries;
		}
	}

	private static void addInstance(HashMap<String, List<TextInstance>> entries, String coder, TextInstance instance) {

		if (allowedCoders.contains(coder)) {

			List<TextInstance> instances = entries.get(coder);
			if (instances == null) {
				instances = new ArrayList<>();
				entries.put(coder, instances);
			}
			instances.add(instance);
		}

	}

}
