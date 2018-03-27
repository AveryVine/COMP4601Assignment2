package edu.carleton.comp4601.resources;

import java.util.ArrayList;

public class User {
	
	int docId;
	String name, url;
	ArrayList<String> webpages;
	
	public User(int docId, String name, String url, ArrayList<String> webpages) {
		this.docId = docId;
		this.name = name;
		this.url = url;
		this.webpages = webpages;
	}
	
	public int getDocId() { 
		return docId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public ArrayList<String> getWebPages() {
		return webpages;
	}
	
	public String htmlTableData() {
		return "<tr> <td> " + docId + " </td> <td> " + name + " </td> <td> " + url + " </td> </tr> ";
	}
	
	public static String htmlTableHeader() {
		return "<tr> <th> ID </th> <th> Name </th> <th> URL </th> </tr> ";
	}
	
}
