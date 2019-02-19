package seers.bug_report_analysis.tools.euler.nimbus;

import seers.appcore.csv.CSVHelper;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class MainClassificationGeneration {

    public static void main(String[] args) throws Exception {
        String classificationOutputFile = "C:\\Users\\ojcch\\Documents\\Repositories\\Git\\Android-Bug-Report" +
                "-Reproduction\\EulerEvaluation\\Data\\3_identified_s2r\\out.csv";
        String parsedBugReportPath = "C:\\Users\\ojcch\\Documents\\Repositories\\Git\\Android-Bug-Report-Reproduction" +
                "\\EulerEvaluation\\Data\\1_parsed_bug_reports_format2";
        String outFolder = "C:\\Users\\ojcch\\Documents\\Repositories\\Git\\Android-Bug-Report-Reproduction" +
                "\\EulerEvaluation\\Data\\3_identified_s2r_in_bug_reports";

        final List<File> reportFiles =
                Arrays.asList(Objects.requireNonNull(new File(parsedBugReportPath).listFiles(f -> f.isFile() && f.getName().endsWith(".xml"))));

        final List<List<String>> lines = CSVHelper.readCsv(classificationOutputFile, false);

        final Map<String, List<List<String>>> linesPerBug = lines.stream()
                .collect(Collectors.groupingBy(l -> l.get(0) + ";" + l.get(1)));

        for (Map.Entry<String, List<List<String>>> bugLines : linesPerBug.entrySet()) {
            final String[] split = bugLines.getKey().split(";");
            String system = split[0];
            String bugId = split[1];

            final File bugFile =
                    reportFiles.stream().filter(f -> f.getName().matches(system + "#.+_" + bugId + "\\.xml")).findFirst().get();

            final ShortLabeledBugReport bugReport = XMLHelper.readXML(ShortLabeledBugReport.class, bugFile);

            final List<List<String>> classifiedSentences = bugLines.getValue();
            tagSentences(bugReport, classifiedSentences);

            File outFile = Paths.get(outFolder, bugFile.getName()).toFile();
            XMLHelper.writeXML(bugReport, outFile);
        }

    }

    private static void tagSentences(ShortLabeledBugReport bugReport, List<List<String>> classifiedSentences) {

        List<ShortLabeledDescriptionSentence> allSentences = new ArrayList<>();
        if (bugReport.getDescription() != null) {
            allSentences = bugReport.getDescription().getAllSentences();
        }

        for (List<String> classifiedSentence : classifiedSentences) {
            final String sentenceId = classifiedSentence.get(2);
            final String clazz = classifiedSentence.get(3);

            String ob = clazz.contains("ob") ? "x" : "";
            String eb = clazz.contains("eb") ? "x" : "";
            String s2r = clazz.contains("sr") ? "x" : "";

            if ("0".equals(sentenceId)) {
                bugReport.getTitle().setOb(ob);
                bugReport.getTitle().setEb(eb);
                bugReport.getTitle().setSr(s2r);
            } else {

                final ShortLabeledDescriptionSentence sentence =
                        allSentences.stream().filter(s -> s.getId().equals(sentenceId)).findFirst().get();

                sentence.setOb(ob);
                sentence.setEb(eb);
                sentence.setSr(s2r);
            }
        }
    }
}
