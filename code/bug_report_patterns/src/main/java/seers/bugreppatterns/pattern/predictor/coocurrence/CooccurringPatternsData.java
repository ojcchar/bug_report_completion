package seers.bugreppatterns.pattern.predictor.coocurrence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import seers.bugrepcompl.utils.DataReader;

public class CooccurringPatternsData {

	private static final Logger LOGGER = LoggerFactory.getLogger(CooccurringPatternsData.class);

	protected Set<CooccurringPattern> cooccurringPatternsOB;
	protected Set<CooccurringPattern> cooccurringPatternsEB;
	protected Set<CooccurringPattern> cooccurringPatternsSR;

	public CooccurringPatternsData(String dataFilePath) throws IOException {
		loadCooccurringPatterns(dataFilePath);
	}

	private void loadCooccurringPatterns(String dataFilePath) throws IOException {

		LOGGER.debug("Loading co-occurring patterns: " + dataFilePath);

		List<List<String>> lines = DataReader.readLines(new File(dataFilePath));

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

		// LOGGER.debug(cooccurringPatternsOB.toString());
		// LOGGER.debug(cooccurringPatternsEB.toString());
		// LOGGER.debug(cooccurringPatternsSR.toString());
	}

	private void addIndividualPatterns(Set<String> individualCooccurring, Set<String> individualNonCooccurring,
			Set<CooccurringPattern> cooccurringPatterns) {

		individualNonCooccurring.stream().forEach(pattern -> {
			if (!individualCooccurring.contains(pattern)) {
				CooccurringPattern cooccurPattern = new CooccurringPattern(
						new LinkedHashSet<>(new LinkedHashSet<>(Arrays.asList(new String[] { pattern }))), true);
				cooccurringPatterns.add(cooccurPattern);
			}
		});

	}

	private void addCooccurringPatterns(List<String> patternList, Set<String> individualCooccurring,
			Set<String> individualNonCooccurring, Set<CooccurringPattern> cooccurringPatterns) {

		if (patternList == null || patternList.isEmpty()) {
			return;
		}

		if (patternList.size() == 1) {
			if (!patternList.get(0).isEmpty()) {
				individualNonCooccurring.add(patternList.get(0));
			}
		} else {
			CooccurringPattern cooccurPattern = new CooccurringPattern(new LinkedHashSet<>(patternList), false);
			cooccurringPatterns.add(cooccurPattern);
			
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

}
