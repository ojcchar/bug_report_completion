package seers.bugrepclassifier.svmbased;

import java.io.File;
import java.io.IOException;
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
		String model = "svm_model/exp/exp-modelall-0.005-b-pngram-light";
		String featuresFile = "svm_model/exp/BugReportFea_exp_all";
		String suffix = "eb";
		String ebClass = classifyInstance(model, featuresFile, suffix, bugId, report);

		String srModel = "svm_model/step/step-modelall-0.01-b-pngram-light";
		String srFeaturesFile = "svm_model/step/BugReportFea_step_all";
		String srSuffix = "sr";
		String srClass = classifyInstance(srModel, srFeaturesFile, srSuffix, bugId, report);

		Labels labels = new Labels("", ebClass, srClass);
		PredictionOutput output = new PredictionOutput(labels, null);

		return output;
	}

	private String classifyInstance(String model, String featuresFile, String suffix, String bugId, BugReport report)
			throws IOException, InterruptedException {

		BugReportFea fea = new BugReportFea(false, featuresFile);
		fea.configure(report);
		String feaStr = fea.getSVMFormatString();

		ArrayList<String> testline = new ArrayList<String>();
		testline.add("+1 " + feaStr);

		String svmFile = "output/" + bugId + ".svm." + suffix;
		Common.outputLines(testline, svmFile, false);

		String outFile = "output/" + bugId + ".out." + suffix;
		// classify

		String command = "svm_light/v6.01/svm_light_windows/svm_classify.exe -f 0 " + svmFile + " " + model + " "
				+ outFile;
//		System.out.println(command);
		Process p = Runtime.getRuntime().exec(command);
		// String line;
		// BufferedReader input = new BufferedReader(new
		// InputStreamReader(p.getInputStream()));
		// while ((line = input.readLine()) != null) {
		// System.out.println(line);
		// }
		// input.close();
		p.waitFor();

		// read output
		List<String> lines = FileUtils.readLines(new File(outFile));
		String line = lines.get(0);
		String cls = line.split(" ")[0].split(":")[1];

		return cls.equals("+1") ? "x" : "";
	}

}
