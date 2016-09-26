package seers.bugreppatterns.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JavaUtils {

	public static Set<String> getSet(String... terms) {
		return new HashSet<String>(Arrays.asList(terms));
	}

}
