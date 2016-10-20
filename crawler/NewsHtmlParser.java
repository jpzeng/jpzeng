package crawler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream; 
import java.io.InputStreamReader; 
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;

import org.htmlparser.Node;

import org.htmlparser.NodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.Parser;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.StyleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasChildFilter;

public class NewsHtmlParser {
	NewsHtmlParser(){}
	public static void HtmlStoreOss(){
		String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
		// accessKey请登录https://ak-console.aliyun.com/#/查看
		String accessKeyId = "***********";
		String accessKeySecret = "**************************";
		
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		 Date now = new Date(); 
		  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
		 
		  String hehe = dateFormat.format( now ); 
		  
		  String filepath = "C:/"+hehe+"-html"+"/";
		try{
			File root = new File(filepath);
			File[]files = root.listFiles();
			for(File file :files){
				
					ossClient.putObject("htmlfiles",file.getName(), file);
					
				
			}
			ossClient.shutdown();

		}
		catch(Exception e ){
			e.printStackTrace();
		}
	}
	public static void test(){
		System.out.print("test! ");
	}
	public void getossurl(){
		String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
		// accessKey请登录https://ak-console.aliyun.com/#/查看
		String accessKeyId = "*********";
		String accessKeySecret = "***************************";
		
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		
	
		String bucketName = "htmlfiles";
		String key = "";
		
		ObjectListing objectListing = ossClient.listObjects("htmlfiles");
		for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			key=objectSummary.getKey().toString();
			Date expiration = new Date(new Date().getTime() + 3600 * 10000);
			URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
			RDS a = new RDS();
			//System.out.println(url.toString());
			a.UpdateOssurl(key, url.toString());
			
			
	
		}
	   
		//// 设置URL过期时间为1小时
		Date expiration = new Date(new Date().getTime() + 3600 * 1000);
		// 生成URL
		//URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
	}
    public void HtmlStore(String Url ){
	  URL url ;
	  try{
	  url = new URL(Url);
	  URLConnection conn = url.openConnection();
	  BufferedReader br = new BufferedReader(
              new InputStreamReader(conn.getInputStream()));
	  
	  Date now = new Date(); 
	  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式

	  String hehe = dateFormat.format( now ); 
	  
	  String HtmlFile = "C:/"+hehe+"-html"+"/";
	  
		new File(HtmlFile).mkdir();
	  Url=Url.substring(Url.lastIndexOf('/')+1,Url.length());
	  System.out.println(Url); 

	  new File(HtmlFile).mkdir();
	  File file = new File (HtmlFile+Url);
	  if (!file.exists()) {
	        file.createNewFile();
	      }
	  FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      String inputLine;
      while ((inputLine = br.readLine()) != null) {
        bw.write(inputLine);
      }
 
      bw.close();
      br.close();
      
      
	  }
	  catch (Exception e )
	  {
		  e.printStackTrace();
	  }
	  
	   
   }
	public  String getTitle(String s,String newstime ,String tagname){
		String title="";
		String para="";
		 String newsname="";
		    newsname=s.substring(s.lastIndexOf('/')+1,s.lastIndexOf(".")-1);
		try{
			Segment B= new Segment();
			Parser parser = new Parser(s);
			parser.setEncoding("utf-8");
            //����title��ǩ���й���
			NodeFilter filter = new TagNameFilter("title");
			NodeList nodes = parser.extractAllNodesThatMatch(filter);
			if (nodes != null) {
				TagNode title_tag= (TagNode) nodes.elementAt(0);
				title=title_tag.toPlainTextString();
				
				
			}
			String []titles = title.split("\\|");
			title= titles[0].toString();
			String []titles1 = title.split("_");
			title= titles1[0].toString();
			System.out.println("标题：  "+title);
			
			
	
			
			//B.Seg(title,s);
			
			 parser.reset();
			 
		

			NodeFilter pfilter =new TagNameFilter("p");
			
			NodeList pnodes = parser.extractAllNodesThatMatch(pfilter);
			StringBuffer bufpara = new StringBuffer(); 
			if(pnodes!=null)
			for (Node node : pnodes.toNodeArray()) {  
			    if(node.getChildren()!=null){	
				    for(Node pnode:node.getChildren().toNodeArray()){
				    if(pnode.getClass()!=ScriptTag.class&&pnode.getClass()!=StyleTag.class&&pnode.getClass()!=Span.class){
					    para=pnode.toPlainTextString();
					    para=para.toString();
					    para=para.replaceAll("\\s*",""); 
					    para=para.replaceAll(" ",""); 
					    Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			            Matcher m = p.matcher(para);
			            para = m.replaceAll("");
				     	if(para.startsWith("&")||para.startsWith("'")||para.startsWith("Copyright"))
				  	  	   continue;
					    else
						   bufpara.append(para.trim());
				    }
				}
			    }
				else
					continue;
				
			}
			System.out.println("内容： "+bufpara.toString()+"\t");	
			//B.Seg(bufpara.toString(),s,title,newstime);
			//B.SegAndStore(bufpara.toString(),s,title,newstime, "testtest");
          //  B.show();
			B.Store(bufpara.toString(),s,title,newstime, newsname,tagname);
		   }catch(Exception e){  
	            e.printStackTrace();  
	        } 
		
		

        
		return title;
		}
	public  String News(String s,String newstime){
		String title="";
		String para="";
	    String newsname="";
	  
	    newsname=s.substring(s.lastIndexOf('/')+1,s.lastIndexOf("."));
		try{
			  HtmlStore(s);
			Segment B= new Segment();
			Parser parser = new Parser(s);
			parser.setEncoding("utf-8");
            //����title��ǩ���й���
			NodeFilter filter = new TagNameFilter("title");
			NodeList nodes = parser.extractAllNodesThatMatch(filter);
			if (nodes != null) {
				TagNode title_tag= (TagNode) nodes.elementAt(0);
				title=title_tag.toPlainTextString();
				
				
			}
			String []titles = title.split("\\|");
			title= titles[0].toString();
			String []titles1 = title.split("_");
			title= titles1[0].toString();
			System.out.println("标题：  "+title);
			
			
	
			
			//B.Seg(title,s);
			
			 parser.reset();
			 
		

			NodeFilter pfilter =new TagNameFilter("p");
			
			NodeList pnodes = parser.extractAllNodesThatMatch(pfilter);
			StringBuffer bufpara = new StringBuffer(); 
			if(pnodes!=null)
			for (Node node : pnodes.toNodeArray()) {  
			    if(node.getChildren()!=null){	
				    for(Node pnode:node.getChildren().toNodeArray()){
				    if(pnode.getClass()!=ScriptTag.class&&pnode.getClass()!=StyleTag.class&&pnode.getClass()!=Span.class){
					    para=pnode.toPlainTextString();
					    para=para.toString();
					    para=para.replaceAll("\\s*",""); 
					    para=para.replaceAll(" ",""); 
					    Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			            Matcher m = p.matcher(para);
			            para = m.replaceAll("");
				     	if(para.startsWith("&")||para.startsWith("'")||para.startsWith("Copyright"))
				  	  	   continue;
					    else
						   bufpara.append(para.trim());
				    }
				}
			    }
				else
					continue;
				
			}
		//	System.out.println("内容： "+bufpara.toString()+"\t");	
			OTS A = new OTS();
			A.intsert(s, bufpara.toString());
			String a = A.getpara(s);
			B.SegAndStore(a,s,title,newstime, newsname);
           
		   }catch(Exception e){  
	            e.printStackTrace();  
	        } 
		
		

        
		return title;
		}
    public  void getHtmlUrl(String url){
    	
    	 
    	try {  
    		
            Parser parser = new Parser(url);
            NodeFilter fl_filter=new TagNameFilter("ul");
            HasAttributeFilter haf = new HasAttributeFilter("class", "list_14");
            AndFilter af = new AndFilter( fl_filter,haf);
            
            
            NodeFilter filter =new HasParentFilter(af);
            AndFilter filter_all = new  AndFilter(filter,new TagNameFilter("li"));
            
            NodeFilter filter1 =new HasParentFilter(filter_all);
            AndFilter filter_all1 = new  AndFilter(filter1,new TagNameFilter("a"));
            
            NodeList nodeList = parser.extractAllNodesThatMatch(filter_all1);
            
           
           String newstime;
           String urls;
            for (Node node : nodeList.toNodeArray()) {  
            	
            	if (node instanceof LinkTag) { 
            	LinkTag link = (LinkTag) node; 
            	if (link != null) { 
            	
            	//HtmlStore(link.getLink());
            	urls=link.getLink();
            	
            	newstime = urls.substring(urls.lastIndexOf ("/",
            			urls.lastIndexOf("/") - 1)+1,urls.lastIndexOf("/")) ;
    			if(newstime.contains("-"))
    				{
    				System.out.println("地址： " + link.getLink());
    				System.out.println("时间:"+newstime);
    				//getTitle(link.getLink(),newstime);
    				News(link.getLink(),newstime);
    				}
    			
    			
            	} 
            	}
            }
        
             
        } catch (Exception e) {  
            e.printStackTrace();  
          
        }  
    	}
	
}
