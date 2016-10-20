package crawler;

import com.aliyun.openservices.ots.ClientException;
import com.aliyun.openservices.ots.OTSClient;
import com.aliyun.openservices.ots.OTSException;
import com.aliyun.openservices.ots.model.ColumnValue;
import com.aliyun.openservices.ots.model.Condition;
import com.aliyun.openservices.ots.model.GetRowRequest;
import com.aliyun.openservices.ots.model.GetRowResult;
import com.aliyun.openservices.ots.model.PrimaryKeyValue;
import com.aliyun.openservices.ots.model.PutRowRequest;
import com.aliyun.openservices.ots.model.Row;
import com.aliyun.openservices.ots.model.RowExistenceExpectation;
import com.aliyun.openservices.ots.model.RowPrimaryKey;
import com.aliyun.openservices.ots.model.RowPutChange;
import com.aliyun.openservices.ots.model.SingleRowQueryCriteria;
/*
往表格里添加记录
 */
public class OTS {
	public static void intsert(String url, String para ){
		final String endPoint = "http://tshtml.cn-hangzhou.ots.aliyuncs.com";
		final String accessId = "****************";
		final String accessKey = "*************************************";
		final String instanceName = "tshtml";
		// 创建一个OTSClient对象
		OTSClient client = new OTSClient(endPoint, accessId, accessKey, instanceName);
		RowPrimaryKey primaryKey = new RowPrimaryKey();
		primaryKey.addPrimaryKeyColumn("url", PrimaryKeyValue.fromString(url));
		
		RowPutChange rowChange = new RowPutChange("pagestore");
		rowChange.setPrimaryKey(primaryKey);
		// 定义要写入改行的属性列
	
		rowChange.addAttributeColumn("col0", ColumnValue.fromString(para));
		// RowExistenceExpectation.EXPECT_NOT_EXIST表示只有此行不存在时才会执行插入
		rowChange.setCondition(new Condition(RowExistenceExpectation.EXPECT_NOT_EXIST));
		try
		{
		// 构造插入数据的请求对象，
		PutRowRequest request = new PutRowRequest();
		request.setRowChange(rowChange);
		// 调用PutRow接口插入数据
		client.putRow(request);
		// 如果没有抛出异常，则说明执行成功
		System.out.println("Put row succeeded.");
		} catch (ClientException ex) {
		System.out.println("Put row failed.");
		} catch (OTSException ex) {
		System.out.println("Put row failed.");
		}
		client.shutdown();
	}
	public static String getpara(String url ){
		final String endPoint = "http://tshtml.cn-hangzhou.ots.aliyuncs.com";
		final String accessId = "****************";
		final String accessKey = "*****************************";
		final String instanceName = "tshtml";
		// 创建一个OTSClient对象
		OTSClient client = new OTSClient(endPoint, accessId, accessKey, instanceName);
		RowPrimaryKey primaryKey = new RowPrimaryKey();
		primaryKey.addPrimaryKeyColumn("url", PrimaryKeyValue.fromString(url));
		SingleRowQueryCriteria criteria = new SingleRowQueryCriteria("pagestore");
		criteria.setPrimaryKey(primaryKey);
		try
		{
		// 构造查询请求对象，这里未指定读哪列，默认读整行
		GetRowRequest request = new GetRowRequest();
		request.setRowQueryCriteria(criteria);
		// 调用GetRow接口查询数据
		GetRowResult result = client.getRow(request);
		// 输出此行的数据
		Row row = result.getRow();
		int consumedReadCU = result.getConsumedCapacity().getCapacityUnit().getReadCapacityUnit();
		client.shutdown();
	//	System.out.println("Consumed capacity unti：" + consumedReadCU);
		return (row.getColumns().get("col0").toString());
		
		} catch (ClientException ex) {
		// 如果抛出客户端异常，则说明参数等有误
		System.out.println("Get row failed.");
		} catch (OTSException ex) {
		// 如果抛出服务器端异常，则说明请求失败
		System.out.println("Get row failed.");
		}
		return null;
		
	}

}

