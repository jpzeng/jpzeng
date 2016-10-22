package ODPSandOSS;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import com.aliyun.odps.Column;
import com.aliyun.odps.Instance;
import com.aliyun.odps.Instance.TaskSummary;
import com.aliyun.odps.Odps;
import com.aliyun.odps.PartitionSpec;
import com.aliyun.odps.TableSchema;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.RecordReader;
import com.aliyun.odps.data.RecordWriter;
import com.aliyun.odps.task.SQLTask;
import com.aliyun.odps.tunnel.TableTunnel;
import com.aliyun.odps.tunnel.TunnelException;
import com.aliyun.odps.tunnel.TableTunnel.DownloadSession;
import com.aliyun.odps.tunnel.TableTunnel.UploadSession;


public class TestSQLTask {
    TestSQLTask(){};
	private static String tunnelUrl = "http://dt.odps.aliyun.com";
	private static String odpsUrl = "http://service.odps.aliyun.com/api";
	private static String uploadodpsUrl = "http://service.odps.aliyun.com";
	private static String table = "news_collector";

	private static String accessId = "***********";
	private static String accessKey = "**************************";

	private static String project = "news_recommendation";

	// private static String sql="create table sale_detail1 (测试 string)
	// partitioned by (sale_date string);";
	public static void TableCreate() {
		Account account = new AliyunAccount(accessId, accessKey);
		Odps odps = new Odps(account);
		odps.setEndpoint(odpsUrl);
		odps.setDefaultProject(project);
		// File file = new
		// File("F:/eclipse/workspace/crawler/src/keywords.txt");
		StringBuffer buf = new StringBuffer();
		StringBuffer buf1 = new StringBuffer();
		buf.append("create table news_collector (url string, tag_name string, title string , news_time string ,   ");
		buf1.append("create table news_collector (url string, tag_name string, title string , news_time string ,   ");
		try {

			for (int i = 1; i <= 1196; i++) {

				buf.append("w" + Integer.toString(i) + " int ,");
				buf1.append("w" + Integer.toString(i) + " int ,");
			}
			/*
			 * while ((lineTxt = bufferedReader.readLine()) != null) { if
			 * (lineTxt == "") continue; buf.append(lineTxt.trim() + " int ,");
			 * }
			 */
			buf.deleteCharAt(buf.length() - 1);
			buf.append(") partitioned by (collect_time string);");
			// buf1.deleteCharAt(buf.length() - 1);
			// buf1.append(") partitioned by (collect_time string);");

			SQLTask.run(odps, buf.toString());
			// SQLTask.run(odps, buf1.toString());

			System.out.print("success! ");

		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * try {
		 * 
		 * TableTunnel tunnel = new TableTunnel(odps);
		 * tunnel.setEndpoint(tunnelUrl); PartitionSpec partitionSpec = new
		 * PartitionSpec(partition); UploadSession uploadSession =
		 * tunnel.createUploadSession(project, table, partitionSpec);
		 * System.out.println("Session Status is : " +
		 * uploadSession.getStatus().toString()); TableSchema schema =
		 * uploadSession.getSchema(); RecordWriter recordWriter =
		 * uploadSession.openRecordWriter(0); Record record
		 * =uploadSession.newRecord(); for (int i = 0; i <
		 * schema.getColumns().size(); i++) { Column column =
		 * schema.getColumn(i);
		 * 
		 * switch (column.getType()) { case BIGINT: record.setBigint(i, 1L);
		 * break; case BOOLEAN: record.setBoolean(i, true); break; case
		 * DATETIME: record.setDatetime(i, new Date()); break; case DOUBLE:
		 * record.setDouble(i, 0.0); break; case STRING: record.setString(i,
		 * "sample"); break; default: throw new RuntimeException(
		 * "Unknown column type: " + column.getType()); } }
		 * 
		 * for (int i = 0; i < 10; i++) { recordWriter.write(record); }
		 * recordWriter.close(); uploadSession.commit(new Long[]{0L});
		 * System.out.println( "upload success!"); } catch (TunnelException e) {
		 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
		 */
	}

	public static void NewsCollectForAnalyze() {
		Account account = new AliyunAccount(accessId, accessKey);
		Odps odps = new Odps(account);
		odps.setEndpoint(odpsUrl);
		odps.setDefaultProject(project);

		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式
		String today = dateFormat.format(now);
		String filepath = "C:/" + today ;
		String partition = "collect_time='" + today+"'";
		try {
			SQLTask.run(odps,
					"ALTER TABLE news_collector  ADD IF NOT EXISTS PARTITION (collect_time='" + today + "'); ");

			TableTunnel tunnel = new TableTunnel(odps);
			tunnel.setEndpoint(tunnelUrl);
			PartitionSpec partitionSpec = new PartitionSpec(partition);
			UploadSession uploadSession = tunnel.createUploadSession(project, table, partitionSpec);
			System.out.println("Session Status is : " + uploadSession.getStatus().toString());
			TableSchema schema = uploadSession.getSchema();
			RecordWriter recordWriter = uploadSession.openRecordWriter(0);

			File root = new File(filepath);
			File[] files = root.listFiles();
			for (File file : files) {

				InputStreamReader read = new InputStreamReader(new FileInputStream(file));// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				int i = 0;
				Record record = uploadSession.newRecord();
				// record.setString(0, "");
				while ((lineTxt = bufferedReader.readLine()) != null) {

					if (lineTxt == "")
						break;

					if (i < 4)
					
					 if(i==1){ System.out.println(lineTxt);
					  record.setString(2, lineTxt); i++; } else
					 
					{
						//System.out.println(lineTxt);
						record.setString(i, lineTxt);
					} else
						record.setBigint(i, Long.parseLong(lineTxt));
					i++;
				}

				recordWriter.write(record);
				read.close();
				bufferedReader.close();

			}
			recordWriter.close();

			uploadSession.commit(new Long[] { 0L });
			System.out.println("upload success!");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

	public static void ResultCollect() {
		Account account = new AliyunAccount(accessId, accessKey);
		Odps odps = new Odps(account);
		odps.setEndpoint(odpsUrl);
		odps.setDefaultProject(project);
		TableTunnel tunnel = new TableTunnel(odps);
		tunnel.setEndpoint(tunnelUrl);
		try {
			DownloadSession downloadSession = tunnel.createDownloadSession(project, "news_result");
			System.out.println("Session Status is : " + downloadSession.getStatus().toString());
			long count = downloadSession.getRecordCount();
			System.out.println("RecordCount is: " + count);
			RecordReader recordReader = downloadSession.openRecordReader(0, count);
			Record record;
			while ((record = recordReader.read()) != null) {
				consumeRecord(record, downloadSession.getSchema());
			}
			System.out.print("success!");
			recordReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void consumeRecord(Record record, TableSchema schema) {
		String url="";
		String tagname="";
		String newstime="";
		String title="";
		String ossurl="";
		for (int i = 0; i < 4; i++) {
			Column column = schema.getColumn(i);
			String colValue = null;
			switch (column.getType()) {
			case BIGINT: {
				Long v = record.getBigint(i);
				colValue = v == null ? null : v.toString();
				break;
			}
			case BOOLEAN: {
				Boolean v = record.getBoolean(i);
				colValue = v == null ? null : v.toString();
				break;
			}
			case DATETIME: {
				Date v = record.getDatetime(i);
				colValue = v == null ? null : v.toString();
				break;
			}
			case DOUBLE: {
				Double v = record.getDouble(i);
				colValue = v == null ? null : v.toString();
				break;
			}
			case STRING: {
				String v = record.getString(i);
				colValue = v == null ? null : v.toString();
				break;
			}
			default:
				throw new RuntimeException("Unknown column type: " + column.getType());
			}
			
			switch(i){
			case 0: url=colValue;
			case 1: newstime=colValue;
			case 2: title=colValue;
			case 3: tagname=colValue;
			}
			
		}
		 ossurl=url.substring(url.lastIndexOf('/')+1,url.length());
		 //System.out.print(ossurl);
		RDS a = new RDS();
		a.insert(url, tagname, newstime, title,ossurl);
	}
    public static String GetOSS(String Url ){
    	
    	 Url=Url.substring(Url.lastIndexOf('/')+1,Url.length());
   	     //System.out.println(Url); 
   	    String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
		// accessKey请登录https://ak-console.aliyun.com/#/查看
		String accessKeyId = "************";
		String accessKeySecret = "******************************";
		
		// 创建OSSClient实例
		//OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		String bucketName = "htmlfiles";
		String key = Url;
		// 设置URL过期时间为1小时
		Date expiration = new Date(new Date().getTime() + 3600 * 1000);
		// 生成URL
		//URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
		//System.out.print(url);
    	 return null;
    	
    }

}