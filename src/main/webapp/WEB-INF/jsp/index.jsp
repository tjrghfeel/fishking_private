<%--
  Created by IntelliJ IDEA.
  User: mac
  Date: 2021/04/12
  Time: 4:30 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="cmm_head.jsp" />
<body>
<!-- Navigation -->
<nav class="navbar fixed-top navbar-dark">
<%--    <a href="javascript:history.back();" class="nav-left" style="width: 44px; height: 40px; padding-top: 7px; top: 2px; left: 4px;"><img src="/assets/smartsail/img/svg/navbar-back-black.svg" alt="뒤로가기"/></a>--%>
    <span class="navbar-title"></span>
</nav>
<!-- // Navigation -->

<!-- 정보 -->
<div class="container nopadding mt-1">
    <h5 class="text-center">
        <img src="/assets/smartsail/img/svg/logo.svg" alt=""/>
    </h5>
</div>
<!--// 정보 -->

<!-- 입력 -->
<div class="container nopadding">
    <div class="mt-4">
        <form class="form-line mt-	1">
            <div class="form-group">
                <label for="memberId" class="sr-only">이메일</label>
                <input type="email" class="form-control" id="memberId" placeholder="이메일" value="">
            </div>
            <div class="form-group">
                <label for="password" class="sr-only">비밀번호</label>
                <input type="password" class="form-control" id="password" placeholder="비밀번호 (영문/숫자/특수문자 조합, 8~15자 이내)" value="">
            </div>
        </form>
        <a class="btn btn-primary btn-lg btn-block" onclick="javascript:fn_login();">로그인</a>
        <p class="text-center mt-3"><a href="findpw.html"><small class="grey">비밀번호를 잊으셨나요?</small></a></p>

        <p class="text-center mt-4">스마트승선 로그인/업체등록시  <br/><a href="policy-terms.html" class="text-primary">이용약관</a> 및 <a href="policy-privacy.html" class="text-primary">개인정보취급방침</a>에 동의하게 됩니다.</small></a></p>
    </div>
    <p class="clearfix"><br/><br/></p>
</div>
<!--// 입력 -->


<!-- 하단버튼 -->
<div class="fixed-bottom">
    <div class="row no-gutters">
        <!-- <div class="col-12"> 비활성버튼 -->
        <div class="col-12"></div>
    </div>
</div>
<!--// 하단버튼 -->

<jsp:include page="cmm_foot.jsp" />
<script>
    // ----- > 스마트승선 로그인
    function fn_login() {
        var memberId = $('#memberId').val();
        var password = $('#password').val();
        $.ajax(
            '/v2/api/smartsail/login',
            {
                method: 'POST',
                contentType: 'application/json;charset=utf-8',
                dataType: 'text',
                data: JSON.stringify({
                    memberId: memberId,
                    password: password
                }),
                success: function (response) {
                    localStorage.setItem('@accessToken', response);
                    window.location.href = '/boarding/dashboard';
                },
                error: function (xhr, status, err) {
                    alert('ID/PW 를 확인해주세요.');
                },
            }
        )
    }
    $(document).ready(function () {
        var accessToken = localStorage.getItem('@accessToken') || null;
        if (accessToken != null) {
            window.location.href = '/boarding/dashboard';
        }
    });
</script>
</body>
</html>
