package seers.bugreppatterns.main.coocurrence;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import seers.bugrepcompl.entity.PatternEntry;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugreppatterns.pattern.predictor.coocurrence.CooccurringPattern;

public class MainSequencesOfPatterns {

	private static final int SEQUENCE_SIZE = 5;

	public static void main(String[] args) throws Exception {

		String dataPath = "test_data" + File.separator + "pattern-data-S.csv";

		// read data
		List<PatternEntry> data = readData(dataPath);

		// sort the data
		// ---------------------

		Comparator<PatternEntry> comparator = (p1, p2) -> p1.instance.getProject().compareTo(p2.instance.getProject());
		comparator = comparator.thenComparing((p1, p2) -> p1.instance.getBugId().compareTo(p2.instance.getBugId()));
		comparator = comparator.thenComparing((p1, p2) -> {
			Integer inst1 = Integer.valueOf(p1.instance.getInstanceId().split("\\.")[0]);
			Integer inst2 = Integer.valueOf(p2.instance.getInstanceId().split("\\.")[0]);
			return inst1.compareTo(inst2);
		});
		comparator = comparator.thenComparing((p1, p2) -> {
			Integer inst1 = Integer.valueOf(p1.instance.getInstanceId().split("\\.")[1]);
			Integer inst2 = Integer.valueOf(p2.instance.getInstanceId().split("\\.")[1]);
			return inst1.compareTo(inst2);
		});

		data.sort(comparator);

		// ---------------------

		// data = data.stream().filter(p ->
		// !p.infoType.equals("OB")).collect(Collectors.toList());

		// ---------------------

		// extract all the bugs
		HashMap<TextInstance, List<PatternEntry>> indexedBugs = indexBugs(data);

		String infoType = "EB";
		HashMap<CooccurringPattern, List<TextInstance>> cooccurringPatterns = extractSequencesForInfoType(indexedBugs,
				infoType);
		printPatterns(infoType, cooccurringPatterns);

		infoType = "SR";
		cooccurringPatterns = extractSequencesForInfoType(indexedBugs, infoType);
		printPatterns(infoType, cooccurringPatterns);

//		infoType = "OB";
//		cooccurringPatterns = extractSequencesForInfoType(indexedBugs, infoType);
//		printPatterns(infoType, cooccurringPatterns);

		// ---------------------

	}

	private static void printPatterns(String infoType,
			HashMap<CooccurringPattern, List<TextInstance>> cooccurringPatterns) {
		for (Entry<CooccurringPattern, List<TextInstance>> patt : cooccurringPatterns.entrySet()) {
			System.out.print(infoType);
			System.out.print(";");
			System.out.print(patt.getKey().getName());
			System.out.print(";");
			// System.out.print(patt.getValue());
			// System.out.print(";");
			System.out.println(patt.getValue().size());
		}
	}

	private static HashMap<CooccurringPattern, List<TextInstance>> extractSequencesForInfoType(
			HashMap<TextInstance, List<PatternEntry>> indexedBugs, String infoType) {

		HashMap<CooccurringPattern, List<TextInstance>> cooccurringPatterns = new LinkedHashMap<>();
		// for each bug
		for (Entry<TextInstance, List<PatternEntry>> entry : indexedBugs.entrySet()) {

			// get the sentences for the bug
			List<PatternEntry> sentences = entry.getValue();

			// extract sequences of patterns
			List<CooccurringPattern> sequences = extractSequences(sentences, infoType);
			addPatterns(cooccurringPatterns, sequences, entry.getKey());
		}
		return cooccurringPatterns;
	}

	private static void addPatterns(HashMap<CooccurringPattern, List<TextInstance>> cooccurringPatterns,
			List<CooccurringPattern> sequences, TextInstance textInstance) {
		for (CooccurringPattern seq : sequences) {
			List<TextInstance> list = cooccurringPatterns.get(seq);
			if (list == null) {
				list = new ArrayList<>();
				cooccurringPatterns.put(seq, list);
			}
			list.add(textInstance);
		}
	}

	private static List<CooccurringPattern> extractSequences(List<PatternEntry> sentences, String infoType) {

		List<CooccurringPattern> cooccurrPatterns = new ArrayList<>();

		// for each sentence
		for (int i = 0; i < sentences.size(); i++) {

			PatternEntry sentence = sentences.get(i);

			if (sentence.infoType.equals(infoType)) {

				// get the next x consecutive patterns
				List<PatternEntry> consecutiveSetnces = findConsecutiveSentences(sentences, sentence, i + 1, infoType);

				if (consecutiveSetnces != null) {

					LinkedHashSet<String> cooccurringPatterns = new LinkedHashSet<>();
					for (PatternEntry patternEntry : consecutiveSetnces) {
						cooccurringPatterns.add(patternEntry.pattern);
					}
					if (cooccurringPatterns.size() > 1) {
						cooccurrPatterns.add(new CooccurringPattern(cooccurringPatterns, false));
					}
				}
			}
		}

		return cooccurrPatterns;
	}

	private static List<PatternEntry> findConsecutiveSentences(List<PatternEntry> sentences, PatternEntry firstSentence,
			int i, String infoType) {

		List<PatternEntry> seqSentences = new ArrayList<>();
		seqSentences.add(firstSentence);

		PatternEntry previousSentence = firstSentence;
		InstanceId previousSentenceId = getInstanceId(firstSentence.instance.getInstanceId(), firstSentence.order);

		for (int j = i; j < sentences.size() && seqSentences.size() < SEQUENCE_SIZE; j++) {

			PatternEntry sentence = sentences.get(j);
			InstanceId thisSentenceId = getInstanceId(sentence.instance.getInstanceId(), sentence.order);

			try {
				// if EB and the sentence is next to the previous one
				if (sentence.infoType.equals(infoType) && previousSentenceId.isNext(thisSentenceId)) {
					previousSentenceId = thisSentenceId;
					previousSentence = sentence;
					seqSentences.add(sentence);
				}
			} catch (Exception e) {
				System.err.println("Order problem between " + previousSentence.instance + " and " + sentence.instance);
			}

		}

		if (seqSentences.size() == 1) {
			return null;
		}

		return seqSentences;
	}

	private static InstanceId getInstanceId(String instanceId, int order) {
		String[] split = instanceId.split("\\.");
		return new InstanceId(Integer.valueOf(split[0]), Integer.valueOf(split[1]), order);
	}

	private static HashMap<TextInstance, List<PatternEntry>> indexBugs(List<PatternEntry> data) {

		HashMap<TextInstance, List<PatternEntry>> index = new HashMap<>();
		for (PatternEntry patternEntry : data) {

			String project = patternEntry.instance.getProject();
			String bugId = patternEntry.instance.getBugId();

			TextInstance bugInstance = new TextInstance(project, bugId, "0");
			List<PatternEntry> sentences = index.get(bugInstance);
			if (sentences == null) {
				sentences = new ArrayList<>();
				index.put(bugInstance, sentences);
			}

			sentences.add(patternEntry);
		}

		return index;
	}

	private static List<PatternEntry> readData(String dataPath) throws IOException {

		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(dataPath), "Cp1252"),
				csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			List<PatternEntry> data = new LinkedList<>();

			allLines.subList(1, allLines.size()).forEach(line -> {

				String project = line.get(0);
				String bugId = line.get(1);
				String instanceId = line.get(2);
				TextInstance instance = new TextInstance(project, bugId, instanceId);
				String pattern = line.get(3);
				String infoType = line.get(4);
				String patternType = line.get(5);
				String bugType = line.get(6);
				int order = Integer.valueOf(line.get(7));

				data.add(new PatternEntry(instance, pattern, infoType, patternType, bugType, order));
			});

			return data;
		}
	}

	public static class InstanceId {

		final int parId;
		final int sentId;
		final int order;

		public InstanceId(int parId, int sentId, int order) {
			super();
			this.parId = parId;
			this.sentId = sentId;
			this.order = order;
		}

		public boolean isNext(InstanceId otherInstId) {
			if (parId == otherInstId.parId && sentId + 1 == otherInstId.sentId) {
				if (order + 1 != otherInstId.order) {
					throw new RuntimeException("The orders do not match with the sentence ids");
				}
				return true;
			} else if (parId + 1 == otherInstId.parId && otherInstId.sentId == 1 && order + 1 == otherInstId.order) {
				return true;
			}
			return false;
		}

	}

}
