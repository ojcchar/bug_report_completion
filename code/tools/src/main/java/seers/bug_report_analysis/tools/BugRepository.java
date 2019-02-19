package seers.bug_report_analysis.tools;

import edu.utdallas.seers.entity.BugReport;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "bugrepository")
@XmlAccessorType(XmlAccessType.FIELD)
public class BugRepository {

    @XmlAttribute
    private String name;

    @XmlElement(name = "bug")
    private List<Bug> bugs;

    public BugRepository() {
    }

    public BugRepository(String name, List<BugReport> bugReports) {
        this.name = name;
        this.bugs = bugReports.stream()
                .map(Bug::new)
                .collect(Collectors.toList());
    }

    public List<Bug> getBugs() {
        return bugs;
    }


    public void writeXMLRepository(String xmlBugReportsPath) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(BugRepository.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");

        marshaller.marshal(this, new File(xmlBugReportsPath));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
