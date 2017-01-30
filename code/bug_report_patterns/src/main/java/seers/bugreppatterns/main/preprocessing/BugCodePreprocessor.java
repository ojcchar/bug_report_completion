package seers.bugreppatterns.main.preprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.regularparse.BugReport;
import seers.bugrepcompl.entity.regularparse.DescriptionParagraph;
import seers.bugrepcompl.entity.regularparse.DescriptionSentence;
import seers.bugrepcompl.utils.DataReader;

public class BugCodePreprocessor {

	static String xmlBugDir = "C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/test_data/data";
	private static String goldSetFile = "C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/gold-set-B-all_data.csv";
	static String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/code_preprocessing";

	static HashMap<String, List<String>> systemRegexes = new HashMap<>();
	static HashMap<String, List<String>> systemStartRegexes = new HashMap<>();
	static HashMap<String, List<String>> systemEndRegexes = new HashMap<>();
	static HashMap<String, List<List<String>>> systemGroupRegexes = new HashMap<>();
	static HashMap<String, List<String>> systemIniSeparators = new HashMap<>();

	static HashSet<String> allowedSystems = new HashSet<String>(Arrays.asList(new String[] { "httpd" }));

	static {

		// docker
		systemIniSeparators.put("docker", Arrays.asList("```"));

		systemRegexes.put("docker",
				Arrays.asList("/home/.*/docker/", "\\.\\/docker", "Makefile:\\d+", "\\[default\\] ", "drwxr", "-rw-",
						"docker version.*\\:", "docker info.*\\:", "Version\\: ", "uname \\-a", "Linux.*x86_64.*GNU",
						"x86_64.*GNU.*Linux", "Containers\\:", "Client version\\:", "iface .*address ", "\\:\\~\\/ ",
						"\\:\\~\\# ", "\\~\\]\\# ", "cat \\/", "\\$ sudo ", "\\$ ps \\-", "\\/bin/docker ", " RUN ",
						">RUN "));

		systemStartRegexes.put("docker",
				Arrays.asList(" ?\\[[0-9A-Fa-f]+\\]", "\\[error\\]", "\\[ERROR\\]", " ?\\d{4}\\/\\d{2}\\/\\d{2}",
						" ?\\d{4}-\\d{2}-\\d{2}", "ERRO\\[", "WARN\\[", "DEBU\\[", "INFO\\[", "FATA\\[", " ?sudo ",
						" ?at .+\\d+"));

		List<List<String>> dockerGroupRegexes = new ArrayList<>();

		dockerGroupRegexes.add(Arrays.asList("docker version.*\\:", "Version\\: ", "OS\\/Arch: ", "API version\\: "));
		dockerGroupRegexes.add(Arrays.asList("docker info.*\\:", "Containers\\:", "Images\\:", "Operating System\\:",
				"CPUs\\:", "Total Memory\\:"));
		dockerGroupRegexes.add(Arrays.asList(" eth", "address ", "netmask ", "auto eth", "iface "));
		dockerGroupRegexes.add(Arrays.asList("container_name\\: ", "image\\: ", "ports\\: ", "volumes\\: "));

		systemGroupRegexes.put("docker", dockerGroupRegexes);

		// -------------------------------------------------

		// eclipse
		systemRegexes.put("eclipse", Arrays.asList("\\\\bin\\\\java "));

		systemStartRegexes.put("eclipse",
				Arrays.asList("(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)",
						"!ENTRY ", "!MESSAGE ", "!STACK ", "import ([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+\\;",
						"package ([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+\\;", "User\\-Agent\\: ", "Java VM\\: ", "VM state\\:",
						"Heap.+total \\d+.+ used \\d+", "ini \\-command ", "\\\\jre\\\\bin\\\\java ",
						"Build version\\: ", "Unexpected Signal : ", "0x[0-9A-Fa-f]+ - 0x[0-9A-Fa-f]+",
						"java -version\\:", "java version \\\"", "public class ", "public static "));

		systemEndRegexes.put("eclipse",
				Arrays.asList("\\) line: \\d+", "\\) line: not available \\[native method\\]", "\\(Unknown Source\\)"));

		List<List<String>> eclipseGroupRegexes = new ArrayList<>();

		eclipseGroupRegexes.add(Arrays.asList("eclipse\\.buildId\\=", "java\\.version\\=", "java\\.vendor\\="));
		eclipseGroupRegexes.add(Arrays.asList("\\/\\*\\*", "\\*\\/"));
		eclipseGroupRegexes.add(Arrays.asList("\\/\\* \\(non\\-Javadoc\\)", "\\*\\/"));
		eclipseGroupRegexes
				.add(Arrays.asList("at ([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+", "\\([a-zA-Z0-9]+\\.java:\\d+\\)"));
		eclipseGroupRegexes.add(Arrays.asList("Process\\: ", "Parent Process\\: "));
		eclipseGroupRegexes.add(Arrays.asList("OS Version\\: ", "Report Version\\: "));
		eclipseGroupRegexes.add(Arrays.asList("Crashes Since Last Report\\: ", "Anonymous UUID\\: "));
		eclipseGroupRegexes.add(Arrays.asList("Exception Type\\: ", "Crashed Thread\\: "));
		eclipseGroupRegexes.add(Arrays.asList("Java information\\:", "Exception type\\: "));
		eclipseGroupRegexes
				.add(Arrays.asList("j  ([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+", "J  ([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+"));
		eclipseGroupRegexes.add(Arrays.asList(" 0x[0-9A-Fa-f]+ JavaThread ", " 0x[0-9A-Fa-f]+ VMThread ",
				" 0x[0-9A-Fa-f]+ WatcherThread "));
		eclipseGroupRegexes.add(Arrays.asList("Virtual Machine Arguments\\:", "JVM Args\\: "));
		eclipseGroupRegexes.add(Arrays.asList("ENTRY org.eclipse.osgi ", "SUBENTRY \\d org.eclipse.osgi "));
		eclipseGroupRegexes.add(Arrays.asList("eclipse\\.buildId\\=", "java\\.fullversion\\="));
		eclipseGroupRegexes.add(Arrays.asList("try \\{", "for ?\\(", "if ?\\(", "public static ", "while ?\\(",
				"return [a-zA-z]+\\;", "public void "));
		// eclipseGroupRegexes.add(Arrays.asList("", "", "", "", "" ,""));

		systemGroupRegexes.put("eclipse", eclipseGroupRegexes);

		// -------------------------------------------------

		// facebook

		systemRegexes.put("facebook", Arrays.asList("if ?\\(.+\\{"));

		systemStartRegexes.put("facebook", Arrays.asList("PHP_EOL", "\\$[a-zA-z]+ \\=", "foreach ?\\(", "echo  ",
				"\\<\\?", "for ?\\(.+\\{", "User[ \\-]Agent\\: "));

		List<List<String>> facebookGroupRegexes = new ArrayList<>();

		facebookGroupRegexes.add(Arrays.asList("\\[\\{", " \\: \\[", "\\}\\_"));
		facebookGroupRegexes.add(Arrays.asList("Request Stream\\:", "access_token\\="));
		facebookGroupRegexes.add(Arrays.asList("\\<fb\\:", "\\<\\/fb"));
		facebookGroupRegexes.add(Arrays.asList("\\<div ", "\\<\\/div\\>", "\\<script", "\\<\\/script\\>", "\\<span ",
				"\\&lt\\;script", "\\&lt\\;\\/script", "\\<a href\\="));
		facebookGroupRegexes.add(Arrays.asList("User\\-Agent", "Keep\\-Alive"));
		facebookGroupRegexes.add(Arrays.asList("\\[key\\] \\=\\>", "\\[value\\] \\=\\>"));
		facebookGroupRegexes.add(Arrays.asList("\\$[a-zA-z]+ \\=", "echo  ", "foreach ?\\(", "if ?\\(", "PHP_EOL",
				"function ?\\(.+\\{", "else ?\\{", "\\$\\_POST\\[", "\\$\\_GET\\[", "\\= ?\\{", "\\}\\;",
				"(onKeyup|onKeypress) ?\\:", "\\{$", "\\}$", "^\\{.+\\}$", "\\}\\)\\;"));
		facebookGroupRegexes.add(Arrays.asList("line number\\: ", "stack\\: ", "message \\:"));
		facebookGroupRegexes.add(Arrays.asList("User[ \\-]Agent\\: ", "NET CLR "));

		systemGroupRegexes.put("facebook", facebookGroupRegexes);

		// -------------------------------------------------

		// firefox
		systemStartRegexes.put("firefox",
				Arrays.asList("User[ \\-]Agent\\: ", "( +)?xul\\.dll\\!", "( +)?USER32\\.DLL\\!",
						"( +)?mozcrt19\\.dll\\!", "NET CLR ", "Build tools.+Compiler.+Version.+flags",
						"Built from http\\:", "\\#[a-zA-Z]+ \\{", "\\d{4}-\\d{2}-\\d{2}.+firefox-bin",
						"\\d+.+xul\\.dll.+", "Reproducible: ", "\\<\\/[a-zA-Z]+\\>"));

		List<List<String>> firefoxGroupRegexes = new ArrayList<>();

		firefoxGroupRegexes.add(Arrays.asList("\\<div ", "\\<\\/div\\>", "\\<script", "\\<\\/script\\>", "\\<span ",
				"\\&lt\\;script", "\\&lt\\;\\/script", "\\<a href\\=", "\\<br\\>", "\\-->", "\\<\\!", "\\<html\\>",
				"\\<head\\>"));
		// firefoxGroupRegexes.add(Arrays.asList("User\\-Agent",
		// "Keep\\-Alive"));
		firefoxGroupRegexes.add(Arrays.asList("\\$[a-zA-z]+ \\=", "echo  ", "foreach ?\\(", "for ?\\(", "if ?\\(",
				"PHP_EOL", "function ?\\(.+\\{", "else ?\\{", "\\$\\_POST\\[", "\\$\\_GET\\[", "\\= ?\\{", "\\}\\;",
				"(onKeyup|onKeypress) ?\\:", "\\{$", "\\}$", "^\\{.+\\}$", "\\}\\)\\;", "\\/\\*\\*", "\\*\\/",
				"var [a-zA-z]+ \\= "));
		firefoxGroupRegexes.add(Arrays.asList("xul\\.dll\\!", "USER32\\.DLL\\!", "mozcrt19\\.dll\\!"));
		firefoxGroupRegexes.add(Arrays.asList("User[ \\-]Agent\\: ", "NET CLR ", "Build Identifier\\:"));
		firefoxGroupRegexes.add(Arrays.asList("\\>TEST\\-INFO ", "\\>TEST-PASS "));
		firefoxGroupRegexes.add(Arrays.asList("CrashTime\\: ", "ProductName\\: "));
		firefoxGroupRegexes.add(Arrays.asList("ASSERT\\: ", "Stack Trace\\:"));
		firefoxGroupRegexes.add(Arrays.asList("Configure arguments", "--enable-.+\\="));
		firefoxGroupRegexes.add(Arrays.asList("TEST-UNEXPECTED-FAIL ", "PROCESS-CRASH "));
		firefoxGroupRegexes.add(Arrays.asList("From\\: ", "Subject\\: "));

		systemGroupRegexes.put("firefox", firefoxGroupRegexes);

		// -------------------------------------------------

		// hibernate

		systemIniSeparators.put("hibernate", Arrays.asList("{code}", "{noformat}"));

		systemRegexes.put("hibernate", Arrays.asList("^(\\s+)?at .+\\(Unknown Source\\)$",
				"^(\\s+)?at .+\\(Native Method\\)$", " throw new [a-zA-Z0-9]+ ?\\(", ";.+\\}$"));

		systemStartRegexes.put("hibernate", Arrays.asList(
				"(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\([a-zA-Z0-9]+\\.java:\\d+\\)",
				"(\\s+)?(at )?([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+\\(Unknown Source\\)", "!ENTRY ", "!MESSAGE ",
				"!STACK ", "import ([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+\\;", "package ([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+\\;",
				"User\\-Agent\\: ", "Java VM\\: ", "VM state\\:", "Heap.+total \\d+.+ used \\d+", "public class ",
				"public static ", "private static ", "\\@Entity", "\\@Table\\(", "\\@Column\\(", "\\@Id", "\\@Override",
				"@Field\\(", "@Lob", "@Cache", "@MappedSuperclass", "@ManyToOne", "@NotNull", "@DefaultBooleanValue\\(",
				"\\@OneToMany\\(", "CREATE TABLE ", "\\@Embeddable", "(public|private) [a-zA-Z0-9]+ .+\\).*\\{",
				"\\@Test", "class [a-zA-Z0-9]+ ?\\{", "\\{code\\:java\\}", "\\{code\\}", "try \\{", "for ?\\(",
				"if ?\\(", "public static ", "else if ?\\(", "\\*\\;.+import ", "\\<persistence-unit", "\\<provider",
				"\\<\\/persistence-unit", "\\<property", "\\<column", "\\<subclass", "registerColumnType\\("));

		List<List<String>> hibernateGroupRegexes = new ArrayList<>();

		hibernateGroupRegexes.add(Arrays.asList("public class ", "try \\{", "for ?\\(", "if ?\\(", "public static ",
				"else if ?\\(", "while ?\\(", "return [a-zA-z]+\\;", "public void ", "\\@Entity", "\\@Table\\(",
				"\\@Column\\(", "\\@Id", "\\@Override", "@Field\\(", "@Lob", "@MappedSuperclass", "@ManyToOne",
				"@Cache", "@NotNull", "@DefaultBooleanValue\\(", "\\@OneToMany\\(", "CREATE TABLE ", "\\@Embeddable",
				"\\@Test", " \\= new [a-zA-Z0-9]+ ?\\(", "\\{code\\:java\\}", "\\{code\\}", "\\* \\(non-Javadoc\\)",
				"\\*\\/"));
		hibernateGroupRegexes.add(Arrays.asList("at ([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+",
				"\\([a-zA-Z0-9]+\\.java:\\d+\\)", "Caused by\\: ([a-zA-Z0-9]+[\\.\\$])+[a-zA-Z0-9]+"));
		hibernateGroupRegexes.add(Arrays.asList("INFO \\[", "WARN \\[", "ERROR \\["));
		hibernateGroupRegexes.add(Arrays.asList("\\<persistence-unit", "\\<provider", "\\<\\/persistence-unit",
				"\\<property", "\\<column"));
		hibernateGroupRegexes.add(Arrays.asList("\\<ehcache", "\\<defaultCache", "\\<defaultCache"));
		hibernateGroupRegexes
				.add(Arrays.asList("\\<hibernate-mapping", "\\<\\/hibernate-mapping", "\\<composite-id", "\\<class"));
		hibernateGroupRegexes
				.add(Arrays.asList("SELECT ", "INNER JOIN", "WHERE ", "select ", "as col_", "inner join", "where "));
		hibernateGroupRegexes.add(Arrays.asList("openSession\\(", "beginTransaction\\(", "commit\\(\\)\\;"));

		systemGroupRegexes.put("hibernate", hibernateGroupRegexes);

	}

	public static void main(String[] args) throws Exception {

		HashMap<TextInstance, Labels> goldSet = DataReader.readGoldSet(goldSetFile);

		Set<TextInstance> bugInstances = goldSet.keySet();

		for (TextInstance bugInstance : bugInstances) {

			String project = bugInstance.getProject();
			if (!allowedSystems.contains(project)) {
				continue;
			}

			try {

				System.out.print("Processing " + bugInstance + " ");

				BugReport bugReport = POSBugProcessorMain.readBug(xmlBugDir, bugInstance);
				BugReport bugPreprocessed = new BugReport(bugReport);
				boolean changed = preprocessBug(bugInstance, bugPreprocessed);

				POSBugProcessorMain.writeBug(bugPreprocessed, outputFolder, bugInstance);

				if (changed) {
					System.out.println(" [changed]");
				} else {
					System.out.println(" [not changed]");
				}
			} catch (Exception e) {
				System.err.println("Error for " + bugInstance);
				e.printStackTrace();
			}

		}

	}

	private static boolean preprocessBug(TextInstance bugInstance, BugReport bugReport) throws Exception {

		List<String> iniSeparators = systemIniSeparators.get(bugInstance.getProject());

		boolean changed = false;

		if (iniSeparators != null) {

			for (String iniSeparator : iniSeparators) {
				List<CodePair> pairs = findCodingPairs(bugReport, iniSeparator, bugInstance);
				boolean changed2 = removeCode(bugReport, pairs, iniSeparator);
				changed = changed || changed2;

			}
		}

		boolean removeOtherSentences = removeOtherSentences(bugReport, bugInstance);
		changed = changed || removeOtherSentences;

		return changed;

	}

	private static boolean removeOtherSentences(BugReport bugReport, TextInstance bugInstance) {
		boolean changed = false;

		List<String> regexes = systemRegexes.get(bugInstance.getProject());
		List<String> regexesStart = systemStartRegexes.get(bugInstance.getProject());
		List<String> regexesEnd = systemEndRegexes.get(bugInstance.getProject());

		if (bugReport.getDescription() == null) {
			return false;
		}

		List<DescriptionParagraph> paragraphsToRemove = new ArrayList<>();
		List<DescriptionParagraph> paragraphs = bugReport.getDescription().getParagraphs();
		for (DescriptionParagraph par : paragraphs) {

			if (par == null || par.getSentences() == null) {
				paragraphsToRemove.add(par);
				continue;
			}

			if (checkParagraph2(par, bugInstance)) {
				paragraphsToRemove.add(par);
			} else {

				List<DescriptionSentence> sentencesToRemove = new ArrayList<>();
				List<DescriptionSentence> sentences = par.getSentences();
				for (DescriptionSentence sent : sentences) {
					if (checkSentence2(sent, regexes) || checkSentence3(sent, regexesStart)
							|| checkSentence4(sent, regexesEnd)) {
						sentencesToRemove.add(sent);
					}
				}

				boolean removeAll = sentences.removeAll(sentencesToRemove);
				changed = changed || removeAll;

				if (sentences.isEmpty()) {
					paragraphsToRemove.add(par);
				}
			}
		}

		boolean removeAll = paragraphs.removeAll(paragraphsToRemove);
		changed = changed || removeAll;

		// -----------------------------

		if (paragraphs.isEmpty()) {
			bugReport.setDescription(null);
		}

		return changed;
	}

	private static boolean checkSentence4(DescriptionSentence sent, List<String> regexesEnd) {

		if (regexesEnd == null) {
			return false;
		}

		String value = sent.getValue();

		boolean anyMatch = regexesEnd.stream().anyMatch(regex -> value.matches("(?s).*" + regex + "$"));

		return anyMatch;
	}

	private static boolean checkSentence3(DescriptionSentence sent, List<String> regexesStart2) {

		if (regexesStart2 == null) {
			return false;
		}

		String value = sent.getValue();

		boolean anyMatch = regexesStart2.stream().anyMatch(regex -> value.matches("(?s)^" + regex + ".*"));

		return anyMatch;
	}

	private static boolean checkParagraph2(DescriptionParagraph par, TextInstance bugInstance) {

		List<List<String>> groupRegexes = systemGroupRegexes.get(bugInstance.getProject());

		if (groupRegexes == null) {
			return false;
		}

		for (List<String> regxs : groupRegexes) {
			int numRegx = 0;
			for (String reg : regxs) {
				boolean matches = checkRegex(Arrays.asList(reg), par.getSentences());
				if (matches) {
					numRegx++;
					if (numRegx > 1) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean checkRegex(List<String> regxs, List<DescriptionSentence> sentences) {
		for (DescriptionSentence sent : sentences) {
			if (checkSentence2(sent, regxs)) {
				return true;
			}
		}
		return false;
	}

	private static boolean checkSentence2(DescriptionSentence sent, List<String> regexesLocal) {

		if (regexesLocal == null) {
			return false;
		}

		String value = sent.getValue();

		boolean anyMatch = regexesLocal.stream().anyMatch(regex -> value.matches("(?s).*" + regex + ".*"));

		return anyMatch;
	}

	private static boolean removeCode(BugReport bugReport, List<CodePair> pairs, String separator) {

		boolean changed = false;

		List<DescriptionParagraph> paragraphsToRemove = new ArrayList<>();
		List<DescriptionParagraph> paragraphs = bugReport.getDescription().getParagraphs();
		for (DescriptionParagraph par : paragraphs) {

			if (par == null || par.getSentences() == null) {
				paragraphsToRemove.add(par);
				continue;
			}

			if (checkParagraph(par, pairs)) {
				paragraphsToRemove.add(par);
			} else {

				List<DescriptionSentence> sentencesToRemove = new ArrayList<>();
				List<DescriptionSentence> sentences = par.getSentences();
				for (DescriptionSentence sent : sentences) {
					CodePair pair = checkSentence(sent, pairs);

					if (pair == null) {
						continue;
					}

					if (!pair.regex) {
						sentencesToRemove.add(sent);
					} else {
						int indexOf = sent.getValue().indexOf(separator);
						String newVal = sent.getValue().substring(0, indexOf);
						if (newVal.trim().isEmpty()) {
							sentencesToRemove.add(sent);
						} else {
							sent.setValue(newVal);
							changed = true;
						}
					}
				}

				boolean removeAll = sentences.removeAll(sentencesToRemove);
				changed = changed || removeAll;

				if (sentences.isEmpty()) {
					paragraphsToRemove.add(par);
				}
			}
		}

		boolean removeAll = paragraphs.removeAll(paragraphsToRemove);
		changed = changed || removeAll;

		// -----------------------------

		if (paragraphs.isEmpty()) {
			bugReport.setDescription(null);
		}

		return changed;

	}

	private static CodePair checkSentence(DescriptionSentence sent, List<CodePair> pairs) {
		Integer parId = Integer.valueOf(sent.getId().split("\\.")[0]);
		Integer sentId = Integer.valueOf(sent.getId().split("\\.")[1]);

		for (CodePair pair : pairs) {

			Integer parIniId = Integer.valueOf(pair.iniId.split("\\.")[0]);
			Integer parEndId = Integer.valueOf(pair.endId.split("\\.")[0]);

			if (parIniId < parId && parId < parEndId) {
				return pair;
			} else {
				Integer sentIniId = Integer.valueOf(pair.iniId.split("\\.")[1]);
				Integer sentEndId = Integer.valueOf(pair.endId.split("\\.")[1]);
				if (parIniId == parId && parIniId == parEndId) {
					if (sentIniId <= sentId && sentId <= sentEndId) {
						return pair;
					}
				} else {
					if (parIniId == parId) {
						if (sentIniId <= sentId) {
							return pair;
						}
					} else if (parEndId == parId) {
						if (sentId <= sentEndId) {
							return pair;
						}
					}
				}
			}

		}

		return null;
	}

	private static boolean checkParagraph(DescriptionParagraph par, List<CodePair> pairs) {
		Integer parId = Integer.valueOf(par.getId());

		for (CodePair pair : pairs) {
			Integer iniId = Integer.valueOf(pair.iniId.split("\\.")[0]);
			Integer endId = Integer.valueOf(pair.endId.split("\\.")[0]);

			if (iniId > endId) {
				throw new RuntimeException("Incorrect pair: " + pair);
			}

			if (iniId < parId && parId < endId) {
				return true;
			} else {
				List<DescriptionSentence> sentences = par.getSentences();
				if (sentences.get(0).getId().equals(iniId)
						&& sentences.get(sentences.size() - 1).getId().equals(iniId)) {
					return true;
				}
			}
		}

		return false;
	}

	private static List<CodePair> findCodingPairs(BugReport bugReport, String separator, TextInstance bugInstance) {

		List<DescriptionSentence> sentences = bugReport.getDescription().getAllSentences();
		List<CodePair> pairs = new ArrayList<>();

		// code to remove the docker template at the beginning of the bug
		if (bugInstance.getProject().equals("docker") && sentences.size() > 10) {
			DescriptionSentence firstSent = sentences.get(0);
			if (firstSent.getValue().equals("<!")) {

				for (int i = 1; i < sentences.size(); i++) {
					DescriptionSentence sent = sentences.get(i);
					if (sent.getValue().equals("BUG REPORT INFORMATION")) {
						if (i + 4 <= sentences.size() - 1) {
							if (sentences.get(i + 4).getValue().equals("-->")) {
								pairs.add(new CodePair(firstSent.getId(), sentences.get(i + 4).getId(), false));
								break;
							}
						}
					}
				}

			}

		}

		// -------------------------------

		String iniId = null;
		String endId = null;
		for (DescriptionSentence sent : sentences) {

			if (sent.getValue().equals(separator)) {

				if (iniId == null) {
					iniId = sent.getId();
				} else {
					endId = sent.getId();
					pairs.add(new CodePair(iniId, endId, false));
					iniId = null;
					endId = null;
				}
			} else {
				String separator2 = separator.replace("{", "\\{").replace("}", "\\}");
				if (sent.getValue().matches("(?s).+" + separator2 + ".+" + separator2)) {
					if (iniId == null) {
						pairs.add(new CodePair(sent.getId(), sent.getId(), true));
						iniId = null;
						endId = null;
					}
				} else if (sent.getValue().startsWith(separator) && sent.getValue().endsWith(separator)) {
					pairs.add(new CodePair(sent.getId(), sent.getId(), false));
					iniId = null;
					endId = null;
				} else if (sent.getValue().startsWith(separator)) {
					if (iniId == null) {
						iniId = sent.getId();
					}
				} else if (sent.getValue().endsWith(separator)) {
					if (iniId != null) {
						endId = sent.getId();
						pairs.add(new CodePair(iniId, endId, false));
						iniId = null;
						endId = null;
					}
				}
			}
		}

		if (iniId != null || endId != null) {
			System.err.println("Could not find pair: " + iniId + ", " + endId);
		}

		return pairs;
	}

	public static class CodePair {
		public String iniId;
		public String endId;

		boolean regex = false;

		public CodePair(String iniId, String endId, boolean regex) {
			super();
			this.iniId = iniId;
			this.endId = endId;
			this.regex = regex;
		}

		@Override
		public String toString() {
			return "[i=" + iniId + ", e=" + endId + "]";
		}

	}

}
