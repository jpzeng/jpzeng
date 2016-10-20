<?php
$url="rm-***********.mysql.rds.aliyuncs.com:3306";
url tagname newstime title ossurl
username password theme
$url = 'rm-**********.mysql.rds.aliyuncs.com:3306/'
$user="*****";
$pass="*******";
$conn = mysql_connect($url,$user,$pass) or die("connect fail !");
mysql_select_db("bigdata",$conn);
$sql=mysql_query("select * from user ");
$test=mysql_fetch_row($sql);
echo $test;
?>
