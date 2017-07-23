package seers.bugreppatterns.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class JavaUtils {

	public static Set<String> getSet(String... terms) {
		return new LinkedHashSet<String>(Arrays.asList(terms));
	}

	public static HashSet<ImmutablePair<String, String>> getPairSet(String... terms) {
		HashSet<ImmutablePair<String, String>> set = new HashSet<>();

		for (int i = 0; i < terms.length; i++) {

			String term = terms[i];
			if (term.length() < 2) {
				continue;
			}

			String t1 = String.valueOf(term.charAt(0));
			String t2 = String.valueOf(term.charAt(1));

			set.add(new ImmutablePair<>(t1, t2));
		}

		return set;
	}

}
