package seers.bugreppatterns.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugreppatterns.main.prediction.HeuristicsClassifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SystemProcessor extends ThreadProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemProcessor.class);

    private String dataFolder;
    private String system;
    private String granularity;

    private HashMap<TextInstance, Labels> goldSet;

    @SuppressWarnings("unchecked")
    public SystemProcessor(ThreadParameters params) {
        super(params);

        dataFolder = params.getStringParam(HeuristicsClassifier.DATA_FOLDER);
        system = params.getStringParam(ThreadExecutor.ELEMENT_PARAM);
        granularity = params.getStringParam(HeuristicsClassifier.GRANULARITY);
        goldSet = params.getParam(HashMap.class, HeuristicsClassifier.GOLDSET);
    }

    @Override
    public void executeJob() throws Exception {

        String sysFolder = dataFolder + File.separator + system;
        List<File> files = getFiles(sysFolder);

        if (files == null) {
            sysFolder = dataFolder + File.separator + system + "_parse";
            files = getFiles(sysFolder);

            if (files == null) {
                files = new ArrayList<>();
            }
        }

        LOGGER.debug("[" + system + "]: " + files.size() + " files");

        Class<? extends ThreadProcessor> class1 = getProcessor();
        ThreadParameters newParams = new ThreadParameters(params);
        newParams.addParam(HeuristicsClassifier.SYSTEM, system);

        ThreadExecutor.executePaginated(files, class1, newParams, 8, 15);

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
            case "SP-F":
                class1 =   SentenceParagraphFeaturesProcessor.class;
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
            return null;
        }

        Files.walkFileTree(Paths.get(sysFolder), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                // no directories
                if (attrs.isDirectory()) {
                    return FileVisitResult.CONTINUE;
                }

                String fullFileName = file.toFile().getName();

                // does the file end with ".xml.parse"
                int idx = fullFileName.indexOf(".xml.parse");
                if (idx == -1 || !fullFileName.endsWith(".xml.parse")) {

                    idx = fullFileName.indexOf(".parse.xml");
                    if (idx == -1 || !fullFileName.endsWith(".parse.xml")) {
                        return FileVisitResult.CONTINUE;
                    }
                }

                String fileName = fullFileName.substring(0, idx);
                TextInstance txtInst = new TextInstance(system, fileName, "0");

                // does the file is in the gold-set
                Labels labels = goldSet.get(txtInst);
                if (labels != null) {
                    files.add(file.toFile());
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return files;
    }

}
