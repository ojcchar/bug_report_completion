package seers.bugrepclassifier.svmbased;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import seers.bugrepclassifier.BugReportClassifier;
import seers.bugrepclassifier.svmbased.model.BugReport;
import seers.bugrepclassifier.svmbased.svm.BugReportFea;
import seers.bugrepclassifier.svmbased.svm.util.Common;
import seers.bugrepcompl.entity.Labels;
import seers.bugreppatterns.pattern.predictor.PredictionOutput;

public class SVMBugReportClassifier extends BugReportClassifier {

	public SVMBugReportClassifier(File filePatterns) throws Exception {
		super(filePatterns);
	}

	@Override
	public PredictionOutput detectInformation(String bugId, String bugDescription) throws Exception {

		// generate features

		BugReport report = new BugReport(bugDescription);
		BugReportFea fea = new BugReportFea(false, "svm_model/exp/BugReportFea_exp_all");

		fea.configure(report);
		String feaStr = fea.getSVMFormatString();

		ArrayList<String> testline = new ArrayList<String>();
		testline.add("+1 " + feaStr);

		String svmFile = "output/" + bugId + ".svm";
		Common.outputLines(testline, svmFile, false);

		String outFile = "output/" + bugId + ".out";
		// classify

		try {
			String line;
			String command = "svm_light/v6.01/svm_light_windows/svm_classify.exe -f 0 " + svmFile
					+ " svm_model/exp/exp-modelall-0.005-b-pngram-light " + outFile;
			System.out.println(command);
			Process p = Runtime.getRuntime().exec(command);
//			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			while ((line = input.readLine()) != null) {
//				System.out.println(line);
//			}
//			input.close();
			p.waitFor();
		} catch (Exception err) {
			err.printStackTrace();
		}

		// read output
		List<String> lines = FileUtils.readLines(new File(outFile));
		String line = lines.get(0);

		String cls = line.split(" ")[0].split(":")[1];

		Labels labels = new Labels("", cls.equals("+1") ? "x" : "", "");
		PredictionOutput output = new PredictionOutput(labels, null);

		return output;
	}

}
