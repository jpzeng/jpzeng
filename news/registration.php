<!doctype html>
<html lang="en">
<head>
<title>新闻首页</title>
<link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<body>
<?php  


$username = $_POST['username'];  
$password = $_POST['password'];  
$keyname1 = $_POST['keyname1'];  
$keyname2 = $_POST['keyname2'];  
$keyname3 = $_POST['keyname3'];  
$keyname=$keyname1."_".$keyname2."_".$keyname3;

if(isset($_POST['username']))
{
$url = 'rm-*********.mysql.rds.aliyuncs.com:3306/';
$user="******";
$pass="*********";
$conn = mysql_connect($url,$user,$pass) or die("connect fail !");
mysql_select_db("bigdata",$conn);
$sql = mysql_query("insert into user(username,password,theme) values('$username','$password','$keyname')");
error_reporting(E_ALL ^ E_NOTICE);
if($sql){  
    //登录成功  
  
echo "注册成功 <a href='login.html'>登录</a> ";
   
    
} else {  
    exit('用户名已存在！点击此处 <a href="javascript:history.back(-1);">返回</a> 重试');  
}   
 mysql_close($conn);

}
//包含数据库连接文件  




 
  
  
  
//注销登录  

?>  

</div>
</body>
</html>