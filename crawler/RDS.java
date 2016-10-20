package crawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class RDS {
	public  void insert(String url, String tagname, String newstime, String title ){

		
		Connection conn = null;
		String sql;
		String conurl = "jdbc:mysql://rm-***********.mysql.rds.aliyuncs.com:3306/bigdata?"
				+ "user=*****&password=******@#$&useUnicode=true&characterEncoding=UTF8";
		try {

			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(conurl);
			Statement stmt = conn.createStatement();
			 sql="INSERT INTO news(url, tagname, newstime, title) VALUES( '"+url+"','"+tagname+"','"+newstime+"','"+title+"')";
			
			String exesql=new String (sql);
			stmt.execute(exesql);
			stmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
     public void UpdateOssurl (String newsname, String ossurl){
    	 Connection conn = null;
 		String sql;
 		String conurl = "jdbc:mysql://rm-************.mysql.rds.aliyuncs.com:3306/bigdata?"
 				+ "user=******&password=******@#$&useUnicode=true&characterEncoding=UTF8";
 		try {

 			Class.forName("com.mysql.jdbc.Driver");
 			conn = DriverManager.getConnection(conurl);
 			Statement stmt = conn.createStatement();
 			stmt.executeUpdate("update news set ossurl ='"+ossurl+"' where ossurl='"+newsname+"'");
 			
 		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
     }
}
