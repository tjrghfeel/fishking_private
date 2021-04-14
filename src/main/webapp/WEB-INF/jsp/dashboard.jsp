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
    <span class="navbar-title"><img src="/assets/smartsail/img/svg/navbar-logo-smartship.svg" alt="스마트승선"/></span>
    <a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>
</nav>
<!-- // Navigation -->

<!-- 탭메뉴 -->
<nav class="nav nav-pills nav-bar nav-justified">
    <a class="nav-link active" href="dashboard.html"><figure class="dashboard"></figure>대시보드</a>
    <a class="nav-link" href="sail.html"><figure class="sail"></figure>승선관리</a>
    <a class="nav-link" href="camera.html"><figure class="camera"></figure>카메라관리</a>
    <a class="nav-link" href="cs-notice.html"><figure class="cs"></figure>고객센터</a>
</nav>
<!--// 탭메뉴 -->

<!-- 안내 -->
<div class="container nopadding mt-2">
    <h5>금일승선현황</h5>
    <div class="card-round-box-grey text-center">
        <div class="row mt-3 mb-3 d-flex align-items-center">
            <div class="col-6">
                <p class="mt-2 mb-2">
                    <canvas id="chart" width="164" height="106"></canvas>
<%--                    <img src="/assets/smartsail/img/svg/chart-b.svg" alt=""/>--%>
                </p>
            </div>
            <div class="col-6 text-left">
                <p><a href="sail.html"><small class="grey">승선대기</small> : <strong class="large" id="data-waitCount">13</strong></a></p>
                <p><a href="sail.html"><small class="grey">승선확인</small> : <strong class="large text-primary" id="data-confirmCount">87</strong></a></p>
                <p><a href="sail.html"><small class="grey">확인실패</small> : <strong class="large orange" id="data-failCount">87</strong></a></p>
                <p><a href="sail.html"><small class="grey">승선취소</small> : <strong class="large text-danger" id="data-cancelCount">13</strong></a></p>
            </div>
        </div>
    </div>
</div>
<!--// 안내 -->
<p class="space mt-2"></p>
<div class="container nopadding mt-2">
    <div class="row mt-1 d-flex align-items-center">
        <div class="col-4">
            <h5>금일 승선 대기자</h5>
        </div>
        <div class="col-8 text-right">
            <div class="custom-control custom-radio custom-control-inline">
                <input checked type="radio" id="customRadioInline1" name="customRadioInline1" class="custom-control-input" onclick="javascript:loadPageData('new');">
                <label class="custom-control-label" for="customRadioInline1">최신순</label>
            </div>
            <div class="custom-control custom-radio custom-control-inline">
                <input type="radio" id="customRadioInline2" name="customRadioInline1" class="custom-control-input" onclick="javascript:loadPageData('shipName');">
                <label class="custom-control-label" for="customRadioInline2">선상명순</label>
            </div>
            <div class="custom-control custom-radio custom-control-inline">
                <input type="radio" id="customRadioInline3" name="customRadioInline1" class="custom-control-input" onclick="javascript:loadPageData('username');">
                <label class="custom-control-label" for="customRadioInline3">승선자순</label>
            </div>
        </div>
    </div>
</div>

<!-- 예약 -->
<div class="container nopadding mt-2" id="list-template" style="display:none;">
    <div class="card-round-grey">
        <div class="card card-sm">
            <div class="row no-gutters d-flex align-items-center">
                <div class="col-2 cardProfileWrap text-center">
                    <img src="/assets/smartsail/img/sample/profile5.jpg" class="profile-thumb-md align-self-center mb-1" alt="profile"><br/><strong name="data-username">김새론</strong>
                </div>
                <div class="col-6">
                    <a href="sail-detail.html">
                        <p>
                            선상명:    <strong class="large" name="data-shipName">챔피언 1호</strong><br/>
                            상품명:    <strong class="large text-info" name="data-goodsName">우럭(오전)</strong><br/>
                            연락처:    <span name="data-phone">010-1234-5678</span>
                        </p>
                    </a>
                </div>
                <div class="col-4 text-right">
                    <a name="data-click" class="btn btn-round btn-dark"><img src="/assets/smartsail/img/svg/icon-jimun.svg" class="vam">지문입력</a>
                </div>
            </div>
            <hr class="mt-1 mb-1"/>
            <div class="row no-gutters">
                <div class="col-12 padding-sm text-center"><small><strong>방문: <strong class="large text-primary" name="data-visitCount">2</strong>회</strong></small> <small class="grey">&nbsp;/&nbsp;</small><small><strong name="data-fingerType">지문 : 오른손 엄지</strong></small></div>
            </div>
        </div>
    </div>
</div>
<!--// 예약 -->
<p class="clearfix"><br/></p>

<!-- Modal 예약승인 -->
<div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-center" id="confirmModalLabel">예약승인</h5>
            </div>
            <div class="modal-body text-center">
                <p>예약승인 하시겠습니까?<br/>예약승인시 예약완료 상태로 변경됩니다.</p>
            </div>
            <div class="modal-footer-btm">
                <div class="row no-gutters">
                    <div class="col-6"><a href="#none" class="btn btn-primary btn-lg btn-block" data-dismiss="modal">확인</a></div>
                    <div class="col-6"><a href="#none" class="btn btn-third btn-lg btn-block" data-dismiss="modal">닫기</a></div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--// Modal 예약승인 -->

<jsp:include page="cmm_foot.jsp" />
<script>
    // ----- > 지문 입력
    function fn_fingerprint (item, index) {
        var data = pageData['boardingPeople'][index];
        console.log(JSON.stringify(data));
    }
    var pageData = null;
    // ----- > 데이터 로드
    function fn_loadPageData (orderBy) {
        $.ajax('/v2/api/sail/dashboard', {
            method: 'GET',
            dataType: 'json',
            data: {
                orderBy: orderBy || 'new'
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
            },
            success: function (response) {
                pageData = response;

                document.querySelector('#data-waitCount').textContent = response['status']['waitCount'];
                document.querySelector('#data-confirmCount').textContent = response['status']['confirmCount'];
                document.querySelector('#data-failCount').textContent = response['status']['failCount'];
                document.querySelector('#data-cancelCount').textContent = response['status']['cancelCount'];

                var failPercentage = 10; //response['status']['failPercentage'];
                var confirmPercentage = 20;// response['status']['confirmPercentage'];
                var ctx = document.getElementById('chart').getContext('2d');
                var chart = new Chart(ctx, {
                    type: 'pie',
                    data: {
                        display: false,
                        labels: ['확인비율', '실패비율'],
                        datasets: [{
                            label: '',
                            data: [confirmPercentage, failPercentage],
                            backgroundColor: [
                                'rgba(255, 99, 132, 0.2)',
                                'rgba(54, 162, 235, 0.2)'
                            ],
                            borderColor: [
                                'rgba(255, 99, 132, 1)',
                                'rgba(54, 162, 235, 1)'
                            ],
                            hoverOffset: 4
                        }],
                        display:false
                    }
                });

                for (var i = 0; i < response['boardingPeople'].length; i++) {
                    var item = response['boardingPeople'][i] || {};
                    var clone = document.querySelector('#list-template').cloneNode(true);
                    clone.id = i;
                    clone.style.display = 'block';
                    clone.querySelector('[name="data-username"]').textContent = item['username'];
                    clone.querySelector('[name="data-shipName"]').textContent = item['shipName'];
                    clone.querySelector('[name="data-goodsName"]').textContent = item['goodsName'];
                    clone.querySelector('[name="data-phone"]').textContent = item['phone'];
                    clone.querySelector('[name="data-visitCount"]').textContent = item['visitCount'] || 0;
                    clone.querySelector('[name="data-fingerType"]').textContent = '지문 : ' + (item['fingerType'] || '');
                    clone.querySelector('[name="data-click"]').setAttribute('data-index', i);
                    clone.querySelector('[name="data-click"]').addEventListener('click', function () {
                        fn_fingerprint(item, this.getAttribute('data-index'));
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
