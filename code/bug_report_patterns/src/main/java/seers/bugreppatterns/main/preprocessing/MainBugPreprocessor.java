package seers.bugreppatterns.main.preprocessing;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.utils.DataReader;
import seers.textanalyzer.TextProcessor;

public class MainBugPreprocessor {

	public static void main(String[] args) throws Exception {

		String inputFolder = "C:/Users/ojcch/Documents/Dropbox/Research/BUG_REPORT_PROJECT_JING/Data/final_data/fixed_parsed_data";
		String goldSetPath = "C:/Users/ojcch/Documents/Dropbox/Research/BUG_REPORT_PROJECT_JING/Data/final_data/gold_sets4/gold-set-B-all_data.csv";
//		String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/data/preprocessed_data/base_preprocessing";
//		String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/data/preprocessed_data/lemmatization";
//		String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/data/preprocessed_data/stopwords";
		String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/data/preprocessed_data/stopwords_lemmatization";
		boolean lemmatization = true;
		boolean stopwordRemoval = true;
		String stopWordsPath = "stopwords.txt";
		
		

		// ----------------------------------------

		Set<TextInstance> bugs = DataReader.readGoldSet(goldSetPath).keySet();
		List<String> stopWords = null;
		if (stopwordRemoval) {
			stopWords = TextProcessor.readStopWords(stopWordsPath);
		}

		// ----------------------------------------

		ThreadParameters params = new ThreadParameters();
		params.addParam("inputFolder", inputFolder);
		params.addParam("outputFolder", outputFolder);
		params.addParam("lemmatization", lemmatization);
		params.addParam("stopwordRemoval", stopwordRemoval);
		params.addParam("stopWords", stopWords);

		ThreadExecutor.executePaginated(new ArrayList<>(bugs), BugPreprocessor.class, params, 20, 50);

		// ----------------------------------------

	}

}
