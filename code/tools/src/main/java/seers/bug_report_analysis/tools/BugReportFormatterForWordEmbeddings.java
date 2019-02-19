package seers.bug_report_analysis.tools;

import com.google.gson.Gson;
import edu.utdallas.seers.entity.BugReport;
import edu.wayne.cs.severe.ir4se.processor.utils.GsonUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.appcore.xml.XMLHelper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BugReportFormatterForWordEmbeddings {

    static Logger LOGGER = LoggerFactory.getLogger(BugReportFormatterForWordEmbeddings.class);

    enum DS {
        ZHANG2017, DYSDOC_POI, MSR11_CHROME, B4BL
    }

    //    static String inputFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\others\\IEEE-Software\\all_bugs";
//    static String inputFolder = "C:\\Users\\ojcch\\Documents\\Projects\\DysDoc2\\data_collection\\bugzilla-dump";
//    static String inputFolder = "C:\\Users\\ojcch\\Documents\\Temp\\bugs-html";
    static String inputFolder = "C:\\Bench4BL\\data_all";
    static String outputFolder = "C:\\Users\\ojcch\\Documents\\Projects\\Nimbus\\tmp";
    static Gson gson = GsonUtils.createDefaultGson();
    static DS dataSet = DS.B4BL;

    public static void main(String[] args) throws Exception {

        List<File> dirs = Arrays.asList(new File(inputFolder).listFiles(File::isDirectory));
        if (DS.B4BL.equals(dataSet)) {
            dirs = dirs.stream().flatMap(d -> Arrays.stream(d.listFiles(File::isDirectory))).collect(Collectors.toList());
        }

        ThreadExecutor.executePaginated(dirs, FolderProcessor.class, new ThreadParameters(), 8);

    }

    public static class FolderProcessor extends ThreadProcessor {

        private final List<File> dirs;

        public FolderProcessor(ThreadParameters params) {
            super(params);
            dirs = params.getListParam(File.class, ThreadExecutor.ELEMENTS_PARAM);
        }

        @Override
        public void executeJob() throws Exception {

            for (File dir : dirs) {

                final String dirName = dir.getName();
                final String sysName = dirName.substring(dirName.indexOf("_") + 1).toLowerCase();

                final List<ThreadProcessor> processors;
                if (DS.B4BL.equals(dataSet)) {
                    final File bugRepoFile = Paths.get(dir.toString(), "bugrepo", "repository.xml").toFile();
                    final BugRepository bugRepository = XMLHelper.readXML(BugRepository.class, bugRepoFile);
                    final List<Bug> bugs = bugRepository.getBugs();
                    final ThreadParameters params = new ThreadParameters();
                    params.addParam("sys", bugRepository.getName());
                    processors = ThreadExecutor.executePaginated(bugs,
                            BugProcessor2.class, params, 8);
                } else {
                    final List<File> files = Arrays.asList(dir.listFiles(File::isFile));
                    processors = ThreadExecutor.executePaginated(files,
                            BugProcessor.class, new ThreadParameters(), 8);
                }

                final List<BugReport> brs =
                        processors.stream().flatMap(p -> {
                            if (p instanceof BugProcessor)
                                return ((BugProcessor) p).getBrs().stream();
                            else
                                return ((BugProcessor2) p).getBrs().stream();
                        }).collect(Collectors.toList());

                if (brs.isEmpty())
                    continue;
                //-----------------------------------------------
                Paths.get(outputFolder, sysName).toFile().mkdirs();
                File bugsFile = Paths.get(outputFolder, sysName, "bug-reports.json").toFile();
                try (FileWriter writer = new FileWriter(bugsFile)) {
                    for (BugReport bugReport : brs) {
                        writer.write(gson.toJson(bugReport) + System.lineSeparator());
                    }
                }
            }
        }


    }

    public static class BugProcessor2 extends ThreadProcessor {
        private final List<Bug> bugs;
        private final String system;
        List<BugReport> brs = new ArrayList<>();

        public BugProcessor2(ThreadParameters params) {
            super(params);
            bugs = params.getListParam(Bug.class, ThreadExecutor.ELEMENTS_PARAM);
            system = params.getStringParam("sys");
        }

        @Override
        public void executeJob() throws Exception {

            for (Bug bug : bugs) {

//                System.out.println(bug.getId());

                try {

                    if (validateBug(system + "-" + bug.getId(), bug.getId(), bug.getDescription())) continue;

//                        System.out.println(issue);
                    brs.add(convertToBr(bug));
                } catch (Exception e) {
                    LOGGER.debug("Error for: " + bug, e);
                }
            }

        }

        private BugReport convertToBr(Bug bug) throws ParseException {
            BugReport br = new BugReport(bug.getId(), bug.getSummary(), bug.getDescription());

            String pattern = "yyyy-MM-dd HH:mm:ss";

            br.setCreationDate(parseDate(bug.getOpenDate(), pattern));
            br.setResolutionDate(parseDate(bug.getFixDate(), pattern));

            return br;
        }

        public List<BugReport> getBrs() {
            return brs;
        }
    }

    public static class BugProcessor extends ThreadProcessor {
        private final List<File> files;
        List<BugReport> brs = new ArrayList<>();

        public BugProcessor(ThreadParameters params) {
            super(params);
            files = params.getListParam(File.class, ThreadExecutor.ELEMENTS_PARAM);
        }

        @Override
        public void executeJob() throws Exception {

            for (File file : files) {

                System.out.println(file);

                try {
                    Issue1 issue = null;
                    switch (dataSet) {
                        case ZHANG2017:
                            issue = readIssue1(file);
                            break;
                        case DYSDOC_POI:
                            issue = readIssue2(file);
                            break;
                        case MSR11_CHROME:
                            issue = readIssue3(file);
                            break;
                    }

                    if (issue == null) continue;

//                        System.out.println(issue);
                    brs.add(convertToBr(issue));
                } catch (Exception e) {
                    LOGGER.debug("Error for: " + file, e);
                }
            }

        }

        public List<BugReport> getBrs() {
            return brs;
        }

        private Issue1 readIssue3(File file) throws Exception {

            final org.jsoup.nodes.Document doc1 = Jsoup.parse(file, Charset.defaultCharset().name());
            final Elements headerElement = doc1.select("#issueheader > table > tbody > tr");

            if (headerElement == null || headerElement.isEmpty()) {
                LOGGER.warn("No valid bug: " + file);
                return null;
            }

            final String bugID = headerElement.first()
                    .select("td").first().select("a").text().trim();

            final Elements descriptionElement = doc1.select("td.issuedescription");
            String description = descriptionElement.select("pre").text().trim();
            description = description.replace("<b>", "").replace("</b>", "");

            if (validateBug(file.getAbsolutePath(), bugID, description)) return null;

            final String openTime =
                    descriptionElement.select("div > span[class=date]").attr("title").trim();
            final String title = headerElement.select("td").get(1).select("span").text().trim();

            Issue1 issue = new Issue1();
            issue.title = title;
            issue.bugID = bugID;
            issue.openTime = openTime;
            issue.description = description;
            return issue;
        }

        private Issue1 readIssue2(File file) throws Exception {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            XPathFactory xPathfactory = XPathFactory.newInstance();

            final String bugID = getValue(doc, xPathfactory, "//bug/bug_id");
            final String description = getValue(doc, xPathfactory, "//bug/long_desc[1]/thetext");

            if (validateBug(file.getAbsolutePath(), bugID, description)) return null;

            final String openTime = getValue(doc, xPathfactory, "//bug/creation_ts");
            final String closedTime = getValue(doc, xPathfactory, "//bug/delta_ts");
            final String title = getValue(doc, xPathfactory, "//bug/short_desc");

            Issue1 issue = new Issue1();
            issue.title = title;
            issue.bugID = bugID;
            issue.openTime = openTime;
            issue.closedTime = closedTime;
            issue.description = description;
            return issue;
        }

        private Issue1 readIssue1(File file) throws IOException {
            final String fileContent = FileUtils.readFileToString(file, Charset.defaultCharset());

            if (StringUtils.isEmpty(fileContent))
                return null;

            String bugID = getTagValue(fileContent, "bugID");
            String description = getTagValue(fileContent, "description");

            //---------------------------------------

            if (validateBug(file.getAbsolutePath(), bugID, description)) return null;

            final String[] split = bugID.split("#");
            if (split.length == 1) {
                LOGGER.warn("Couldn't parse bug id: " + bugID + " - " + file);
                return null;
            }


            //-----------------------------------------------

            bugID = Integer.valueOf(split[split.length - 1].trim()).toString();


            String openTime = getTagValue(fileContent, "openTime");
            String closedTime = getTagValue(fileContent, "closedTime");
            String title = getTagValue(fileContent, "title");

            Issue1 issue = new Issue1();
            issue.title = title;
            issue.bugID = bugID;
            issue.openTime = openTime;
            issue.closedTime = closedTime;
            issue.description = description;
            return issue;

//            return XMLHelper.readXML(Issue1.class, file);
        }


        private BugReport convertToBr(Issue1 issue) throws ParseException {
            BugReport br = new BugReport(issue.bugID, issue.title, issue.description);

            String pattern = null;

            switch (dataSet) {
                case ZHANG2017:
                    pattern = "EEE MMM dd HH:mm:ss zzz yyyy";
                    break;
                case DYSDOC_POI:
                    pattern = "yyyy-MM-dd HH:mm:ss Z";
                    break;
                case MSR11_CHROME:
                    pattern = "EEE MMM dd HH:mm:ss yyyy";
                    break;
            }


            br.setCreationDate(parseDate(issue.openTime, pattern));
            br.setResolutionDate(parseDate(issue.closedTime, pattern));

            return br;
        }
    }

    private static String getValue(Document doc, XPathFactory xPathfactory, String expression) throws XPathExpressionException {
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = xpath.compile(expression);
        return (String) expr.evaluate(doc, XPathConstants.STRING);
    }

    static DateTime parseDate(String date, String pattern) throws ParseException {
        if (StringUtils.isEmpty(date)) return null;
        DateFormat dateFormat = new SimpleDateFormat(
                pattern);

        final Date d = dateFormat.parse(date);
        return new DateTime(d);
    }

    static String getTagValue(String content, String tag) {
        String regex = ".*<" + tag + ">(.+)</" + tag + ">.*";
//        System.out.println(regex);
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.matches()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    static boolean validateBug(String file, String bugID, String description) {
        if (StringUtils.isEmpty(bugID)) {
            LOGGER.warn("No bug id: " + file);
            return true;
        }

        if (StringUtils.isEmpty(description)) {
            LOGGER.warn("No description: " + file);
            return true;
        }

        if ("No description provided.".equalsIgnoreCase(description)) {
            //LOGGER.warn("No description provided: " + file);
            return true;
        }
        return false;
    }

}
