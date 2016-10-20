package ODPSandOSS;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class RDS {
	public static void connect() {

	Connection conn = null;
	String sql;
	String url = "jdbc:mysql://rm-*********.mysql.rds.aliyuncs.com:3306/bigdata?"
			+ "user=****&password=********useUnicode=true&characterEncoding=UTF8";
	try {

		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url);
		Statement stmt = conn.createStatement();
		sql = "select * from user";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {

			String key = new String(rs.getString(1).toString().getBytes(), "UTF-8");
			key = key.toLowerCase();
			System.out.println(key);

		}
	} catch (SQLException e) {
		System.out.println("MySQL操作错误");
		e.printStackTrace();
	} catch (Exception e) {
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
	public  void insert(String url, String tagname, String newstime, String title,String ossurl ){
		Connection conn = null;
		String sql;
		String conurl = "jdbc:mysql://rm-*******.mysql.rds.aliyuncs.com:3306/bigdata?"
				+ "user=******&password=**********&useUnicode=true&characterEncoding=UTF8";
		try {

			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(conurl);
			Statement stmt = conn.createStatement();
			 sql="INSERT INTO news(url, tagname, newstime, title,ossurl) VALUES( '"+url+"','"+tagname+"','"+newstime+"','"+title+"','"+ossurl+"')";
			
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

}
