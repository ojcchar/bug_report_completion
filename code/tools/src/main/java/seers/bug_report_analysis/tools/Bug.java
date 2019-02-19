package seers.bug_report_analysis.tools;

import edu.utdallas.seers.entity.BugReport;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "bug")
@XmlAccessorType(XmlAccessType.FIELD)
public class Bug {
    @XmlAttribute
    private String id;
    @XmlAttribute(name = "opendate")
    private String openDate;
    @XmlAttribute(name = "fixdate")
    private String fixDate;
    @XmlAttribute(name = "resolution")
    private String resolution;
    @XmlElement(name = "buginformation")
    private BugInformation bugInformation;
    @XmlElement(name = "fixedFiles")
    private FixedFiles fixedFiles;
    @XmlElement(name = "links")
    private Links links;

    public static final DateTimeFormatter DATE_FORMATTER
            = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public Bug() {
    }

    public Bug(BugReport bugReport) {

        this.id = bugReport.getKey();
        this.openDate = bugReport.getCreationDate().toString(DATE_FORMATTER);
        this.fixDate = bugReport.getResolutionDate().toString(DATE_FORMATTER);
        this.bugInformation = new BugInformation(bugReport.getTitle(), bugReport.getDescription());
        this.fixedFiles = new FixedFiles(bugReport.getFixedFiles());

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getFixDate() {
        return fixDate;
    }

    public void setFixDate(String fixDate) {
        this.fixDate = fixDate;
    }

    public BugInformation getBugInformation() {
        return bugInformation;
    }

    public void setBugInformation(BugInformation bugInformation) {
        this.bugInformation = bugInformation;
    }

    public FixedFiles getFixedFiles() {
        return fixedFiles;
    }

    public void setFixedFiles(FixedFiles fixedFiles) {
        this.fixedFiles = fixedFiles;
    }

    public String getSummary() {
        return getBugInformation().summary;
    }

    public String getDescription() {
        return getBugInformation().description;
    }

    public List<String> getFixedFileList() {
        return fixedFiles.getList();
    }

    public void updateText(String summary, String description) {
        getBugInformation().summary = summary;
        getBugInformation().description = description;
    }

    @XmlRootElement(name = "buginformation")
    @XmlAccessorType(XmlAccessType.FIELD)
    static class BugInformation {
        @XmlElement
        private String summary;

        @XmlElement
        private String description;
        @XmlElement
        private String version;
        @XmlElement
        private String fixedVersion;
        @XmlElement
        private String type;

        public BugInformation() {
        }

        BugInformation(String summary, String description) {
            this.summary = summary;
            this.description = description.replace('\n', ' ');
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }


    @XmlRootElement(name = "fixedFiles")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class FixedFiles {

        @XmlElement(name = "file")
        private List<FixedFile> fixedFiles;

        public FixedFiles() {
        }

        public FixedFiles(List<String> fixedFiles) {
            this.fixedFiles = fixedFiles.stream().map(FixedFile::new).collect(Collectors.toList());
        }

        public List<String> getList() {
            return fixedFiles.stream().map(f -> f.value).collect(Collectors.toList());
        }
    }

    @XmlRootElement(name = "file")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class FixedFile {

        @XmlAttribute
        private String type;
        @XmlValue
        private String value;

        public FixedFile() {

        }

        public FixedFile(String value) {
            this.value = value;
        }

    }



    @XmlRootElement(name = "links")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class Links {

        @XmlElement(name = "link")
        private List<Link> link;

    }

    @XmlRootElement(name = "link")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class Link {

        @XmlAttribute
        private String type;
        @XmlAttribute(name = "description")
        private String description;
        @XmlValue
        private String value;


    }


}
