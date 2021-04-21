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
    <span class="navbar-title">약관 및 정책</span>
</nav>
<!-- // Navigation -->
<!-- 입력 -->
<div class="container nopadding mt-0">
    <div class="pt-0">
        <hr class="full mt-0 mb-3"/>
        <a href="/boarding/cssetpolicyterms">
            <div class="row no-gutters align-items-center">
                <div class="col-6 pl-2">이용약관</div>
                <div class="col-5 text-right"></div>
                <div class="col-1 text-right pl-1"><img src="/assets/smartsail/img/svg/cal-arrow-right.svg" alt=""/></div>
            </div>
        </a>
        <hr class="full mt-3 mb-3"/>
        <a href="/boarding/cssetpolicyprivacy">
            <div class="row no-gutters align-items-center">
                <div class="col-6 pl-2">개인정보처리방침</div>
                <div class="col-5 text-right"></div>
                <div class="col-1 text-right pl-1"><img src="/assets/smartsail/img/svg/cal-arrow-right.svg" alt=""/></div>
            </div>
        </a>
        <hr class="full mt-3 mb-3"/>
        <a href="/boarding/cssetpolicycancel">
            <div class="row no-gutters align-items-center">
                <div class="col-6 pl-2">취소 및 환불 규정</div>
                <div class="col-5 text-right"></div>
                <div class="col-1 text-right pl-1"><img src="/assets/smartsail/img/svg/cal-arrow-right.svg" alt=""/></div>
            </div>
        </a>
        <hr class="full mt-3 mb-3"/>
        <a href="/boarding/cssetpolicylbs">
            <div class="row no-gutters align-items-center">
                <div class="col-6 pl-2">위치기반 서비스 이용약관</div>
                <div class="col-5 text-right"></div>
                <div class="col-1 text-right pl-1"><img src="/assets/smartsail/img/svg/cal-arrow-right.svg" alt=""/></div>
            </div>
        </a>
        <hr class="full mt-3 mb-3"/>
        <a href="/boarding/cssetpolicyagree">
            <div class="row no-gutters align-items-center">
                <div class="col-6 pl-2">개인정보 제 3자 제공 동의</div>
                <div class="col-5 text-right"></div>
                <div class="col-1 text-right pl-1"><img src="/assets/smartsail/img/svg/cal-arrow-right.svg" alt=""/></div>
            </div>
        </a>
        <hr class="full mt-3 mb-3"/>
    </div>
</div>
<!--// 입력 -->

<jsp:include page="cmm_foot.jsp" />

</body>
</html>
