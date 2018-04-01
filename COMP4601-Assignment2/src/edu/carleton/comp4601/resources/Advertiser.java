package edu.carleton.comp4601.resources;

public class Advertiser {

	public static String augment(String user, String page) {
		WebPage webpage = Database.getInstance().getWebPage(page);
		String pageHtml = webpage.getHTML();
		String pageAdGenre = webpage.getGenre();
		String userAdGenre = Database.getInstance().getUser(user).getPreferredGenre();
		String bodyTag = "<body>";
		int insertIndex = pageHtml.indexOf(bodyTag) + bodyTag.length();
		String ads = "<div>User-based Advertisement: " + userAdGenre + "<hr></div>" + "<div>Page-based Advertisement: " + pageAdGenre + "<hr></div>";
		pageHtml = pageHtml.substring(0, insertIndex) + ads + pageHtml.substring(insertIndex, pageHtml.length() - 1);
		return pageHtml;
	}
	
}
