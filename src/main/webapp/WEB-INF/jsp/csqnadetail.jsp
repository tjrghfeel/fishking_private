<%@ page import="com.tobe.fishking.v2.model.board.QnADetailDto" %><%--
  Created by IntelliJ IDEA.
  User: jinheelee
  Date: 2021/04/13
  Time: 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="cmm_head.jsp" />
<body>
<!-- Navigation -->
<nav class="navbar fixed-top navbar-dark bg-primary">
    <a href="javascript:location.href = '/boarding/dashboard';" class="nav-left"><img src="/assets/smartsail/img/svg/navbar-back.svg" alt="뒤로가기"/></a>
    <span class="navbar-title">고객센터</span>
    <%--<a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>--%>
</nav>
<!-- // Navigation -->

<!-- 탭메뉴 -->
<jsp:include page="cs_tab.jsp" />
<!--// 탭메뉴 -->

<!-- 탭메뉴 -->
<jsp:include page="cs_qna_tab.jsp" />
<!--// 탭메뉴 -->

<%
    QnADetailDto result = (QnADetailDto) request.getAttribute("result");
%>
<!-- 데이터 -->
<div class="container nopadding mt-3">
    <dl class="dl-horizontal-round">
        <dt>문의 유형</dt>
        <dd>${result.questionType}</dd>
        <dt>답변 여부</dt>
        <dd><span class="status-icon ${result.replied? 'status3' : 'status2'}">${result.replied? '답변완료' : '답변대기'}</span> &nbsp; <%=result.getDate().substring(0,10).replaceAll("-",".")%></dd>
        <dt>문의 내용</dt>
        <dd>
            <%=result.getContents().replaceAll("\n","<br/>")%>
        </dd>
    </dl>
    <hr class="space mt-4 mb-4">
    <c:if test="${result.replied}">
        <dl class="dl-horizontal-round">
            <dt>답변 내용</dt>
            <dd>
                <%=result.getReplyContents().replaceAll("\n","<br/>")%>
            </dd>
        </dl>
        <hr class="space mt-4 mb-4">
    </c:if>
</div>
<!--// 데이터 -->

<!-- 하단버튼 -->
<div class="fixed-bottom">
    <div class="row no-gutters">
        <div class="col-12"><a href="/boarding/csqnalist" class="btn btn-primary btn-lg btn-block">목록으로</a></div>
    </div>
</div>
<!--// 하단버튼 -->

<jsp:include page="cmm_foot.jsp" />

</body>
</html>
