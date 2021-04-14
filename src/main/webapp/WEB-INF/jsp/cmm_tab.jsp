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
<nav class="nav nav-pills nav-bar nav-justified">
    <a class="nav-link <%=uri.contains("dashboard")? "active" : ""%>" href="/boarding/dashboard"><figure class="dashboard"></figure>대시보드</a>
    <a class="nav-link" ><figure class="sail"></figure>승선관리</a>
    <a class="nav-link <%=uri.contains("camera")? "active" : ""%>" href="/boarding/camera"><figure class="camera"></figure>카메라관리</a>
    <a class="nav-link" ><figure class="cs"></figure>고객센터</a>
</nav>
