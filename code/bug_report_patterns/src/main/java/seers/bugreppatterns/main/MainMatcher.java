package seers.bugreppatterns.main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.bugreppatterns.matcher.SentenceMatcherProcessor;

public class MainMatcher {

	static String fileAssignment = "test_data\\matcher\\sentences_coding.csv";
	public static CsvWriter conflictsWriter;

	public static CsvWriter matchedWriterSentences;
	public static CsvWriter matchedWriterParagraphs;

	public static CsvWriter goldSetWriterSentences;
	public static CsvWriter goldSetWriterParagraphs;
	public static CsvWriter goldSetWriterDocuments;

	public static ConcurrentHashMap<GoldSetParagraph, GoldSetClasses> goldSetParagraphs = new ConcurrentHashMap<>(
			new LinkedHashMap<>());
	public static ConcurrentHashMap<GoldSetDoc, GoldSetClasses> goldSetDocs = new ConcurrentHashMap<>(
			new LinkedHashMap<>());

	public static void main(String[] args) throws Exception {
		List<List<String>> codedData = readData();

		conflictsWriter = new CsvWriterBuilder(new FileWriter("conflicts-coded-parsed.csv")).separator(';').build();

		matchedWriterSentences = new CsvWriterBuilder(new FileWriter("matched-coded-parsed-S.csv")).separator(';')
				.build();
		matchedWriterParagraphs = new CsvWriterBuilder(new FileWriter("matched-coded-parsed-P.csv")).separator(';')
				.build();

		goldSetWriterSentences = new CsvWriterBuilder(new FileWriter("gold-set-S.csv")).separator(';').build();
		goldSetWriterParagraphs = new CsvWriterBuilder(new FileWriter("gold-set-P.csv")).separator(';').build();
		goldSetWriterDocuments = new CsvWriterBuilder(new FileWriter("gold-set-B.csv")).separator(';').build();

		try {

			generateTitles();

			Class<? extends ThreadProcessor> class1 = SentenceMatcherProcessor.class;
			ThreadParameters params = new ThreadParameters();
			ThreadExecutor.executePaginated(codedData.subList(1, codedData.size()), class1, params, 5);

			generateGoldSets();
		} finally {
			conflictsWriter.close();
			matchedWriterSentences.close();
			matchedWriterParagraphs.close();
			goldSetWriterSentences.close();
			goldSetWriterParagraphs.close();
			goldSetWriterDocuments.close();
		}

	}

	private static void generateTitles() {
		conflictsWriter.writeNext(
				Arrays.asList(new String[] { "text_type", "system", "bug_id", "sentence_id", "type_of_problem" }));

		matchedWriterSentences.writeNext(Arrays.asList(new String[] { "system", "bug_id", "sentence_id",
				"parsed_paragraph_id", "parsed_sentence_id", "is_ob", "is_eb", "is_sr" }));
		matchedWriterParagraphs.writeNext(Arrays.asList(
				new String[] { "system", "bug_id", "sentence_id", "parsed_paragraph_id", "is_ob", "is_eb", "is_sr" }));

		String[] title = new String[] { "system", "bug_id", "instance_id", "is_ob", "is_eb", "is_sr" };
		goldSetWriterSentences.writeNext(Arrays.asList(title));
		goldSetWriterParagraphs.writeNext(Arrays.asList(title));
		goldSetWriterDocuments.writeNext(Arrays.asList(title));

	}

	private static void generateGoldSets() {
		Set<Entry<GoldSetParagraph, GoldSetClasses>> entrySet = goldSetParagraphs.entrySet();
		for (Entry<GoldSetParagraph, GoldSetClasses> entry : entrySet) {

			List<String> nextLine = new ArrayList<>(entry.getKey().toList());
			nextLine.addAll(entry.getValue().toList());
			goldSetWriterParagraphs.writeNext(nextLine);
		}

		Set<Entry<GoldSetDoc, GoldSetClasses>> entrySet2 = goldSetDocs.entrySet();
		for (Entry<GoldSetDoc, GoldSetClasses> entry : entrySet2) {

			List<String> nextLine = new ArrayList<>(entry.getKey().toList());
			nextLine.addAll(entry.getValue().toList());
			goldSetWriterDocuments.writeNext(nextLine);
		}
	}

	private static List<List<String>> readData() throws IOException {
		CsvParser csvParser = new CsvParserBuilder().separator(';').multiLine(true).build();
		try (CsvReader csvReader = new CsvReader(new FileReader(fileAssignment), csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			return allLines;

		}
	}

	public static class GoldSetDoc {
		public String system;
		public String bugId;

		public GoldSetDoc(String system, String bugId) {
			super();
			this.system = system;
			this.bugId = bugId;
		}

		public List<String> toList() {
			return Arrays.asList(new String[] { system, bugId, "0" });
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((bugId == null) ? 0 : bugId.hashCode());
			result = prime * result + ((system == null) ? 0 : system.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GoldSetDoc other = (GoldSetDoc) obj;
			if (bugId == null) {
				if (other.bugId != null)
					return false;
			} else if (!bugId.equals(other.bugId))
				return false;
			if (system == null) {
				if (other.system != null)
					return false;
			} else if (!system.equals(other.system))
				return false;
			return true;
		}

	}

	public static class GoldSetParagraph {
		public String system;
		public String bugId;
		public String paragraphId;

		public GoldSetParagraph(String system, String bugId, String paragraphId) {
			super();
			this.system = system;
			this.bugId = bugId;
			this.paragraphId = paragraphId;
		}

		public List<String> toList() {
			return Arrays.asList(new String[] { system, bugId, paragraphId });
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((bugId == null) ? 0 : bugId.hashCode());
			result = prime * result + ((paragraphId == null) ? 0 : paragraphId.hashCode());
			result = prime * result + ((system == null) ? 0 : system.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GoldSetParagraph other = (GoldSetParagraph) obj;
			if (bugId == null) {
				if (other.bugId != null)
					return false;
			} else if (!bugId.equals(other.bugId))
				return false;
			if (paragraphId == null) {
				if (other.paragraphId != null)
					return false;
			} else if (!paragraphId.equals(other.paragraphId))
				return false;
			if (system == null) {
				if (other.system != null)
					return false;
			} else if (!system.equals(other.system))
				return false;
			return true;
		}

	}

	public static class GoldSetClasses {
		public String ob = "";
		public String eb = "";
		public String sr = "";

		public List<String> toList() {
			return Arrays.asList(new String[] { ob, eb, sr });
		}
	}
}
