1. To check for inconsistencies in the coded data:
	1.1 Download the coded data as a CSV file
	1.2 Copy/move the coded data file (CSV) in the /bug_report_patterns/test_data/matcher folder
	1.3 Copy/move the parsed files in the /bug_report_patterns/test_data/data folder
	1.4 Modify the path of the CSV file in seers.bugreppatterns.main.MainMatcher
	1.5 Run seers.bugreppatterns.main.MainMatcher
	1.6 Check the /bug_report_patterns/test_data/matcher/conflicts-coded-parsed.csv file

2. To generate gold sets:
	2.1 Run seers.bugreppatterns.main.GoldSetGenerator with the arguments:
			- path of the CSV file with the coded data
			- folder where the output files will be stored
			- path of the goldset of Davis' data ('all_data_only_bugs_davies.csv')
	
3. To generate the features
	3.1 Update and run the MainHRClassifierRunner class to generate the output-pre-features
	3.1 Run MainFeatures class

Files required to run the ML predictor:
1. The gold-sets
	gold-set-B.csv
	gold-set-P.csv
	gold-set-S.csv
2. The features
3. The patterns definitions: patterns-prediction.csv (most precise implementations-let's try with all of them first)

	