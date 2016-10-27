package seers.bugreppatterns.pattern.predictor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import seers.bugrepcompl.utils.DataReader;
import seers.bugreppatterns.pattern.PatternMatcher;

public class CoocurrencePredictor extends LabelPredictor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoocurrencePredictor.class);

	protected static Set<LinkedHashSet<String>> cooccurringPatternsOB;
	protected static Set<LinkedHashSet<String>> cooccurringPatternsEB;
	protected static Set<LinkedHashSet<String>> cooccurringPatternsSR;

	public CoocurrencePredictor(String configFolder) throws IOException {
		loadCooccurringPatterns(configFolder);
	}

	private synchronized void loadCooccurringPatterns(String configFolder) throws IOException {

		// patterns already loaded
		if (cooccurringPatternsOB != null && cooccurringPatternsEB != null && cooccurringPatternsSR != null) {
			return;
		}

		LOGGER.debug("Loading co-occurring patterns");

		List<List<String>> lines = DataReader.readLines(new File(configFolder + File.separator + "coocurrence-B.csv"));

		// ----------------------------------------

		Set<String> individualNonCooccurringOb = new HashSet<>();
		Set<String> individualCooccurringOb = new HashSet<>();

		Set<String> individualNonCooccurringEb = new HashSet<>();
		Set<String> individualCooccurringEb = new HashSet<>();

		Set<String> individualNonCooccurringSr = new HashSet<>();
		Set<String> individualCooccurringSr = new HashSet<>();

		// ----------------------------------------

		cooccurringPatternsOB = new LinkedHashSet<>();
		cooccurringPatternsEB = new LinkedHashSet<>();
		cooccurringPatternsSR = new LinkedHashSet<>();

		// -----------------------------

		for (List<String> line : lines) {
			String obPatterns = line.get(4);
			String ebPatterns = line.get(5);
			String srPatterns = line.get(6);

			// ---------------------
			List<String> obPatternsList = getPatternsList(obPatterns);
			List<String> ebPatternsList = getPatternsList(ebPatterns);
			List<String> srPatternsList = getPatternsList(srPatterns);

			// ----------------------------------------

			addCooccurringPatterns(obPatternsList, individualCooccurringOb, individualNonCooccurringOb,
					cooccurringPatternsOB);
			addCooccurringPatterns(ebPatternsList, individualCooccurringEb, individualNonCooccurringEb,
					cooccurringPatternsEB);
			addCooccurringPatterns(srPatternsList, individualCooccurringSr, individualNonCooccurringSr,
					cooccurringPatternsSR);

		}

		// ----------------------------------------

		addIndividualPatterns(individualCooccurringOb, individualNonCooccurringOb, cooccurringPatternsOB);
		addIndividualPatterns(individualCooccurringEb, individualNonCooccurringEb, cooccurringPatternsEB);
		addIndividualPatterns(individualCooccurringSr, individualNonCooccurringSr, cooccurringPatternsSR);

		LOGGER.debug("Co-occurring patterns: " + cooccurringPatternsOB.size() + " OB, " + cooccurringPatternsEB.size()
				+ " EB, " + cooccurringPatternsSR.size() + " SR");

		LOGGER.debug(cooccurringPatternsOB.toString());
		LOGGER.debug(cooccurringPatternsEB.toString());
		LOGGER.debug(cooccurringPatternsSR.toString());
	}

	private void addIndividualPatterns(Set<String> individualCooccurring, Set<String> individualNonCooccurring,
			Set<LinkedHashSet<String>> cooccurringPatterns) {

		individualNonCooccurring.stream().forEach(pattern -> {
			if (!individualCooccurring.contains(pattern)) {
				cooccurringPatterns.add(new LinkedHashSet<>(Arrays.asList(new String[] { pattern })));
			}
		});

	}

	private void addCooccurringPatterns(List<String> patternList, Set<String> individualCooccurring,
			Set<String> individualNonCooccurring, Set<LinkedHashSet<String>> cooccurringPatterns) {

		if (patternList == null || patternList.isEmpty()) {
			return;
		}

		if (patternList.size() == 1) {
			if (!patternList.get(0).isEmpty()) {
				individualNonCooccurring.add(patternList.get(0));
			}
		} else {
			cooccurringPatterns.add(new LinkedHashSet<>(patternList));
			individualCooccurring.addAll(patternList);
		}

	}

	private List<String> getPatternsList(String patterns) {

		StringBuffer buffer = new StringBuffer(patterns);
		buffer.deleteCharAt(0);
		buffer.deleteCharAt(buffer.length() - 1);

		String[] patternArray = buffer.toString().split(",");

		List<String> patternList = new ArrayList<>();
		for (String pattern : patternArray) {
			if (!pattern.trim().isEmpty()) {
				patternList.add(pattern.trim());
			}
		}
		
		if (patternList.isEmpty()) {
			return null;
		}

		return patternList;
	}

	@Override
	public Labels predictLabels(String bugRepId, String instanceId,
			LinkedHashMap<PatternMatcher, Integer> patternMatches, String granularity) throws Exception {

		if ("B".equals(granularity)) {
			return predictLabelsForBugs(patternMatches);
		}

		return new Labels();
	}

	private Labels predictLabelsForBugs(LinkedHashMap<PatternMatcher, Integer> patternMatches) {
		String isOB = "";
		String isEB = "";
		String isSR = "";

		if (matchesAnyPattern(cooccurringPatternsOB, patternMatches)) {
			isOB = "x";
		}

		if (matchesAnyPattern(cooccurringPatternsEB, patternMatches)) {
			isEB = "x";
		}

		if (matchesAnyPattern(cooccurringPatternsSR, patternMatches)) {
			isSR = "x";
		}

		return new Labels(isOB, isEB, isSR);
	}

	private boolean matchesAnyPattern(Set<LinkedHashSet<String>> cooccurringPatterns,
			LinkedHashMap<PatternMatcher, Integer> patternMatches) {

		LinkedHashSet<String> cooccur = null;

		Set<PatternMatcher> patternMatchesSet = patternMatches.keySet();

		for (LinkedHashSet<String> patterns : cooccurringPatterns) {

			boolean aPatternDoesNotMatch = patterns.stream()
					.anyMatch(pattern -> !containsPattern(pattern, patternMatchesSet));
			boolean allPatternsMatch = !aPatternDoesNotMatch;
			if (allPatternsMatch) {
				cooccur = patterns;
				break;
			}

		}

		// if (cooccur != null) {
		// LOGGER.debug(cooccur.toString());
		// }

		return cooccur != null;
	}

	protected boolean containsPattern(String pattern, Set<PatternMatcher> patternMatches) {
		PatternMatcher patt = PatternMatcher.createFakePattern(pattern);
		return patternMatches.contains(patt);
	}

}
