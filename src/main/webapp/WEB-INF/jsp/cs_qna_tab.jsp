<%--
  Created by IntelliJ IDEA.
  User: jinheelee
  Date: 2021/04/13
  Time: 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String uri = request.getRequestURI();
%>
<nav class="nav nav-pills nav-menu nav-justified">
    <a class="nav-link <%=!uri.contains("csqnalist") && !uri.contains("csqnadetail") && uri.contains("csqna")? "active" : ""%>" href="/boarding/csqna">문의하기</a>
    <a class="nav-link <%=uri.contains("csqnalist") || uri.contains("csqnadetail")? "active" : ""%>" href="/boarding/csqnalist">문의내역</a>
</nav>
