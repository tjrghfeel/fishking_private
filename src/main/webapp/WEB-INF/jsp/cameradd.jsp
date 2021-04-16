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
    <span class="navbar-title">카메라설정</span>
</nav>
<!-- // Navigation -->


<!-- 상품타이틀 -->
<div class="container nopadding">
    <div class="card mt-3">
        <h4 class="text-center data-title"></h4>
    </div>
</div>
<!--// 상품타이틀 -->
<p class="space mt-2"></p>

<!-- 리스트 -->
<div class="container nopadding mt-3 card card-sm" id="list-template" style="display:none;">
    <div class="row no-gutters mt-3 mb-2">
        <div class="col-4 pl-3">
            <div class="cardimgWrap">
                <img src="/assets/smartsail/img/sample/boat1.jpg" class="img-fluid data-image" alt="">
            </div>
        </div>
        <div class="col-5">
            <h6 class="data-name">카메라 우측</h6>
<%--            <p><span class="grey">조회수  4,321<br/>27명 시청중</span></p>--%>
        </div>
        <div class="col-3 text-center">
            <nav>
                <div class="nav nav-tabs btn-set mt-3 mr-3 vam" id="nav-tab" role="tablist">
                    <a class="nav-link active btn btn-on data-isUse-on" id="nav-home-tab" data-toggle="tab" role="tab" aria-controls="nav-on" aria-selected="true">ON</a>
                    <a class="nav-link btn btn-off data-isUse-off" id="nav-profile-tab" data-toggle="tab" role="tab" aria-controls="nav-off" aria-selected="false">OFF</a>
                </div>
            </nav>
        </div>
    </div>
    <hr class="full mt-2 mb-3"/>
</div>
<!--// 리스트 -->


<!-- 하단버튼 -->
<div class="fixed-bottom">
    <div class="row no-gutters">
        <!-- <div class="col-12"><a href="#none" class="btn btn-grey btn-lg btn-block">확인</a></div> 비활성버튼 -->
        <div class="col-12"><a href="camera.html" class="btn btn-primary btn-lg btn-block">확인</a></div>
    </div>
</div>
<!--// 하단버튼 -->

<jsp:include page="cmm_foot.jsp" />
<script>
    var list = [];
    $(document).ready(function () {
        var query = new URLSearchParams(window.location.search);
        var shipId = query.get('shipId');
        $.ajax('/v2/api/sail/camera', {
            method: 'GET',
            dataType: 'json',
            data: {
                shipId: shipId
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
            },
            success: function (response) {
                console.log(JSON.stringify(response));

                document.querySelector('.data-title').innerHTML = response['shipName'] + ' <br/> <strong class="large orange">' + Intl.NumberFormat().format(response['count'] || 0) + '</strong> <small class="grey">대의 카메라 제어중</small>';
                for (var i = 0; i < response.content.length; i++) {
                    var item = response.content[i];
                    var clone = document.querySelector('#list-template').cloneNode(true);
                    clone.id = i;
                    clone.style.display = 'block';
                    clone.querySelector('.data-image').src = item['image'];
                    clone.querySelector('.data-name').textContent = item['name'];
                    document.body.appendChild(clone);
                    console.log(JSON.stringify(item))
                }
            }
        })
    });
</script>
</body>
</html>
