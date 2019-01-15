<html>
<body>
<h2>Hello World!</h2>
</body>

<%


    System.out.println(request.getParameter("bage"));
    Cookie cookie = new Cookie("my-key","my-value");
    response.addCookie(cookie);

%>

</html>
