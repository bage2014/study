<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<script src="https://cdn.bootcss.com/jquery/1.10.2/jquery.min.js">


<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

</head>

<script type="text/javascript">

    function doLogin(){

        var username = $("#username").val();
        var password = $("#password").val();
        $.ajax({
               type: "POST",
               url: "Login",
               data: "username="+username+"&password=" + password,
               success: function(msg){
                 alert( "Data Saved: " + msg );
               }
            });
    } 

</script>

<body>

    <form action="login" name="loginForm">

        用户名:<input type="text" name = "username" id = "username" />
        <br />
        <br />
        密&nbsp;码: <input type="text" name = "password" id = "password" />
        <br />
        <br />
        <button type="button" name = "submitForm" id = "submitForm" onclick="doLogin()">登录</button>     
    </form>

</body>
</html>