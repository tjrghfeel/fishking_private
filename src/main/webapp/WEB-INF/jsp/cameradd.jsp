<%--
  Created by IntelliJ IDEA.
  User: jinheelee
  Date: 2021/04/13
  Time: 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="cmm_head.jsp" />

<body>
<!-- Navigation -->
<nav class="navbar fixed-top navbar-dark bg-primary">
    <a href="javascript:history.back();" class="nav-left" style="width: 44px; height: 40px; padding-top: 7px; top: 2px; left: 4px;"><img src="/assets/smartsail/img/svg/navbar-back.svg" alt="뒤로가기"/></a>
    <span class="navbar-title">카메라설정</span>
</nav>
<!-- // Navigation -->


<!-- 상품타이틀 -->
<div class="container nopadding">
    <div class="card mt-3">
        <h4 class="text-center">${result.shipName} <br/> <strong class="large orange">${result.count}</strong> <small class="grey">대의 카메라 제어중</small></h4>
    </div>
</div>
<!--// 상품타이틀 -->
<p class="space mt-2"></p>

<!-- 리스트 -->
<c:forEach var="item" items="${result.content}">
    <div class="container nopadding  card card-sm">
        <div class="row no-gutters ">
<%--            <div class="col-4 pl-3">--%>
<%--                <div class="cardimgWrap">--%>
<%--                    <img src="${item.image}" class="img-fluid" alt="">--%>
<%--                </div>--%>
<%--            </div>--%>
<%--            <div class="col-5">--%>
<%--                <h6>${item.name}</h6>--%>
<%--&lt;%&ndash;                <p><span class="grey">조회수  4,321<br/>27명 시청중</span></p>&ndash;%&gt;--%>
<%--            </div>--%>
<%--            <div class="col-3 text-center">--%>
<%--                <nav>--%>
<%--                    <div class="nav nav-tabs btn-set mt-3 mr-3 vam" id="nav-tab" role="tablist">--%>
<%--                        <a onclick="javascript:fn_updateCamera('${item.cameraId}', true);" class="nav-link btn btn-on ${item.isUse? 'active' : ''}" id="nav-home-tab" data-toggle="tab" role="tab" aria-controls="nav-on" aria-selected="true">ON</a>--%>
<%--                        <a onclick="javascript:fn_updateCamera('${item.cameraId}', false);" class="nav-link btn btn-off ${!item.isUse? 'active' : ''}" id="nav-profile-tab" data-toggle="tab" role="tab" aria-controls="nav-off" aria-selected="false">OFF</a>--%>
<%--                    </div>--%>
<%--                </nav>--%>
<%--            </div>--%>
            <div class="cardimgWrap">
                <img src="${item.image}" class="img-fluid data-profileImage" alt="">
            </div>
            <div class="cardInfoWrap">
                <div class="card-body pt-0 h-100">
                    <div class="row no-gutters d-flex align-items-center h-100">
                        <div class="col-7">
                            <h6>${item.name}</h6>
                                <%--                <p><span class="grey">조회수  4,321<br/>27명 시청중</span></p>--%>
                        </div>
                        <div class="col-5 text-center">
                            <nav>
                                <div class="nav nav-tabs btn-set vam" id="nav-tab" role="tablist" style="margin-top: -15px;">
                                    <a onclick="javascript:fn_updateCamera('${item.cameraId}', true);" class="nav-link btn btn-on ${item.isUse? 'active' : ''}" id="nav-home-tab" data-toggle="tab" role="tab" aria-controls="nav-on" aria-selected="true">ON</a>
                                    <a onclick="javascript:fn_updateCamera('${item.cameraId}', false);" class="nav-link btn btn-off ${!item.isUse? 'active' : ''}" id="nav-profile-tab" data-toggle="tab" role="tab" aria-controls="nav-off" aria-selected="false">OFF</a>
                                </div>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <hr class="full mt-2 mb-3"/>
    </div>
</c:forEach>
<!--// 리스트 -->


<!-- 하단버튼 -->
<%--<div class="fixed-bottom">--%>
<%--    <div class="row no-gutters">--%>
<%--        <!-- <div class="col-12"><a href="#none" class="btn btn-grey btn-lg btn-block">확인</a></div> 비활성버튼 -->--%>
<%--        <div class="col-12"><a href="camera.html" class="btn btn-primary btn-lg btn-block">확인</a></div>--%>
<%--    </div>--%>
<%--</div>--%>
<!--// 하단버튼 -->

<jsp:include page="cmm_foot.jsp" />
<script>
    function fn_updateCamera (cameraId, isUse) {
        $.ajax('/v2/api/sail/camera/update', {
            method: 'POST',
            dataType: 'json',
            data: JSON.stringify({
                cameraId: cameraId,
                isUse: isUse
            }),
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
                xhr.setRequestHeader("Content-Type", "application/json;charset=utf-8");
            },
            success: function (response) {
                console.log(JSON.stringify(response));
            }
        })
    }
</script>
</body>
</html>
