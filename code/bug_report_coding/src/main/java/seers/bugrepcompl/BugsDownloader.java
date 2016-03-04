package seers.bugrepcompl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import seers.appcore.http.HttpHelper;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.appcore.utils.ExceptionUtils;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.BugLongDescription;
import seers.bugrepcompl.entity.BugReport;
import seers.bugrepcompl.entity.BugzillaBugReport;
import seers.bugrepcompl.entity.BugzillaWrapper;

public class BugsDownloader extends ThreadProcessor {

	private String outFolder;
	@SuppressWarnings("rawtypes")
	private List<List> lines;
	private List<String> systems;

	public BugsDownloader(ThreadParameters params) {
		super(params);
		outFolder = params.getStringParam(MainBugsDownload.OUT_FOLDER);
		lines = params.getListParam(List.class, ThreadExecutor.ELEMENTS_PARAM);
		systems = params.getListParam(String.class, MainBugsDownload.SYSTEMS);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void executeJob() throws Exception {
		for (List<String> line : lines) {
			String project = line.get(0);

			if (!systems.contains(project)) {
				continue;
			}

			File folder = new File(FilenameUtils.separatorsToSystem(outFolder + File.separator + project));
			if (!folder.exists()) {
				folder.mkdirs();
			}

			String baseUrl = line.get(1);
			String issueId = line.get(2);

			try {
				downloadIssue(folder, baseUrl, issueId);
			} catch (Exception e) {
				Exception e2 = new Exception("Error for issue [" + project + "-" + issueId + "]: " + e.getMessage());
				ExceptionUtils.addStackTrace(e, e2);
				throw e2;
			}

		}
	}

	private void downloadIssue(File folder, String baseUrl, String issueId)
			throws InterruptedException, IOException, JAXBException, ParserConfigurationException, SAXException {
		String inputUrl = baseUrl.substring(0, baseUrl.length() - 4) + "?ctype=xml&id=" + issueId;
		String stringResponse = HttpHelper.sendGetRequest(inputUrl);

		// ----------------------

		BugzillaWrapper bugWr = XMLHelper.readXMLFromString(BugzillaWrapper.class, stringResponse);
		BugReport obj = getBugReport(bugWr);

		// -----------------------

		File outputFile = new File(FilenameUtils.separatorsToSystem(folder + File.separator + issueId + ".xml"));
		XMLHelper.writeXML(BugReport.class, obj, outputFile);

	}

	public static BugReport getBugReport(BugzillaWrapper bugWr) {
		BugzillaBugReport bug = bugWr.getBug();
		List<BugLongDescription> descriptions = bug.getDescriptions();

		// -------------------------
		String desc = null;
		if (descriptions != null && !descriptions.isEmpty()) {
			BugLongDescription bugDesc = descriptions.get(0);
			if (bug.getReporter().equalsIgnoreCase(bugDesc.getWho())) {
				desc = bugDesc.getText();
			} else {
				throw new RuntimeException("Reporter and person of the first comment are not equal: "
						+ bug.getReporter() + " vs. " + bugDesc.getWho());
			}
		}
		BugReport obj = new BugReport(bug.getId(), bug.getTitle(), desc);
		return obj;
	}

}
