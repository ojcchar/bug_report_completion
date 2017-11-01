package seers.bugrepclassifier.svmbased.svm;

import java.util.ArrayList;

import seers.bugrepclassifier.svmbased.model.SVMBugReport;
import seers.bugrepclassifier.svmbased.svm.util.Common;

public class PrepareTestBugReport {
	public static void main(String[] args){
		String rawfilepath = args[0];
		String featurepath = args[1];
		String svmfilepath = args[2];
		
//		boolean patflag = false;
//		if(args[2].equals("true")){
//			patflag = true;
//		}
//				
//		boolean ngramflag = false;
//		if(args[4].equals("true")){
//			ngramflag = true;
//		}
		
		SVMBugReport report = new SVMBugReport(rawfilepath,"");
		
		BugReportFea fea = new BugReportFea(false,featurepath);
		
		fea.configure(report);
		String feaStr = fea.getSVMFormatString();
		
		ArrayList<String> testline = new ArrayList<String>();
		testline.add("+1 "+feaStr);
		
		Common.outputLines(testline, svmfilepath,false);
	}
}
