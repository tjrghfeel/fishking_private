<%--
  Created by IntelliJ IDEA.
  User: mac
  Date: 2021/04/12
  Time: 4:30 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>Hello, ${name}!</h1>
    <h5>${maps.test}</h5>
    <c:forEach var="l" items="${lists}">
        <h6>${l}</h6>
    </c:forEach>
</body>
</html>
