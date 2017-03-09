package seers.bugrepclassifier.svmbased.svm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import seers.bugrepclassifier.svmbased.model.BugReport;
import seers.bugrepclassifier.svmbased.model.ParseResult;
import seers.bugrepclassifier.svmbased.svm.util.Common;

public class BugReportFea {
	BugReport report;
	public boolean train;
	public HashMap<String, Integer> strFeaMap;
	String name;
	int categoryFeaSize = -1;
	int strFeaSize = -1;
	public static int strFeaFrom = 15000;
	
	public BugReportFea(boolean train, String name) {
		this.train = train;
		this.name = name;
		if (train) {
			this.strFeaMap = new HashMap<String, Integer>();
		} else {
			this.strFeaMap = Common.readFile2Map(name);
		}
	}
	
	public void configure(BugReport report){
		this.report = report;
	}
	
	public void freeze() {
		if (!train) {
			System.err.println("Should not be called when testing");
		}
		Common.outputHashMap(this.strFeaMap, name);
	}
	
	public String getSVMFormatString(){
		StringBuilder sb = new StringBuilder();

		String patternFea = getPatternFeature();
		if(patternFea!=null){
			sb.append(patternFea);
		}
		
		ArrayList<String> ngramFeas = this.getNgramFeatures();
		
		strFeaSize = ngramFeas.size();

		HashSet<Integer> strIndexSet = new HashSet<Integer>();
		for (int i = 0; i < ngramFeas.size(); i++) {
			String str = ngramFeas.get(i);
			int idx = this.getStrIdx(str);
			if (idx != -1) {
				strIndexSet.add(idx);
			}
		}
		ArrayList<Integer> sortedIndexes = new ArrayList<Integer>(strIndexSet);
		Collections.sort(sortedIndexes);

		for (Integer idx : sortedIndexes) {		
			sb.append(" ").append(idx + this.strFeaFrom).append(":1");
		}
		
		if(patternFea==null || patternFea.replaceAll(" ","").equals("")){
			String temp = sb.toString();
			return temp.substring(temp.indexOf(" ")+1);
		}else{
			return sb.toString();
		}
	}

	public String getPatternFeature(){
		return null;
	}
	
	public ArrayList<String> getNgramFeatures() {
		
		ArrayList<String> feas = new ArrayList<String>();
		
		
		for(ParseResult pr:this.report.parseResults){

			ArrayList<String> tokens = pr.words;
			for(int i = 0;i<tokens.size();i++){
				//if(flag==true && matchLabel.contains(tokens.get(i).toLowerCase())){
					//System.out.println(tokens.get(i));
					//continue;
				//}
				
				feas.add(tokens.get(i));
				if(i<tokens.size()-1){
					feas.add(tokens.get(i)+"#"+tokens.get(i+1));
				}
				if(i>0 && i<tokens.size()-1){
					feas.add(tokens.get(i-1)+"#"+tokens.get(i)+"#"+tokens.get(i+1));
				}
			}
		}
			
		return feas;
		
	}
	
	private int getStrIdx(String str) {
		if (strFeaMap.containsKey(str)) {
			return this.strFeaMap.get(str);
		} else {
			if (train) {
				int v = strFeaMap.size();
				strFeaMap.put(str, v);
				return v;
			} else {
				return -1;
			}
		}
	}
}
