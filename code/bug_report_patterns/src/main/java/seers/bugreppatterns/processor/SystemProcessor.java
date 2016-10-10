package seers.bugreppatterns.processor;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.bugreppatterns.main.MainHRClassifier;

public class SystemProcessor extends ThreadProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemProcessor.class);

	private String dataFolder;
	private String system;
	private String granularity;

	public SystemProcessor(ThreadParameters params) {
		super(params);

		dataFolder = params.getStringParam(MainHRClassifier.DATA_FOLDER);
		system = params.getStringParam(ThreadExecutor.ELEMENT_PARAM);
		granularity = params.getStringParam(MainHRClassifier.GRANULARITY);
	}

	@Override
	public void executeJob() throws Exception {

		String sysFolder = dataFolder + File.separator + system + "_parse";
		List<File> files = getFiles(sysFolder);

		LOGGER.debug("[" + system + "]: " + files.size() + " files");

		Class<? extends ThreadProcessor> class1 = getProcessor();
		ThreadParameters newParams = new ThreadParameters(params);
		newParams.addParam(MainHRClassifier.SYSTEM, system);

		ThreadExecutor.executePaginated(files, class1, newParams, 50, 15);

		LOGGER.debug("[" + system + "]: " + " Done!");

	}

	private Class<? extends ThreadProcessor> getProcessor() {
		Class<? extends ThreadProcessor> class1 = null;
		switch (granularity) {
		case "B":
			class1 = BugReportProcessor.class;
			break;
		case "P":
			class1 = ParagraphProcessor.class;
			break;
		case "S":
			class1 = SentenceProcessor.class;
			break;

		default:
			break;
		}
		return class1;
	}

	private List<File> getFiles(String sysFolder) throws IOException {
		final List<File> files = new ArrayList<>();

		File folder = new File(sysFolder);
		if (!folder.exists()) {
			LOGGER.warn("Folder does not exist: " + sysFolder);
			return files;
		}

		Files.walkFileTree(Paths.get(sysFolder), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (!attrs.isDirectory()) {
					if (file.toFile().getName().endsWith(".xml.parse")) {
						files.add(file.toFile());
					}
				}
				return FileVisitResult.CONTINUE;
			}
		});
		return files;
	}

}
