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
    <span class="navbar-title">알림 설정</span>
</nav>
<!-- // Navigation -->

<!-- 입력 -->
<div class="container nopadding mt-3">
    <div class="row no-gutters mt-3 mb-2">
        <div class="col">
            <strong>Wi-Fi 에서만 동영상 스트리밍</strong><br/>
            <small class="grey">Wi-Fi 에서만 동영상이 재생됩니다.</small>
        </div>
        <div class="col">
            <nav>
                <div class="nav nav-tabs btn-set" id="nav-tab" role="tablist">
                    <a class="nav-link active btn btn-on" id="nav-home-tab" data-toggle="tab" href="#nav-on" role="tab" aria-controls="nav-on" aria-selected="true">ON</a>
                    <a class="nav-link btn btn-off" id="nav-profile-tab" data-toggle="tab" href="#nav-off" role="tab" aria-controls="nav-off" aria-selected="false">OFF</a>
                </div>
            </nav>
        </div>
    </div>
    <p class="space"></p>
</div>
<!--// 입력 -->

<jsp:include page="cmm_foot.jsp" />

</body>
</html>
