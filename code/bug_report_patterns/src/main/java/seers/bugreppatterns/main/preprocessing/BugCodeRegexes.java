package seers.bugreppatterns.main.preprocessing;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static seers.bugreppatterns.main.preprocessing.BugContentCategory.*;

public class BugCodeRegexes {


    //regexes by system in general
    static HashMap<String, List<ImmutablePair<String, BugContentCategory>>>
            systemRegexes = new HashMap<>();
    //regexes by system that match the start of sentences
    static HashMap<String, List<ImmutablePair<String, BugContentCategory>>> systemStartRegexes = new HashMap<>();
    //regexes by system that match the end of sentences
    static HashMap<String, List<ImmutablePair<String, BugContentCategory>>> systemEndRegexes = new HashMap<>();

    //regexes by system grouped by "paragraph"
    static HashMap<String, List<ImmutablePair<List<String>, BugContentCategory>>> systemGroupRegexes = new HashMap<>();

    //these are for JIRA and Github bugs, where there are special tags/separators to delimit and highlight code
    static HashMap<String, List<String>> systemIniSeparators = new HashMap<>();

    static {
        addDockerRegexes();
        addEclipseRegexes();
        addFacebookRegexes();
        addFirefoxRegexes();
        addHibernateRegexes();
        addHttpdRegexes();
        addLibreOfficeRegexes();
        addOpenMrsRegexes();
        addWordPressRegexes();

        addArgoumlRegexes();
        addJeditRegexes();
        addOpenOfficeRegexes();

        addOtherSystemsRegexes();
    }

    private static void addOtherSystemsRegexes() {

        List<String> projects = Arrays.asList("wfcore", "derby", "datamongo", "pig",
                "math", "openjpa", "mng", "amq", "ww", "myfaces", "groovy", "drill", "continuum",
                "wicket", "cb", "accumulo", "hbase", "spark", "hadoop", "cassandra", "hive", "pdfbox", "datacmns",
                "mahout");

        for (String projectName : projects) {

            systemIniSeparators.put(projectName, Arrays.asList("{code}", "{noformat}"));

            List<String> regexes = Arrays.asList("([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)",
                    "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Native Method\\)",
                    "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                    "\\$([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                    "(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)",
                    "(\\s+)?(at )?\\$([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                    "(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                    "(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Native Method\\)",
                    "(\\s+)?(at )?\\$([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Native Method\\)",
                    "Caused by\\: .+Exception.+\\:");

            systemRegexes.put(projectName, regexes.stream()
                    .map(r -> new ImmutablePair<>(r, BugContentCategory.EXEC_TRACES))
                    .collect(Collectors.toList())
            );

            List<List<String>> logRx = new ArrayList<>();

            logRx.add(Arrays.asList("Java Version", "Java Vendor", "Java Classpath"));
            final List<String> logStrs = Arrays.asList("\\[INFO\\]", "\\[WARN\\]", "\\[DEBUG\\]", "\\[ERROR\\]");
            logRx.add(logStrs);

            systemStartRegexes.put(projectName, logStrs.stream()
                    .map(r -> new ImmutablePair<>(r, BugContentCategory.PROG_LOGS))
                    .collect(Collectors.toList())
            );

            List<List<String>> tracesRx = new ArrayList<>();
            tracesRx.add(Arrays.asList("([a-zA-Z0-9]+[\\.])+[a-zA-Z0-9]+Exception:",
                    "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)",
                    "[argouml].*at.*([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+.*", "\\([a-zA-Z0-9]+\\.java:\\d+\\)"));

            addGroupRegexes(projectName, null, tracesRx, logRx);
        }
    }

    private static void addOpenOfficeRegexes() {

        final String projectName = "openoffice";

        List<List<String>> logRx = new ArrayList<>();
        logRx.add(Arrays.asList("0x[0-9A-Fa-f]+", "\\+ \\d+", "\\.dylib", "Thread State", "at [a-zA-Z0-9]+\\.c:\\d+"));
        logRx.add(Arrays.asList("Model:", "Graphics:", "Network Service:"));
        logRx.add(Arrays.asList("ProblemType:", "Architecture:", "Uname:"));


        List<List<String>> tracesRx = new ArrayList<>();
        tracesRx.add(Arrays.asList("([a-zA-Z0-9]+[\\.])+[a-zA-Z0-9]+Exception:?",
                "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)"
        ));

        addGroupRegexes(projectName, null, tracesRx, logRx);
    }

    private static void addJeditRegexes() {

        final String projectName = "jedit";

        List<List<String>> logRx = new ArrayList<>();
        logRx.add(Arrays.asList("java\\.version", "java\\.vendor", "java\\.vm\\.version"));

        List<List<String>> tracesRx = new ArrayList<>();
        tracesRx.add(Arrays.asList("([a-zA-Z0-9]+[\\.])+[a-zA-Z0-9]+Exception:?",
                "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)",
                "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\\\?\\([a-zA-Z0-9]+\\.java:\\d+\\\\?\\)",
                "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\\\?\\(Unknown Source\\\\?\\)"));

        addGroupRegexes(projectName, null, tracesRx, logRx);
    }

    private static void addArgoumlRegexes() {

        final String projectName = "argouml";
        List<String> regexes = Arrays.asList("([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)",
                "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Native Method\\)",
                "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                "\\$([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                "(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)",
                "(\\s+)?(at )?\\$([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                "(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                "(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Native Method\\)",
                "(\\s+)?(at )?\\$([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Native Method\\)",
                "Caused by\\: .+Exception.+\\:");

        systemRegexes.put(projectName, regexes.stream()
                .map(r -> new ImmutablePair<>(r, BugContentCategory.EXEC_TRACES))
                .collect(Collectors.toList())
        );

        List<List<String>> logRx = new ArrayList<>();

        logRx.add(Arrays.asList("Java Version", "Java Vendor", "Java Classpath"));

        List<List<String>> tracesRx = new ArrayList<>();
        tracesRx.add(Arrays.asList("([a-zA-Z0-9]+[\\.])+[a-zA-Z0-9]+Exception:",
                "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)",
                "[argouml].*at.*([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+.*", "\\([a-zA-Z0-9]+\\.java:\\d+\\)"));

        addGroupRegexes(projectName, null, tracesRx, logRx);
    }

    private static void addWordPressRegexes() {
        // wordpress-android

        final String projectName = "wordpress-android";
        systemIniSeparators.put(projectName, Collections.singletonList("```"));

        List<String> regexes = Arrays.asList("([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)",
                "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Native Method\\)",
                "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                "\\$([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                "(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)",
                "(\\s+)?(at )?\\$([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                "(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                "(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Native Method\\)",
                "(\\s+)?(at )?\\$([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Native Method\\)",
                "Caused by\\: .+Exception.+\\:");

        systemRegexes.put(projectName, regexes.stream()
                .map(r -> new ImmutablePair<>(r, BugContentCategory.EXEC_TRACES))
                .collect(Collectors.toList())
        );

        systemStartRegexes.put(projectName, Stream.of("\\[screen shot", "\\[screenshot")
                .map(r -> new ImmutablePair<>(r, BugContentCategory.PROG_LOGS))
                .collect(Collectors.toList()));

        List<List<String>> logRx = new ArrayList<>();

        logRx.add(Arrays.asList("\\[INFO\\]", "\\[WARN\\]", "\\[DEBUG\\]", "\\[ERROR\\]"));
        logRx
                .add(Arrays.asList("D\\/Cursor", "D\\/ActionBarSherlock", "D\\/AndroidRuntime", "E\\/AndroidRuntime"));
        logRx.add(
                Arrays.asList("\\<\\/methodResponse", "\\<\\/fault", "\\<\\/methodResponse", "\\<\\/methodResponse"));

        addGroupRegexes(projectName, null, null, logRx);
    }

    private static void addOpenMrsRegexes() {
        // -------------------------------------------------

        // openmrs

        final String projectName = "openmrs";
        systemIniSeparators.put(projectName, Arrays.asList("{code}", "{noformat}"));

        systemStartRegexes.put(projectName,
                Stream.of("([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)",
                        "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Native Method\\)",
                        "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                        "\\$([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                        "(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)",
                        "(\\s+)?(at )?\\$([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                        "(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)",
                        "(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Native Method\\)",
                        "(\\s+)?(at )?\\$([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Native Method\\)",
                        "Caused by\\: .+Exception.+\\:",
                        "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+ \\. [a-zA-Z0-9]+ \\(\\d+\\)")
                        .map(r -> new ImmutablePair<>(r, BugContentCategory.EXEC_TRACES))
                        .collect(Collectors.toList())
        );

        List<List<String>> logRx = new ArrayList<>();

        logRx.add(Arrays.asList("INFO - ", "ERROR - "));

        List<List<String>> tracesRx = new ArrayList<>();
        tracesRx.add(Arrays.asList("([a-zA-Z0-9]+[\\.])+[a-zA-Z0-9]+Exception:",
                "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)"));

        addGroupRegexes(projectName, null, tracesRx, logRx);
    }

    private static void addLibreOfficeRegexes() {
        final String projectName = "libreoffice";

        // -------------------------------------------------

        // libreoffice
        List<List<String>> codeRx = new ArrayList<>();

        codeRx.add(Arrays.asList("--enable-", "--with-"));


        List<List<String>> logsRx = new ArrayList<>();
        logsRx.add(Arrays.asList("AE162\\=", "AD162\\="));
        logsRx.add(Arrays.asList("ProblemType: ", "DistroRelease: "));
        logsRx.add(Arrays.asList("Range\\(\\\"B3\\\"\\)", "Range\\(\\\"C3\\\"\\)"));

        List<List<String>> tracesRx = new ArrayList<>();
        tracesRx.add(Arrays.asList("#\\d+\\s+0x[0-9A-Fa-f]+\\s+in", "at\\s+.+:\\d+"));

        addGroupRegexes(projectName, codeRx, tracesRx, logsRx);
    }

    private static void addHttpdRegexes() {
        // -------------------------------------------------

        // httpd
        final String projectName = "httpd";
        systemStartRegexes.put(projectName,
                Stream.of("Installing configuration files.+\\[", "\\/configure --", "use [a-zA-Z0-9]+\\:\\:.+",
                        "#use [a-zA-Z0-9]+\\:\\:.+", "#define ", "^c.+mod_.+.$", "\\[.+\\].+\\[info\\]",
                        ".+\\[.+\\].+\\\"GET \\/\\\"", "httpd in free\\(\\)\\:")
                        .map(r -> new ImmutablePair<>(r, BugContentCategory.PROG_LOGS))
                        .collect(Collectors.toList())
        );

        List<List<String>> codeRx = new ArrayList<>();

        codeRx.add(Arrays.asList("if \\[ \\!", "\\]\\; then", "if not exist \\\"", "mkdir \\\"", "$ diff"));
        codeRx.add(Arrays.asList("configure --prefix\\=", "gmake install", "config --prefix\\=",
                "CXXFLAGS\\=", "CFLAGS\\=", "--prefix\\=", "--with-"));
        codeRx.add(Arrays.asList("\\<div ", "\\<\\/div\\>", "\\<script", "\\<\\/script\\>", "\\<span ",
                "\\&lt\\;script", "\\&lt\\;\\/script", "\\<a href\\=", "\\<br\\>", "\\-->", "\\<\\!", "\\<html\\>",
                "\\<\\/html\\>", "\\<head\\>", "\\<body\\>", "\\<\\/body\\>"));
        codeRx.add(Arrays.asList("try \\{", "for ?\\(", "if ?\\(", "else if ?\\(", "while ?\\(",
                "return [a-zA-z]+\\;", "public void ", "else ?\\{", "exit ?\\(0\\)\\;", "exit ?\\(1\\)\\;"));

        List<List<String>> logRx = new ArrayList<>();
        logRx.add(Arrays.asList("See any operating system documentation", "more information, such as the ld"));
        logRx.add(Arrays.asList("If you ever happen to want", "in a given directory, LIBDIR"));
        logRx.add(Arrays.asList("Building shared\\: ", "Building shared\\: mod"));
        logRx.add(Arrays.asList(" \\[info\\] ", " \\[warn\\] ", " \\[notice\\] ", " \\[debug\\] ", "\\[error\\] "));
        logRx.add(Arrays.asList("^\\d+\\:.+\\= \\d+", "^\\d+\\:.+\\= \\d+ \\[\\d+\\]"));
        logRx.add(Arrays.asList("Reading symbols from \\/", "Loaded symbols for \\/"));
        logRx.add(Arrays.asList("No symbol table info available\\.", "\\#\\d+\\s+0x[0-9A-Fa-f]+\\s+in\\s+\\?\\?",
                "\\#\\d+\\s+0x[0-9A-Fa-f]+\\s+in\\s+.+", "\\#\\d+\\s+0x[0-9A-Fa-f]+\\s+in\\s+.+at\\s+.+\\d+"));
        logRx.add(Arrays.asList("GDB is free software", "This GDB was configured as"));
        logRx.add(Arrays.asList("-rw-", "-rwx", "\\$ ls -"));
        logRx.add(Arrays.asList("exports\\.c\\:\\d+", "make\\[\\d+\\]\\:", "make\\: "));
        logRx.add(Arrays.asList("VirtualHost ", "ServerName ", "CacheRoot ", "AllowOverride ", "Directory ",
                "CacheSize "));
        logRx.add(Arrays.asList("User-Agent\\: ", "Host\\: ", "Keep-Alive\\: ", "Connection\\: "));
        logRx.add(Arrays.asList("crashdump\\[\\d+\\]\\:", "0x[0-9A-Fa-f]+", "SIGSEGV", "Segmentation fault",
                "\\s+inet\\s+", "\\s+TCP\\s+", "SIGTRAP"));
        logRx.add(Arrays.asList("configure\\:\\d+\\: ", "configure\\:\\d+\\: gcc "));
        logRx.add(Arrays.asList("Server version\\: ", "Server built\\: "));
        logRx.add(Arrays.asList("make\\[1\\]\\: ", "make\\[2\\]\\: "));
        logRx.add(Arrays.asList("0x[0-9A-Fa-f]+", ", line \\d+ in", "SIGTRAP"));
        logRx.add(Arrays.asList("--- ", "\\+\\+\\+ "));
        logRx.add(Arrays.asList("AllowOverride ", "Allow from ", "\\<Location"));

        addGroupRegexes(projectName, codeRx, null, logRx);
    }

    private static void addHibernateRegexes() {
        // -------------------------------------------------

        // hibernate
        final String projectName = "hibernate";
        systemIniSeparators.put(projectName, Arrays.asList("{code}", "{noformat}"));

        Stream<ImmutablePair<String, BugContentCategory>> tracesRegexes = Stream.of("^(\\s+)?at .+\\(Unknown Source\\)$"
                , "^(\\s+)?at .+\\(Native Method\\)$")
                .map(r -> new ImmutablePair<>(r, BugContentCategory.EXEC_TRACES));

        Stream<ImmutablePair<String, BugContentCategory>> codeRegexes =
                Stream.of(" throw new [a-zA-Z0-9]+ ?\\(", ";.+\\}$", "\\{code\\}$")
                        .map(r -> new ImmutablePair<>(r, BugContentCategory.SRC_CODE));

        Stream<ImmutablePair<String, BugContentCategory>> logRegexes =
                Stream.of("INFO \\[", "WARN \\[", "ERROR \\[", "DEBUG \\[")
                        .map(r -> new ImmutablePair<>(r, PROG_LOGS));


        systemRegexes.put(projectName, Stream.of(tracesRegexes, codeRegexes, logRegexes)
                .reduce(Stream::concat).get().collect(Collectors.toList()));


        final Stream<ImmutablePair<String, BugContentCategory>> tracesStr = Stream.of("(\\s+)?(at )?" +
                        "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)",
                "(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)")
                .map(r -> new ImmutablePair<>(r, BugContentCategory.EXEC_TRACES));

        final Stream<ImmutablePair<String, BugContentCategory>> logsStr = Stream.of("!ENTRY ", "!MESSAGE ",
                "!STACK ",
                "User\\-Agent\\: ", "Java VM\\: ", "VM state\\:", "Heap.+total \\d+.+ used \\d+")
                .map(r -> new ImmutablePair<>(r, BugContentCategory.PROG_LOGS));

        final Stream<ImmutablePair<String, BugContentCategory>> codeStr =
                Stream.of("import ([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+\\;",
                        "package ([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+\\;",
                        "public class ", "public static ", "private static ",
                        "\\@Entity", "\\@Table\\(", "\\@Column\\(", "\\@Id", "\\@Override", "@Field\\(", "@Lob",
                        "@Cache", "@MappedSuperclass", "@ManyToOne", "@NotNull", "@DefaultBooleanValue\\(", "@Column",
                        "\\@OneToMany\\(", "CREATE TABLE ", "\\@Embeddable", "(public|private) [a-zA-Z0-9]+ .+\\).*\\{",
                        "\\@Test", "class [a-zA-Z0-9]+ ?\\{", "\\{code\\:java\\}", "\\{code", "try \\{", "for ?\\(",
                        "if ?\\(", "public static ", "else if ?\\(", "\\*\\;.+import ", "\\<persistence-unit",
                        "\\<provider", "\\<\\/persistence-unit", "\\<property", "\\<column", "\\<subclass",
                        "registerColumnType\\(", "Hibernate: .+\\(", "INSERT INTO ", "DELETE FROM ")
                        .map(r -> new ImmutablePair<>(r, BugContentCategory.SRC_CODE));


        systemStartRegexes.put(projectName,
                Stream.of(logsStr, codeStr, tracesStr)
                        .reduce(Stream::concat).get()
                        .collect(Collectors.toList())
        );

        List<List<String>> codeRxs = new ArrayList<>();

        codeRxs.add(Arrays.asList("public class ", "try \\{", "for ?\\(", "if ?\\(", "public static ",
                "else if ?\\(", "while ?\\(", "return [a-zA-z]+\\;", "public void ", "\\@Entity", "\\@Table\\(",
                "\\@Column\\(", "\\@Id", "\\@Override", "@Field\\(", "@Lob", "@MappedSuperclass", "@ManyToOne",
                "@Cache", "@NotNull", "@DefaultBooleanValue\\(", "\\@OneToMany\\(", "CREATE TABLE ", "\\@Embeddable",
                "\\@Test", " \\= new [a-zA-Z0-9]+ ?\\(", "\\{code\\:java\\}", "\\{code\\}", "\\* \\(non-Javadoc\\)",
                "\\*\\/"));
        codeRxs.add(Arrays.asList("\\<persistence-unit", "\\<provider", "\\<\\/persistence-unit",
                "\\<property", "\\<column"));
        codeRxs.add(Arrays.asList("\\<ehcache", "\\<defaultCache", "\\<defaultCache"));
        codeRxs
                .add(Arrays.asList("\\<hibernate-mapping", "\\<\\/hibernate-mapping", "\\<composite-id", "\\<class"));
        codeRxs
                .add(Arrays.asList("SELECT ", "INNER JOIN", "WHERE ", "select ", "as col_", "inner join", "where "));
        codeRxs.add(Arrays.asList("openSession\\(", "beginTransaction\\(", "commit\\(\\)\\;"));


        List<List<String>> tracesRx = new ArrayList<>();
        tracesRx.add(Arrays.asList("at ([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+",
                "\\([a-zA-Z0-9]+\\.java:\\d+\\)", "Caused by\\: ([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+"));


        List<List<String>> logsRx = new ArrayList<>();
        logsRx.add(Arrays.asList("\\[javac\\] ", "\\[javac\\] \\d+ error"));
        logsRx.add(Arrays.asList("INFO \\[", "WARN \\[", "ERROR \\[", "DEBUG \\["));
        logsRx.add(Arrays.asList("Daemon Thread ", "\\) line: \\d+"));
        logsRx.add(Arrays.asList("\\d+ DEBUG SQL", "select"));

        addGroupRegexes(projectName, codeRxs, tracesRx, logsRx);
    }

    private static void addFirefoxRegexes() {
        // firefox
        final String projectName = "firefox";
        systemStartRegexes.put(projectName,
                Arrays.asList("User[ \\-]Agent\\: ", "( +)?xul\\.dll\\!", "( +)?USER32\\.DLL\\!",
                        "( +)?mozcrt19\\.dll\\!", "NET CLR ", "Build tools.+Compiler.+Version.+flags",
                        "Built from http\\:", "\\#[a-zA-Z]+ \\{", "\\d{4}-\\d{2}-\\d{2}.+firefox-bin",
                        "\\d+.+xul\\.dll.+", "Reproducible: ", "\\<\\/[a-zA-Z]+\\>").stream()
                        .map(r -> new ImmutablePair<>(r, BugContentCategory.PROG_LOGS)).collect(Collectors.toList())
        );

        List<List<String>> codeRx = new ArrayList<>();

        codeRx.add(Arrays.asList("\\<div ", "\\<\\/div\\>", "\\<script", "\\<\\/script\\>", "\\<span ",
                "\\&lt\\;script", "\\&lt\\;\\/script", "\\<a href\\=", "\\<br\\>", "\\-->", "\\<\\!", "\\<html\\>",
                "\\<head\\>"));
        // firefoxGroupRegexes.add(Arrays.asList("User\\-Agent",
        // "Keep\\-Alive"));
        codeRx.add(Arrays.asList("\\$[a-zA-z]+ \\=", "echo  ", "foreach ?\\(", "for ?\\(", "if ?\\(",
                "PHP_EOL", "function ?\\(.+\\{", "else ?\\{", "\\$\\_POST\\[", "\\$\\_GET\\[", "\\= ?\\{", "\\}\\;",
                "(onKeyup|onKeypress) ?\\:", "\\{$", "\\}$", "^\\{.+\\}$", "\\}\\)\\;", "\\/\\*\\*", "\\*\\/",
                "var [a-zA-z]+ \\= "));

        List<List<String>> logsRx = new ArrayList<>();
        logsRx.add(Arrays.asList("xul\\.dll\\!", "USER32\\.DLL\\!", "mozcrt19\\.dll\\!"));
        logsRx.add(Arrays.asList("User[ \\-]Agent\\: ", "NET CLR ", "Build Identifier\\:"));
        logsRx.add(Arrays.asList("\\>TEST\\-INFO ", "\\>TEST-PASS "));
        logsRx.add(Arrays.asList("CrashTime\\: ", "ProductName\\: "));
        logsRx.add(Arrays.asList("ASSERT\\: ", "Stack Trace\\:"));
        logsRx.add(Arrays.asList("Configure arguments", "--enable-.+\\="));
        logsRx.add(Arrays.asList("TEST-UNEXPECTED-FAIL ", "PROCESS-CRASH "));
        logsRx.add(Arrays.asList("From\\: ", "Subject\\: "));

        addGroupRegexes(projectName, codeRx, null, logsRx);
    }

    private static void addFacebookRegexes() {
        // -------------------------------------------------

        // facebook

        final String projectName = "facebook";

        systemRegexes.put(projectName,
                Stream.of("if ?\\(.+\\{").map(r -> new ImmutablePair<>(r,
                        BugContentCategory.SRC_CODE))
                        .collect(Collectors.toList()));

        systemStartRegexes.put(projectName, Stream.of("PHP_EOL", "\\$[a-zA-z]+ \\=", "foreach ?\\(", "echo  ",
                "\\<\\?", "for ?\\(.+\\{", "User[ \\-]Agent\\: ").map(r -> new ImmutablePair<>(r,
                BugContentCategory.PROG_LOGS))
                .collect(Collectors.toList()));

        List<List<String>> logRx = new ArrayList<>();

        logRx.add(Arrays.asList("\\[\\{", " \\: \\[", "\\}\\_"));
        logRx.add(Arrays.asList("Request Stream\\:", "access_token\\="));
        logRx.add(Arrays.asList("User\\-Agent", "Keep\\-Alive"));
        logRx.add(Arrays.asList("\\[key\\] \\=\\>", "\\[value\\] \\=\\>"));
        logRx.add(Arrays.asList("line number\\: ", "stack\\: ", "message \\:"));
        logRx.add(Arrays.asList("User[ \\-]Agent\\: ", "NET CLR "));

        List<List<String>> codeRx = new ArrayList<>();
        codeRx.add(Arrays.asList("\\<fb\\:", "\\<\\/fb"));
        codeRx.add(Arrays.asList("\\<div ", "\\<\\/div\\>", "\\<script", "\\<\\/script\\>", "\\<span ",
                "\\&lt\\;script", "\\&lt\\;\\/script", "\\<a href\\="));
        codeRx.add(Arrays.asList("\\$[a-zA-z]+ \\=", "echo  ", "foreach ?\\(", "if ?\\(", "PHP_EOL",
                "function ?\\(.+\\{", "else ?\\{", "\\$\\_POST\\[", "\\$\\_GET\\[", "\\= ?\\{", "\\}\\;",
                "(onKeyup|onKeypress) ?\\:", "\\{$", "\\}$", "^\\{.+\\}$", "\\}\\)\\;"));


        addGroupRegexes(projectName, codeRx, null, logRx);
    }

    private static void addEclipseRegexes() {

        // -------------------------------------------------

        // eclipse
        final String projectName = "eclipse";
        systemRegexes.put(projectName, Stream.of("\\\\bin\\\\java ")
                .map(r -> new ImmutablePair<>(r, BugContentCategory.SRC_CODE))
                .collect(Collectors.toList()));


        // -------------------------------------------------

        final Stream<ImmutablePair<String, BugContentCategory>> tracesRx = Stream.of("(\\s+)?(at )?" +
                "([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)")
                .map(r -> new ImmutablePair<>(r, EXEC_TRACES));

        final Stream<ImmutablePair<String, BugContentCategory>> logsRx = Stream.of("!ENTRY ", "!MESSAGE ",
                "!STACK ",
                "User\\-Agent\\: ", "Java VM\\: ", "VM state\\:",
                "Heap.+total \\d+.+ used \\d+", "ini \\-command ",
                "Build version\\: ", "Unexpected Signal : ", "0x[0-9A-Fa-f]+ - 0x[0-9A-Fa-f]+")
                .map(r -> new ImmutablePair<>(r, PROG_LOGS));

        final Stream<ImmutablePair<String, BugContentCategory>> codeRx = Stream.of("\\\\jre\\\\bin\\\\java ",
                "import ([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+\\;",
                "package ([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+\\;",
                "public class ", "public static ",
                "java -version\\:", "java version \\\"", "\\(D\\:\\\\")
                .map(r -> new ImmutablePair<>(r, SRC_CODE));

        systemStartRegexes.put(projectName,
                Stream.of(tracesRx, logsRx, codeRx)
                        .reduce(Stream::concat).get()
                        .collect(Collectors.toList())
        );


        // -------------------------------------------------
        systemEndRegexes.put(projectName,
                Stream.of("\\) line: \\d+", "\\) line: not available \\[native method\\]", "\\(Unknown Source\\)")
                        .map(r -> new ImmutablePair<>(r, EXEC_TRACES)).collect(Collectors.toList()));


        // -------------------------------------------------
        List<List<String>> codeRx2 = new ArrayList<>();

        codeRx2.add(Arrays.asList("eclipse\\.buildId\\=", "java\\.version\\=", "java\\.vendor\\="));
        codeRx2.add(Arrays.asList("\\/\\*\\*", "\\*\\/"));
        codeRx2.add(Arrays.asList("\\/\\* \\(non\\-Javadoc\\)", "\\*\\/"));
        codeRx2.add(Arrays.asList("eclipse\\.buildId\\=", "java\\.fullversion\\="));
        codeRx2.add(Arrays.asList("try \\{", "for ?\\(", "if ?\\(", "public static ", "while ?\\(",
                "return [a-zA-z]+\\;", "public void "));
        codeRx2.add(Arrays.asList("<w:wordDocument ", "<w:style "));


        List<List<String>> tracesRx2 = new ArrayList<>();
        tracesRx2
                .add(Arrays.asList("at ([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+", "\\([a-zA-Z0-9]+\\.java:\\d+\\)"));
        tracesRx2
                .add(Arrays.asList("j  ([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+", "J  ([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+"));

        List<List<String>> logsRx2 = new ArrayList<>();
        logsRx2.add(Arrays.asList("Process\\: ", "Parent Process\\: "));
        logsRx2.add(Arrays.asList("OS Version\\: ", "Report Version\\: "));
        logsRx2.add(Arrays.asList("Crashes Since Last Report\\: ", "Anonymous UUID\\: "));
        logsRx2.add(Arrays.asList("Exception Type\\: ", "Crashed Thread\\: "));
        logsRx2.add(Arrays.asList("Java information\\:", "Exception type\\: "));
        logsRx2.add(Arrays.asList(" 0x[0-9A-Fa-f]+ JavaThread ", " 0x[0-9A-Fa-f]+ VMThread ",
                " 0x[0-9A-Fa-f]+ WatcherThread "));
        logsRx2.add(Arrays.asList("Virtual Machine Arguments\\:", "JVM Args\\: "));
        logsRx2.add(Arrays.asList("ENTRY org.eclipse.osgi ", "SUBENTRY \\d org.eclipse.osgi "));
        logsRx2
                .add(Arrays.asList("\\d+\\s+Project.+is missing required", "The project cannot be built until"));
        logsRx2.add(Arrays.asList("line \\d+: warning:", "line \\d+: error:"));
        logsRx2.add(Arrays.asList("Enter bugs above this line", "installation :"));


        addGroupRegexes(projectName, codeRx2, tracesRx2, logsRx2);
    }

    private static void addGroupRegexes(String projectName, List<List<String>> codeRx2, List<List<String>> tracesRx2,
                                        List<List<String>> logsRx2) {
        final Stream<ImmutablePair<List<String>, BugContentCategory>> codeGrpRegexes = codeRx2 == null ?
                Stream.empty() :
                codeRx2.stream().map(r -> new ImmutablePair<>(r, SRC_CODE));
        final Stream<ImmutablePair<List<String>, BugContentCategory>> tracesGrpRegexes = tracesRx2 == null ?
                Stream.empty() : tracesRx2.stream().map(r -> new ImmutablePair<>(r, EXEC_TRACES));
        final Stream<ImmutablePair<List<String>, BugContentCategory>> logsGrpRegexes = logsRx2 == null ?
                Stream.empty() :
                logsRx2.stream().map(r -> new ImmutablePair<>(r, PROG_LOGS));

        systemGroupRegexes.put(projectName, Stream.of(logsGrpRegexes, codeGrpRegexes, tracesGrpRegexes)
                .reduce(Stream::concat).get()
                .collect(Collectors.toList()));
    }

    private static void addDockerRegexes() {
        // docker
        final String projectName = "docker";
        systemIniSeparators.put(projectName, Arrays.asList("```", "\"'"));


        //--------------------------------------


        List<String> regexes = Arrays.asList("/home/.*/docker/", "\\.\\/docker", "Makefile:\\d+", "\\[default\\] ",
                "drwxr", "-rw-", "docker version.*\\:", "docker info.*\\:", "Version\\: ", "uname \\-a",
                "Linux.*x86_64.*GNU", "x86_64.*GNU.*Linux", "Containers\\:", "Client version\\:", "iface .*address ",
                "\\:\\~\\/ ", "\\:\\~\\# ", "\\~\\]\\# ", "cat \\/", "\\$ sudo ", "\\$ ps \\-", "\\/bin/docker ",
                " RUN ", ">RUN ", "dockerd\\[\\d+\\]\\:", "ERRO\\[\\d+\\]", "systemd\\[\\d+\\]\\:",
                ":\\~\\$ docker start", ":: busybox", "kernel: \\[ \\d+\\.\\d+\\]", "Message from syslogd");

        systemRegexes.put(projectName, regexes.stream().map(r -> new ImmutablePair<>(r, BugContentCategory.PROG_LOGS))
                .collect(Collectors.toList()));

        //--------------------------------------


        Stream<ImmutablePair<String, BugContentCategory>> logsRegexes = Stream.of(" ?\\[[0-9A-Fa-f]+\\]", "\\[error\\]",
                "\\[ERROR\\]", " ?\\d{4}\\/\\d{2}\\/\\d{2}",
                " ?\\d{4}-\\d{2}-\\d{2}", "ERRO\\[", "WARN\\[", "DEBU\\[", "INFO\\[", "FATA\\[")
                .map(r -> new ImmutablePair<>(r, PROG_LOGS));
        Stream<ImmutablePair<String, BugContentCategory>> codeRegexes = Stream.of(" ?sudo ", "\\$ docker run -")
                .map(r -> new ImmutablePair<>(r, SRC_CODE));
        Stream<ImmutablePair<String, BugContentCategory>> tracesRegexes = Stream.of(" ?at .+\\d+")
                .map(r -> new ImmutablePair<>(r, EXEC_TRACES));

        systemStartRegexes.put(projectName,
                Stream.of(logsRegexes, codeRegexes, tracesRegexes)
                        .reduce(Stream::concat).get()
                        .collect(Collectors.toList())
        );

        //--------------------------------------

        List<List<String>> logRx = new ArrayList<>();

        logRx.add(Arrays.asList("docker version.*\\:", "Version\\: ", "OS\\/Arch: ", "API version\\: "));
        logRx.add(Arrays.asList("docker info.*\\:", "Containers\\:", "Images\\:", "Operating System\\:",
                "CPUs\\:", "Total Memory\\:"));
        logRx.add(Arrays.asList(" eth", "address ", "netmask ", "auto eth", "iface ", "inet ", "inet6 "));
        logRx.add(Arrays.asList("container_name\\: ", "image\\: ", "ports\\: ", "volumes\\: "));
        logRx.add(Arrays.asList("goroutine \\d+ \\[", "0x[0-9A-Fa-f]+"));
        logRx.add(Arrays.asList("\\d+: [main|Init]", "Package: ", "File: "));
        logRx.add(Arrays.asList("Making bundle: ", "bundles\\/"));
        logRx.add(Arrays.asList(" INFO \\[", " WARN \\[", " DEBUG \\[", " ERROR \\["));
        logRx.add(Arrays.asList("ObjectName: ", "State: "));
        logRx.add(Arrays.asList("level=info ", "level=warning ", "level=error "));

        List<List<String>> codeRx = new ArrayList<>();
        codeRx.add(Arrays.asList("docker_.+test.*\\.go:\\d+:", "[0-9A-Fa-f]+"));
        codeRx.add(Arrays.asList("\\$ docker run -", "exit status"));

        addGroupRegexes(projectName, codeRx, null, logRx);
    }
}