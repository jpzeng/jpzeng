<!doctype html>
<html lang="en">
<head>
<title>新闻首页</title>
<link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<body>
<?php  







if (!empty($_COOKIE['UserName'])&&!isset($_POST['password']))
{

$uName=$_COOKIE['UserName'];
$url = 'rm-******************.rds.aliyuncs.com:3306/';
$user="******";
$pass="************";
$conn = mysql_connect($url,$user,$pass) or die("connect fail !");
mysql_select_db("bigdata",$conn);
$username=$uName;
    echo "<div class='header'>
        <div class ='web_name'>个性化体育新闻</div>
        <div class='user_display'>
             用户$username,欢迎您！ 
        </div>
      </div>
      <div class='header_bottom'>
      </div>

      <div class='container'>";
    
    
    $select_query= mysql_query("select keyname from user where username = $username");
    $keyname = mysql_fetch_row($select_query);
    $keysname=explode('_', $keyname[0]);
    $i =0 ;
    
    while( $i<sizeof($keysname))
    {
        $news_sql=mysql_query("select * from keyandurl where keyname='$keysname[$i]'");

        echo "<div class='keyname'> $keysname[$i]</div>";

        echo "<ul>";
         while($news=mysql_fetch_row($news_sql))
                echo "<li> <a href='$news[0]' target='_blank'>$news[3] </a><div class='timedisplay'>$news[2]</div></li>";
        echo "</ul>";
        
        $i++;
    }

}
else
{
$username = $_POST['username'];  
$password = $_POST['password'];  


$url = 'rm-**************.mysql.rds.aliyuncs.com:3306/';
$user="******";
$pass="****************";
$conn = mysql_connect($url,$user,$pass) or die("connect fail !");
mysql_select_db("bigdata",$conn);

$check_query = mysql_query("select username from user where username='$username' and password='$password' ");
if($result = mysql_fetch_array($check_query)){  
    //登录成功  

    echo "<div class='header'>

    <div class ='web_name'>
    <div class ='web_name1'>Pesonal</div>
    <div class ='web_name2'>News</div>
    </div>
        <div class='user_display'>

             用户$username,欢迎您！ 
        </div>
      </div>
      <div class='header_bottom'>
      </div>

      <div class='container'>";
    setCookie("UserName",$username,time()+600);
    session_start();  
    $_SESSION['username'] = $username;  
    $select_query= mysql_query("select theme from user where username = $username");
    $keyname = mysql_fetch_row($select_query);
    $keysname=explode('_', $keyname[0]);
    $i =0 ;
    error_reporting(E_ALL ^ E_NOTICE);
    while( $i<sizeof($keysname))
    {
        $news_sql=mysql_query("select * from news where tagname='$keysname[$i]'");

        echo "<div class='keyname'> $keysname[$i]</div>";

        echo "<ul>";
        while($news=mysql_fetch_row($news_sql))
                echo "<li>
                    <a href='$news[0]' target='_blank'>$news[3] </a>
                    <div  class = 'ossurldisplay' ><a href='$news[4]' target='_blank'>源网页 </a></div>
                    <div class='timedisplay'>$news[2]</div> 
                    
                    </li>";
        echo "</ul>";
        
        $i++;
    }
    

   
    
} else {  
    exit('登录失败！点击此处 <a href="javascript:history.back(-1);">返回</a> 重试');  
}   
}


//包含数据库连接文件  




 
  
  
  
//注销登录  

?>  

</div>
</body>
</html>