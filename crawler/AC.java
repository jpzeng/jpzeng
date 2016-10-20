package crawler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import multi.patt.match.ac.*;

public class AC {
	private  ArrayList<String> keys;
	AC(){
		
	}
	public  void match(String filename,String url,String title,String newstime){
		try{
			  
			
		ArrayList keyword=new ArrayList();
		AcApply obj = new AcApply();		
		String filename2 ="./lib/keywords.txt";
		StringBuffer words = new StringBuffer();
		InputStreamReader read1 = new InputStreamReader(
                new FileInputStream(filename),"UTF-8");//考虑到编码格式
                BufferedReader bufferedReader1 = new BufferedReader(read1);
                String word = null;
                while((word= bufferedReader1.readLine()) != null){
                    words.append(word);
                }
                bufferedReader1.close();
		InputStreamReader read = new InputStreamReader(
                new FileInputStream(filename2),"UTF-8");//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                
                while((lineTxt = bufferedReader.readLine()) != null){
                	//System.out.print(lineTxt);
                	Set result = obj.findSingleWord(lineTxt, words.toString());
                	if(result.size() == 0)
                		keyword.add(0);
                	else
                		keyword.add(1);
                   
                }
                bufferedReader.close();
                
                BufferedWriter output = new BufferedWriter( new FileWriter(filename));
                output.write(url+"\r\n");
                output.write(title+"\r\n");
                output.write(newstime+"\r\n");
                for(int i=0;i<keyword.size();i++)
        			output.write(keyword.get(i)+"\r\n");
                output.close();
                
			
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
			
	}
	
	
	public  void analyzer(String filename,String url,String title,String newstime,String tagname){
		try{
			  
			
		ArrayList keyword=new ArrayList();
		AcApply obj = new AcApply();		
		String filename2 ="lib/keywords.txt";
		StringBuffer words = new StringBuffer();
		InputStreamReader read1 = new InputStreamReader(
                new FileInputStream(filename));//考虑到编码格式
                BufferedReader bufferedReader1 = new BufferedReader(read1);
                String word = null;
                while((word= bufferedReader1.readLine()) != null){
                    words.append(word);
                }
                bufferedReader1.close();
		InputStreamReader read = new InputStreamReader(
                new FileInputStream(filename2));//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	
                	Set result = obj.findSingleWord(lineTxt, words.toString());
                	if(result.size() == 0)
                		keyword.add(0);
                	else
                		keyword.add(1);
                   
                }
                bufferedReader.close();
                
                BufferedWriter output = new BufferedWriter( new FileWriter(filename));
                output.write(url+"\r\n");
                output.write(tagname+"\r\n");
                output.write(title+"\r\n");
                output.write(newstime+"\r\n");
                for(int i=0;i<keyword.size();i++)
        			output.write(keyword.get(i)+"\r\n");
                output.close();
                
			
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
			
	}

}
