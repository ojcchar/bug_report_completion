package seers.bugreppatterns.processor;

import java.io.File;
import java.util.List;

import net.quux00.simplecsv.CsvWriter;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.bugreppatterns.main.MainHRClassifier;
import seers.bugreppatterns.pattern.PatternMatcher;

public class BugReportProcessor extends ThreadProcessor {

	private String dataFolder;
	private CsvWriter wr;
	private List<PatternMatcher> patterns;
	private List<File> files;
	private String granularity;

	public BugReportProcessor(ThreadParameters params) {
		super(params);

		dataFolder = params.getStringParam(MainHRClassifier.DATA_FOLDER);
		wr = params.getParam(CsvWriter.class, MainHRClassifier.OUT_WR);
		patterns = params.getListParam(PatternMatcher.class, MainHRClassifier.PATTERNS);
		files = params.getListParam(File.class, ThreadExecutor.ELEMENTS_PARAM);
		granularity = params.getStringParam(MainHRClassifier.GRANULARITY);
	}

	@Override
	public void executeJob() throws Exception {

	}

}
