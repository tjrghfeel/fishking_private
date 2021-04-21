<%@ page import="com.tobe.fishking.v2.model.board.NoticeDetailDto" %><%--
  Created by IntelliJ IDEA.
  User: jinheelee
  Date: 2021/04/13
  Time: 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="cmm_head.jsp" />
<body>
<!-- Navigation -->
<nav class="navbar fixed-top navbar-dark bg-primary">
    <a href="javascript:history.back();" class="nav-left"><img src="/assets/smartsail/img/svg/navbar-back.svg" alt="뒤로가기"/></a>
    <span class="navbar-title">공지사항</span>
    <%--<a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>--%>
</nav>
<!-- // Navigation -->

<!-- 탭메뉴 -->
<jsp:include page="cs_tab.jsp" />
<!--// 탭메뉴 -->

<%
    NoticeDetailDto dto = (NoticeDetailDto) request.getAttribute("result");
    String date = dto.getDate().toString().substring(0, 10).replaceAll("-",".");
    String contents = dto.getContents().replaceAll("\n", "<br/>");
%>
<!-- 리스트 -->
<div class="container nopadding">
    <div class="pt-0">
        <hr class="full mt-0 mb-3"/>
        <a >
            <div class="row no-gutters align-items-center">
                <div class="col-12 text-center">
                    <h5 class="mb-1"><strong class="text-primary">[${result.channelType}]</strong> ${result.title}</h5>
                    <small class="grey"><%=date%></small>
                </div>
            </div>
        </a>
        <hr class="full mt-3 mb-3"/>
        <div class="row no-gutters align-items-center">
            <div class="col-12 pl-2">
                <%=contents%>
            </div>
        </div>
        <hr class="full mt-3 mb-3"/>
    </div>
</div>
<!--// 리스트 -->

<!-- 하단버튼 -->
<div class="fixed-bottom">
    <div class="row no-gutters">
        <div class="col-12"><a href="/boarding/csnotice" class="btn btn-primary btn-lg btn-block">목록</a></div>
    </div>
</div>
<!--// 하단버튼 -->

<jsp:include page="cmm_foot.jsp" />
<script>

</script>
</body>
</html>
