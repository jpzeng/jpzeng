package crawler;

public class HtmlCrawler {
	public static void main(String[] args) {

		NewsHtmlParser p = new NewsHtmlParser();
	    p.getHtmlUrl("http://news.sina.com.cn/");
	    p.HtmlStoreOss();
	 //   p.test();
		//System.out.print("hello world");

	}

}
