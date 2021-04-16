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
<%--    <a href="javascript:history.back();" class="nav-left"><img src="/assets/smartsail/img/svg/navbar-back.svg" alt="뒤로가기"/></a>--%>
    <span class="navbar-title">상품관리</span>
    <a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>
</nav>
<!-- // Navigation -->

<!-- 탭메뉴 -->
<jsp:include page="cmm_tab.jsp" />
<!--// 탭메뉴 -->

<!--  Filter -->
<div class="filterlinewrap container nopadding">
    <ul class="nav nav-tabs nav-filter">
        <li>
            <div class="input-group keyword">
                <select class="custom-select">
                    <option>선상명</option>
                </select>
                <input id="keyword" type="text" class="form-control" placeholder="검색어 입력" value="">
            </div><!-- /input-group -->
        </li>
        <li>
            <label for="hasVideo" class="sr-only">녹화영상</label>
            <select class="form-control" id="hasVideo">
                <option value="">녹화영상전체</option>
                <option value="Y">유</option>
                <option value="N">무</option>
            </select>
        </li>
        <li class="full">
            <p><a onclick="javascript:fn_loadPageData();" class="btn btn-primary btn-sm">검색</a><a onclick="javascript:fn_init();" class="btn btn-grey btn-sm">초기화</a></p>
        </li>
    </ul>
</div>
<!-- /end Filter -->
<p class="clearfix"></p>


<!-- 리스트 -->
<div class="container nopadding mt-3" id="list-template" style="display:none;">
    <a>
        <div class="card card-sm">
            <div class="row no-gutters">
                <div class="cardimgWrap">
                    <img src="/assets/smartsail/img/sample/boat1.jpg" class="img-fluid data-profileImage" alt="">
                </div>
                <div class="cardInfoWrap">
                    <div class="card-body pt-0">
                        <div class="row no-gutters d-flex align-items-center">
                            <div class="col-9">
                                <h6 class="data-shipName">어복황제3호</h6>
                                <p>
                                    <span class="grey">♡ <span class="data-takes"></span></span>
                                </p>
                            </div>
                            <div class="col-3 text-right">
                                <small class="grey">녹화영상:</small> <strong class="large red data-hasVideo">유</strong>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </a>
    <hr class="full mt-2 mb-3"/>
</div>
<!--// 리스트 -->


<jsp:include page="cmm_foot.jsp" />
<script>
    var data = [];
    function fn_init() {
        document.querySelector('#keyword').value = '';
        document.querySelector('#hasVideo').value = '';
    }
    function moveToDetail (item, index) {
        var detail = data[index];
        window.location.href = '/boarding/cameradd?shipId=' + detail['shipId'];
    }
    function fn_loadPageData() {
        var keyword = document.getElementById('keyword').value;
        var hasVideo = document.querySelector('#hasVideo').selectedOptions[0].value;
        if (keyword.length == 0) keyword = null;
        if (hasVideo.length == 0) hasVideo = null;

        $.ajax('/v2/api/sail/ships', {
            method: 'GET',
            dataType: 'json',
            data: {
                keyword: keyword,
                hasVideo: hasVideo
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
            },
            success: function (response) {
                console.log(JSON.stringify(response));
                data = response;
                for (var i = 0; i < response.length; i++) {
                    var item = response[i] || {};
                    var clone = document.querySelector('#list-template').cloneNode(true);
                    clone.id = i;
                    clone.style.display = 'block';
                    clone.querySelector('.data-profileImage').src = item['profileImage'];
                    clone.querySelector('.data-shipName').textContent = item['shipName'];
                    clone.querySelector('.data-takes').textContent = Intl.NumberFormat().format(item['takes'] || 0);
                    if (item['hasVideo']) {
                        clone.querySelector('.data-hasVideo').textContent = '유';
                    }else{
                        clone.querySelector('.data-hasVideo').textContent = '무';
                        clone.querySelector('.data-hasVideo').classList.remove('red');
                    }
                    clone.querySelector('a').setAttribute('data-index', i);
                    clone.querySelector('a').addEventListener('click', function () {
                        moveToDetail(item, this.getAttribute('data-index'));
                    });
                    document.body.appendChild(clone);
                }
            }
        });
    }
    $(document).ready(function () {
        fn_loadPageData();
    });
</script>
</body>
</html>
