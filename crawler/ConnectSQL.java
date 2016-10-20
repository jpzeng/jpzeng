package crawler;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
public class ConnectSQL {
	
	public ArrayList<String> ExtractKeys(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/personal_news?useUnicode=true&amp;characterEncoding=UTF-8 ","root","");
			ArrayList<String> keys = new ArrayList();
			
			 
	        Statement stmt = con.createStatement();
	        String sql = "select * from keyboards";
	        ResultSet rs = stmt.executeQuery(sql);
	        while(rs.next()){     
	        	
	        String key =new String( rs.getString(1).toString().getBytes(),"UTF-8");
	        key=key.toLowerCase();
	        keys.add(key);
	       
	        }
	        
	        rs.close();
	      
	      stmt.close();
	   
	      con.close();
	      return keys;
			}
			catch(Exception e ){
				e.printStackTrace();
				return null;
			}
	}
	public void InsertSQL( String keys, String URL,String title,String newstime ){
		try{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/personal_news","root","");
		Statement stmt= con.createStatement();
		String sql="INSERT INTO keyandurl( keyname, url,title,newstime) VALUES( '"+keys+"','"+URL+"','"+title+"','"+newstime+"')";
		
		String exesql=new String (sql);
		stmt.execute(exesql);
		 stmt.close();
		   
	      con.close();
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
    public void DeleteSQL(){
    	try{
    		Class.forName("com.mysql.jdbc.Driver");
    		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/personal_news","root","");
    		Statement stmt= con.createStatement();
    		String sql="Delete from keyandurl where url  in (SELECT * FROM(select  url  from keyandurl  group  by  url   having  count(url) > 1 ) AS TEMP) "
    				+ "and id not in (SELECT * FROM(select min(id)  from keyandurl  group  by  url   having  count(url) > 1 ) AS TEMP) ";
    		String exesql=new String (sql);
    		stmt.executeUpdate(exesql);
    		 stmt.close();
    		   
   	      con.close();
    		
    	}catch(Exception e){
			e.printStackTrace();
		}
    }
}