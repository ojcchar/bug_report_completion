package seers.bugrepclassifier.svmbased.model;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BugReportReader extends DefaultHandler {
	ArrayList<String> tags = new ArrayList<String>();
	String id;
	String description;
	String title;
	
	public BugReportReader() {
		super();
		this.id = "";
		this.title = "";
		this.description = "";
	}
	
	public String getID(){
		return id;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		tags.add(qName);
		
	}
	
	public void characters(char ch[], int start, int length)
			throws SAXException {
		String str = new String(ch, start, length);

		if(tags.get(tags.size()-1).equalsIgnoreCase("id")){
			this.id += str;
		}else if(tags.get(tags.size()-1).equalsIgnoreCase("description")){
			//System.out.println(str);
			this.description += str;
		}else if(tags.get(tags.size()-1).equalsIgnoreCase("title")){
			this.title += str;
		}
	}
	
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		tags.remove(tags.size() - 1);
	}
}
