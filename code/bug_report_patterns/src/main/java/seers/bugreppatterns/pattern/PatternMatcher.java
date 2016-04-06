package seers.bugreppatterns.pattern;

public abstract class PatternMatcher {

	public static final String EB = "EB";
	public static final String SR = "SR";

	public abstract boolean matchSentence(String text) throws Exception;

	public abstract boolean matchParagraph(String text) throws Exception;

	public abstract boolean matchDocument(String text) throws Exception;

	public abstract String getType();

}
