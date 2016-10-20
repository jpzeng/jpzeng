package crawler;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Segment {

	private static String[] words = new String[100000];
	private static int counts;

	public void Seg(String news, String url, String title, String newstime) {

		try {

			// AC acmatch=new AC();
			IKAnalyzer anal = new IKAnalyzer(true);
			StringReader reader = new StringReader(news);

			TokenStream ts = anal.tokenStream("", reader);
			CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);

			while (ts.incrementToken()) {
				words[counts] = term.toString();
				counts++;
				// acmatch.match(term.toString(),url,title,newstime);

			}
			reader.close();
			anal.close();
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SegAndStore(String news, String url, String title, String newstime, String newsname) {

		try {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式

			String hehe = dateFormat.format(now);

			String HtmlFile = "C:/" + hehe + "/";
			String filename = HtmlFile + newsname + ".txt";
			new File(HtmlFile).mkdir();

			AC acmatch = new AC();
			IKAnalyzer anal = new IKAnalyzer(true);
			StringReader reader = new StringReader(news);
			ArrayList keyword = new ArrayList();

			TokenStream ts = anal.tokenStream("", reader);
			CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);

			while (ts.incrementToken()) {
				keyword.add(term.toString());

			}
			reader.close();
			anal.close();

			File f = new File(filename);
			if (f.exists()) {
				System.out.print("文件存在");
			} else {
				System.out.print("文件不存在");
				f.createNewFile();// 不存在则创建
			}
			StringBuffer buf = new StringBuffer();
			

			Writer output = new OutputStreamWriter(new FileOutputStream(f,true), "UTF-8");
			

			String str = "";
			for (int i = 0; i < keyword.size(); i++) {
				str = (String) keyword.get(i);
				if (str == null)
					continue;
				if (Character.isDigit(str.charAt(0)))
					continue;
				if (str.charAt(0) >= 'A' && str.charAt(0) <= 'Z' || str.charAt(0) >= 'a' && str.charAt(0) <= 'z')
					continue;
				if (str.length() < 2)
					continue;

				buf.append(str + "\r\n");
			}

			output.write(buf.toString());
			output.close();
			acmatch.match(filename, url, title, newstime);

			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Store(String news, String url, String title, String newstime, String newsname, String tagname) {

		try {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式

			String hehe = dateFormat.format(now);

			String HtmlFile = "./src/" + hehe + "-1" + "/";
			String filename = HtmlFile + newsname + ".txt";
			new File(HtmlFile).mkdir();

			AC acmatch = new AC();
			IKAnalyzer anal = new IKAnalyzer(true);
			StringReader reader = new StringReader(news);
			ArrayList keyword = new ArrayList();

			TokenStream ts = anal.tokenStream("", reader);
			CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);

			while (ts.incrementToken()) {
				keyword.add(term.toString());

			}
			reader.close();
			anal.close();

			File f = new File(filename);
			if (f.exists()) {
				System.out.print("文件存在");
			} else {
				System.out.print("文件不存在");
				f.createNewFile();// 不存在则创建
			}
			StringBuffer buf = new StringBuffer();
			BufferedWriter output = new BufferedWriter(new FileWriter(f));

			String str = "";
			for (int i = 0; i < keyword.size(); i++) {
				str = (String) keyword.get(i);
				if (str == null)
					continue;
				if (Character.isDigit(str.charAt(0)))
					continue;
				if (str.charAt(0) >= 'A' && str.charAt(0) <= 'Z' || str.charAt(0) >= 'a' && str.charAt(0) <= 'z')
					continue;
				if (str.length() < 2)
					continue;

				buf.append(str + "\r\n");
			}

			output.write(buf.toString());
			output.close();
			acmatch.analyzer(filename, url, title, newstime, tagname);

			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void show() {
		int i;
		String r;
		String str = "";
		StringBuffer buf = new StringBuffer();

		try {
			File f = new File("./src/keywords.txt");
			if (f.exists()) {
				System.out.print("文件存在");
			} else {
				System.out.print("文件不存在");
				f.createNewFile();// 不存在则创建
			}
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			Set set = new HashSet(Arrays.asList(words));
			Iterator<String> it = set.iterator();

			while (it.hasNext()) {
				str = it.next();
				if (str == null)
					continue;
				if (Character.isDigit(str.charAt(0)))
					continue;
				if (str.length() < 2)
					continue;
				if (str.charAt(0) >= 'A' && str.charAt(0) <= 'Z' || str.charAt(0) >= 'a' && str.charAt(0) <= 'z')
					continue;

				buf.append(str + "\r\n");
			}

			output.write(buf.toString());
			output.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
