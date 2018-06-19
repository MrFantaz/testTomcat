<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %><%--
  Created by IntelliJ IDEA.
  User: dima
  Date: 14.06.2018
  Time: 21:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Pohel</h1>
<% String hex = request.getAttribute("hex").toString();
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:YYYY");
    String dateFrom = simpleDateFormat.format(date);
   response.getWriter().println("<h1>Current date: "+dateFrom+"</h1>");
%>
<%=hex%>
</body>
</html>
