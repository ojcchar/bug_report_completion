package seers.bugrepclassifier.svmbased.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import seers.bugrepclassifier.svmbased.svm.util.Common;

public class BugReport {
	public String fileID;
	public String system;
	public String reportID;
	public String content;
	public String xmlLine;
	public String title;
	public String description;
	public boolean exp_behavior;
	public boolean steps_2_repr;
	public ArrayList<String> paragraphs;
	public HashMap<String, ArrayList<String>> paragraphMap;
	public HashMap<String,String> sentenceMap;
	public ArrayList<ParseResult> parseResults;
	public HashMap<String,ParseResult> parseResultsMap;

	public HashMap<Integer, int[]> positionMap;

	
	public BugReport(String fileID,String system){
		System.setProperty("file.encoding", "UTF-8");
		this.paragraphMap = new HashMap<String, ArrayList<String>>();
		this.sentenceMap = new HashMap<String,String>();
		this.fileID = fileID;
		this.system = system;
		this.xmlLine = Common.getLine(fileID);
		this.title = "";
		this.reportID="";
		this.description = "";
		this.readSource();
		this.preprocessing();
	}
	
	public BugReport(String bugDescription) {
		this.paragraphMap = new HashMap<String, ArrayList<String>>();
		this.sentenceMap = new HashMap<String,String>();
		this.description = bugDescription;
		this.readSource2();
		this.preprocessing();
	}

	private void readSource2() {
		this.paragraphs = new ArrayList<String>();
		String[] temp = this.description.split("\n\n");
		for(int i = 0;i<temp.length;i++){
			String s = temp[i];
			if(s.isEmpty()){
				continue;
			}
			paragraphs.add(s);
		}
	}

	public void readSource() {
		this.paragraphs = new ArrayList<String>();
		this.xmlLine = this.xmlLine.replace("<description>", "<description><![CDATA[").replace("</description>", "]]></description>")
				.replace("<title>", "<title><![CDATA[").replace("</title>", "]]></title>");
		InputStream inputStream;
		try {
			inputStream = new ByteArrayInputStream(this.xmlLine.getBytes());

			SAXParserFactory sf = SAXParserFactory.newInstance();
			SAXParser sp = sf.newSAXParser();
			BugReportReader reader = new BugReportReader();
			sp.parse(new InputSource(inputStream), reader);
			this.reportID += reader.getID();
			this.description += reader.getDescription().replaceAll("````", "\"\"").replaceAll("```", "\"'").
					replaceAll("``","\"").replaceAll("`","'").replaceAll("’", "'")
					.replaceAll("”", "\"").replaceAll("“", "\"").replaceAll("—", "--");
			this.title += reader.getTitle();
			
			//System.out.println(description);
			String[] temp = this.description.split("\n\n");
			for(int i = 0;i<temp.length;i++){
				String s = temp[i];
				if(s.isEmpty()){
					//Common.pause("empty paragraph");
					continue;
				}
				paragraphs.add(s);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void preprocessing(){

		this.parseResults = new ArrayList<ParseResult>();
		
		Properties props = new Properties();
	    props.setProperty("annotators", "tokenize, ssplit");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    
	    for(int pid = 0;pid<this.paragraphs.size();pid++){
	    	//System.out.println(i+" "+report.paragraphs.get(i).length());
	    	
	    	String paragraph = this.paragraphs.get(pid);
	    	String[] ss = paragraph.split("\n");
	    	
	    	//flag used to indicate whether we split paragraph by row, to check if there exists some special case
	    	boolean flag = false;
	    	
	    	if(paragraph.toLowerCase().startsWith("steps to reproduce")){
	    		flag = true;
	    	}else{
	    		for(String s:ss){
	    			//pattern 1.) 1. 1) - 
	    			if(s.matches("(\\W*)\\d((\\.\\))|\\.|\\)|,|-)(.*)")||s.matches("(\\W*)(-|\\*)(.*)")){
	    				flag = true;
	    				break;
	    			}
	    		}
	    	}
	    	
	    	boolean errorflag = false;
	    	for(String s:ss){
    			if(s.matches("(\\W*)(at )(.*)")){
    				errorflag = true;
    				break;
    			}
    		}
	    	
	    	
	    	//System.out.println(flag);
	    	
	    	ArrayList<String> parseSentences = new ArrayList<String>();
	    	//if paragraph contains specical patterns, corenlp will process line by line. 
	    	//Otherwise, process the paragraph directly
	    	if(flag==true){
	    		for(String s:ss){
	    			parseSentences.add(s);
	    		}
	    	}else{
    			parseSentences.add(paragraph);
    		}
	    	
	    	ArrayList<String> splittedSentence = new ArrayList<String>();
	    	
	    	//paragraph contains error code such as:at java.lang.Object.wait(Native Method) will not be processed by corenlp
//	    	if((errorflag==true || report.paragraphs.get(pid).length()>700) && flag==false){
//	    		for(String s:ss){
//	    			splittedSentence.add(s);
//	    		}
//	    		this.paragraphMap.put(String.valueOf(pid),splittedSentence);
//	    		//splitfile.add(splittedSentence);
//	    		//Common.pause(" ");
//	    		continue;
//	    	}
	    	
	    	
	    	for(int id = 0;id<parseSentences.size();id++){
	    		String parseSentence = parseSentences.get(id);
//	    		if(parseSentence.startsWith("java.")||parseSentence.startsWith("	at ")||parseSentence.startsWith("\"") ||parseSentence.startsWith("org.apache")){//parseSentence.matches("(\\W*)(at )(\\w+\\.){3,}(.*)")){
//	    			System.out.println("NOT processing: "+parseSentence);
//	    			splittedSentence.add(parseSentence);
//	    			if(sid==parseSentences.size()-1){
//	    				//splitfile.add(splittedSentence);
//	    				this.paragraphMap.put(String.valueOf(pid),splittedSentence);
//	    			}
//	    			continue;
//	    		}
	    		
	    		if(parseSentence.matches("(\\W*)\\d((\\.\\))|\\.|\\)|,|-)(.*)")){
	    			parseSentence = parseSentence.replaceFirst("((\\.\\))|\\.|\\)|,|-)\\s*"," ");
	    		//	System.out.println(parseSentence);
	    		}
	    		//System.out.println(parseSentence);
		    	Annotation document = new Annotation(parseSentence);
			    
				pipeline.annotate(document);
				
				List<CoreMap> sentence = document.get(SentencesAnnotation.class);
				
				int sid = 0;
				for(CoreMap s:sentence){
					ParseResult pr = new ParseResult();
					
					String plainSentence = s.toShorterString("Text");
					plainSentence = plainSentence.substring(plainSentence.indexOf("=")+1, plainSentence.length()-1);
					//System.out.println(plainSentence);
					pr.sentence = plainSentence;
					splittedSentence.add(plainSentence);

					List<CoreLabel> tokens= s.get(TokensAnnotation.class);
						
					for(CoreLabel token:tokens){
						//String word = token.word().replace("``", "\"").replace("''", "\"").replace("`", "'").replace("-LRB-", "(").replace(" ", " ")
						//		.replace("-RRB-", ")").replace("-LSB-", "[").replace("-RSB-", "]").replace("-LCB-", "{").replace("-RCB-", "}").replace("...", ". . .");
						//sb.append(word+"/"+token.tag()+" ");
						//System.out.print(token.word()+"/"+token.originalText()+" "+token.tag()+" ");
						pr.words.add(token.word());
					}
					pr.id = this.parseResults.size();
					this.parseResults.add(pr);
					this.sentenceMap.put(pid+"."+sid, plainSentence);
					sid++;
				}
				
				
				if(id==parseSentences.size()-1){
					this.paragraphMap.put(String.valueOf(pid),splittedSentence);
    			}
	    	}
	    }
	}
	
	public static void main(String args[]) {
		File folder = new File("/users/ljwinnie/Desktop/bug_report/data/eclipse");
		String[] filelist = folder.list();
		for(int i = 0;i<2;i++){
			String file = "/users/ljwinnie/Desktop/bug_report/data/eclipse/"+filelist[i];
			System.out.println(file);
			BugReport report = new BugReport(file,"eclipse");
		}
	}
}
