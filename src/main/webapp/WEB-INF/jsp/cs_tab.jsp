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
    <a class="nav-link <%=uri.contains("csnotice")? "active" : ""%>" href="/boarding/csnotice">공지사항</a>
    <a class="nav-link <%=uri.contains("csfaq")? "active" : ""%>" href="/boarding/csfaq">자주하는 질문</a>
    <a class="nav-link <%=uri.contains("csqna")? "active" : ""%>" href="/boarding/csqna">1:1문의</a>
    <a class="nav-link <%=uri.contains("csset")? "active" : ""%>" href="/boarding/csset">설정</a>
</nav>
