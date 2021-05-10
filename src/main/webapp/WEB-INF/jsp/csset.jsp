<%--
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
    <span class="navbar-title">설정</span>
    <%--<a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>--%>
</nav>
<!-- // Navigation -->
<!-- 입력 -->
<div class="container nopadding mt-0">
    <div class="pt-0">
        <hr class="full mt-0 mb-3"/>
<%--        <hr class="full mt-3 mb-3"/>--%>
<%--        <a href="/boarding/cssetalarm">--%>
<%--            <div class="row no-gutters align-items-center">--%>
<%--                <div class="col-3 pl-2">알림설정</div>--%>
<%--                <div class="col-8 text-right"></div>--%>
<%--                <div class="col-1 text-right pl-1"><img src="/assets/smartsail/img/svg/cal-arrow-right.svg" alt=""/></div>--%>
<%--            </div>--%>
<%--        </a>--%>
<%--        <hr class="full mt-3 mb-3"/>--%>
<%--        <a>--%>
<%--            <div class="row no-gutters align-items-center">--%>
<%--                <div class="col-6 pl-2">접근 권한 설정</div>--%>
<%--                <div class="col-5 text-right"></div>--%>
<%--                <div class="col-1 text-right pl-1"><img src="/assets/smartsail/img/svg/cal-arrow-right.svg" alt=""/></div>--%>
<%--            </div>--%>
<%--        </a>--%>
<%--        <hr class="full mt-3 mb-3"/>--%>
<%--        <a href="/boarding/cssetvod">--%>
<%--            <div class="row no-gutters align-items-center">--%>
<%--                <div class="col-3 pl-2">동영상 설정</div>--%>
<%--                <div class="col-8 text-right"></div>--%>
<%--                <div class="col-1 text-right pl-1"><img src="/assets/smartsail/img/svg/cal-arrow-right.svg" alt=""/></div>--%>
<%--            </div>--%>
<%--        </a>--%>
        <hr class="full mt-3 mb-3"/>
        <a href="/boarding/cssetpolicy">
            <div class="row no-gutters align-items-center">
                <div class="col-3 pl-2">약관 및 정책</div>
                <div class="col-8 text-right"></div>
                <div class="col-1 text-right pl-1"><img src="/assets/smartsail/img/svg/cal-arrow-right.svg" alt=""/></div>
            </div>
        </a>
        <hr class="full mt-3 mb-3"/>
        <a onclick="javascript:fn_logout();">
            <div class="row no-gutters align-items-center">
                <div class="col-3 pl-2">로그아웃</div>
                <div class="col-8 text-right"></div>
                <div class="col-1 text-right pl-1"><img src="/assets/smartsail/img/svg/cal-arrow-right.svg" alt=""/></div>
            </div>
        </a>
        <hr class="full mt-3 mb-3"/>
        <a>
            <div class="row no-gutters align-items-center">
                <div class="col-3 pl-2">버전정보</div>
<%--                <div class="col-8 text-right"><strong>1.7.7</strong> &nbsp; <span class="status-icon status6">최신버전</span></div>--%>
                <div class="col-8 text-right"></div>
                <div class="col-1 text-right pl-1"><img src="/assets/smartsail/img/svg/cal-arrow-right.svg" alt=""/></div>
            </div>
        </a>
        <hr class="full mt-3 mb-3"/>
    </div>
</div>
<!--// 입력 -->

<jsp:include page="cmm_foot.jsp" />
<script>
  // ----- > 스마트승선 로그인
  function fn_logout() {
    if (confirm('로그아웃하시겠습니까?')) {
      localStorage.removeItem('@accessToken');
      window.location.href = '/boarding/index';
    }
  }
</script>

</body>
</html>
