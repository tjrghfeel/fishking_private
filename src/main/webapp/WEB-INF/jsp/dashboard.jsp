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
    <span class="navbar-title"><img src="/assets/smartsail/img/svg/navbar-logo-smartship.svg" alt="스마트승선"/></span>
<%--<a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>--%>
</nav>
<!-- // Navigation -->

<!-- 탭메뉴 -->
<jsp:include page="cmm_tab.jsp" />
<!--// 탭메뉴 -->

<!-- 안내 -->
<div class="container nopadding mt-2">
    <h5>금일승선현황</h5>
    <div class="card-round-box-grey text-center">
        <div class="row mt-3 mb-3 d-flex align-items-center justify-content-center">
<%--            <div class="col-6">--%>
<%--                <p class="mt-2 mb-2">--%>
<%--                    <canvas id="chart" width="164" height="106"></canvas>--%>
<%--                    <img src="/assets/smartsail/img/svg/chart-b.svg" alt=""/>--%>
<%--                </p>--%>
<%--            </div>--%>
            <div class="d-flex align-items-center flex-row align-items-center justify-content-around" style="width: 100%">
                <span ><a ><small class="grey">승선대기</small> : <strong class="large" id="data-waitCount">0</strong></a></span>
                <span ><a ><small class="grey">승선확인</small> : <strong class="large text-primary" id="data-confirmCount">0</strong></a></span>
                <span ><a ><small class="grey">확인실패</small> : <strong class="large orange" id="data-failCount">0</strong></a></span>
                <span ><a ><small class="grey">승선취소</small> : <strong class="large text-danger" id="data-cancelCount">0</strong></a></span>
            </div>
        </div>
    </div>
</div>
<!--// 안내 -->
<p class="space mt-2"></p>
<div class="container nopadding mt-2">
    <div class="row mt-1 d-flex align-items-center">
        <div class="col-5">
            <h5>금일 승선 대기자</h5>
        </div>
        <div class="col-7 text-right">
            <div class="custom-control custom-radio custom-control-inline mr-0">
                <input checked type="radio" id="customRadioInline1" name="customRadioInline1" class="custom-control-input" onclick="javascript:fn_loadPageData('new');">
                <label class="custom-control-label" for="customRadioInline1">최신</label>
            </div>
            <div class="custom-control custom-radio custom-control-inline mr-0">
                <input type="radio" id="customRadioInline2" name="customRadioInline1" class="custom-control-input" onclick="javascript:fn_loadPageData('shipName');">
                <label class="custom-control-label" for="customRadioInline2">선명</label>
            </div>
            <div class="custom-control custom-radio custom-control-inline mr-0">
                <input type="radio" id="customRadioInline3" name="customRadioInline1" class="custom-control-input" onclick="javascript:fn_loadPageData('username');">
                <label class="custom-control-label" for="customRadioInline3">승선자</label>
            </div>
        </div>
    </div>
</div>

<!-- 예약 -->
<div class="container nopadding mt-2" id="list-template" style="display: none;">
    <div class="card-round-grey">
        <div class="card card-sm"  style="margin-top: 0">
            <div class="row no-gutters d-flex align-items-center">
                <div class="col-6">
                    <a>
                        <p>
                            승선자:    <strong class="" name="data-username">챔피언 1호</strong><br/>
                            선상명:    <strong class="" name="data-shipName">챔피언 1호</strong><br/>
                            상품명:    <strong class="text-info" name="data-goodsName">우럭(오전)</strong><br/>
                            연락처:    <span name="data-phone">010-1234-5678</span><br/>
                            비상시:    <span name="data-emergency">010-1234-5678</span><br/>
                            상태:     <span name="data-st">010-1234-5678</span>
                        </p>
                    </a>
                </div>
                <div class="col-6 text-right d-flex flex-column align-items-end justify-content-around">
                    <a name="data-click" class="btn btn-round btn-dark data-finger-confirm" style="padding: 8px 10px; margin: 4px 0;"><img src="/assets/smartsail/img/svg/icon-jimun.svg" class="vam">승선확인</a>
                    <a name="data-click" class="btn btn-round btn-dark data-finger-regist" style="padding: 8px 10px; margin: 4px 0;"><img src="/assets/smartsail/img/svg/icon-jimun.svg" class="vam">지문등록</a>
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
<%--<p class="clearfix"><br/></p>--%>

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
                    <div class="col-6"><a class="btn btn-primary btn-lg btn-block" data-dismiss="modal">확인</a></div>
                    <div class="col-6"><a class="btn btn-third btn-lg btn-block" data-dismiss="modal">닫기</a></div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--// Modal 예약승인 -->

<jsp:include page="cmm_foot.jsp" />
<script>
    // ----- > 승선 확인
    function fn_fingerprint_confirm (item, index) {
        var data = pageData['boardingPeople'][index];
        window.location.href = '/boarding/fingerprint?data=' + encodeURIComponent(JSON.stringify(data));
    }
    // ----- > 지문 등록
    function fn_fingerprint_regist (item, index) {
        var data = pageData['boardingPeople'][index];
        window.location.href = '/boarding/fingerprintregist?data=' + encodeURIComponent(JSON.stringify(data));
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
                // var ctx = document.getElementById('chart').getContext('2d');
                // var chart = new Chart(ctx, {
                //     type: 'pie',
                //     data: {
                //         display: false,
                //         labels: ['확인비율', '실패비율'],
                //         datasets: [{
                //             label: '',
                //             data: [confirmPercentage, failPercentage],
                //             backgroundColor: [
                //                 'rgba(255, 99, 132, 0.2)',
                //                 'rgba(54, 162, 235, 0.2)'
                //             ],
                //             borderColor: [
                //                 'rgba(255, 99, 132, 1)',
                //                 'rgba(54, 162, 235, 1)'
                //             ],
                //             hoverOffset: 4
                //         }],
                //         display:false
                //     }
                // });

                var items = document.querySelectorAll('[id^="list-item-"]');
                for (var i = 0; i < items.length; i++) {
                    items[i].remove();
                }
                for (var i = 0; i < response['boardingPeople'].length; i++) {
                    var item = response['boardingPeople'][i] || {};
                    var clone = document.querySelector('#list-template').cloneNode(true);
                    clone.id = 'list-item-' + i;
                    clone.style.display = 'block';
                    clone.querySelector('[name="data-username"]').textContent = item['username'];
                    clone.querySelector('[name="data-shipName"]').textContent = item['shipName'];
                    clone.querySelector('[name="data-goodsName"]').textContent = item['goodsName'] + ' (' + item['fishingStartTime'] + '~)';
                    clone.querySelector('[name="data-phone"]').textContent = item['phone'];
                    clone.querySelector('[name="data-emergency"]').textContent = item['emergencyPhone'];
                    clone.querySelector('[name="data-st"]').textContent = item['status'];
                    clone.querySelector('[name="data-visitCount"]').textContent = item['visitCount'] || 0;
                    clone.querySelector('[name="data-fingerType"]').textContent = '지문 : ' + (item['fingerType'] || '');
                    // ----- > 승선확인 이벤트
                    clone.querySelector('.data-finger-confirm').setAttribute('data-index', i);
                    clone.querySelector('.data-finger-confirm').addEventListener('click', function () {
                        fn_fingerprint_confirm(item, this.getAttribute('data-index'));
                    });
                    // ----- > 지문등록 이벤트
                    clone.querySelector('.data-finger-regist').setAttribute('data-index', i);
                    clone.querySelector('.data-finger-regist').addEventListener('click', function () {
                        fn_fingerprint_regist(item, this.getAttribute('data-index'));
                    });
                    // ----- > 최초 방문은 지문등록 버튼만 표시
                    if ((item['visitCount'] || 0) == 0) {
                        clone.querySelector('.data-finger-confirm').style.display = 'none';
                    }
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
