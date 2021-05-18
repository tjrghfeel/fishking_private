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
    <span class="navbar-title">다른 손가락 선택</span>
    <%--<a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>--%>
</nav>
<!-- // Navigation -->
<p class="clearfix"></p>


<!-- 안내 -->
<div class="container nopadding mt-3">
    <h5>지문 등록을 하실 손가락을 선택해 주세요.</h5>
    <div class="card-round-grey mt-3">
        <div class="card card-sm">
            <div class="row no-gutters mt-5 mb-5 text-center">
                <div class="col-12">
                    <p class="clearfix"><br/></p>
                    <div class="text-center">
                        <div class="fingerWap">
                            <img src="/assets/smartsail/img/svg/finger.svg" alt=""/>
                            <a onclick="javascript:setFingerTypeNum(10);"><span id="num-10" class="finger-point" style="top:31px; left:5px;">10</span></a>
                            <a onclick="javascript:setFingerTypeNum(9);"><span id="num-9" class="finger-point" style="top:12px; left:26px;">9</span></a>
                            <a onclick="javascript:setFingerTypeNum(8);"><span id="num-8" class="finger-point" style="top:5px; left:53px;">8</span></a>
                            <a onclick="javascript:setFingerTypeNum(7);"><span id="num-7" class="finger-point" style="top:7px; left:81px;">7</span></a>
                            <a onclick="javascript:setFingerTypeNum(6);"><span id="num-6" class="finger-point" style="top:80px; left:107px;">6</span></a>

                            <a onclick="javascript:setFingerTypeNum(5);"><span id="num-5" class="finger-point" style="top:31px; left:245px;">5</span></a>
                            <a onclick="javascript:setFingerTypeNum(4);"><span id="num-4" class="finger-point" style="top:12px; left:221px;">4</span></a>
                            <a onclick="javascript:setFingerTypeNum(3);"><span id="num-3" class="finger-point" style="top:5px; left:193px;">3</span></a>
                            <a onclick="javascript:setFingerTypeNum(2);"><span id="num-2" class="finger-point active" style="top:7px; left:166px;">2</span></a>
                            <a onclick="javascript:setFingerTypeNum(1);"><span id="num-1" class="finger-point" style="top:80px; left:137px;">1</span></a>
                        </div>
                    </div>
                    <p class="clearfix"><br/><br/></p>
                </div>
            </div>
        </div>
    </div>
    <h6 class="text-danger mt-3">단, 이후 재방문 시 같은 손가락을 이용하여 등록하셔야 합니다. </h6>
</div>
<!--// 안내 -->


<!-- 하단버튼 -->
<div class="fixed-bottom">
    <div class="row no-gutters">
        <div class="col-12"><a onclick="javascript:start();" class="btn btn-primary btn-lg btn-block">선택 완료</a></div>
    </div>
</div>
<!--// 하단버튼 -->

<jsp:include page="cmm_foot.jsp" />
<script>
    var fingerTypeNum = 2;
    function setFingerTypeNum (num) {
        fingerTypeNum = num;
        var arr = document.querySelectorAll('.finger-point');
        for (var i = 0; i < arr.length; i++) {
            arr[i].classList.remove('active');
        }
        document.getElementById('num-' + num).classList.add('active');
    }
    // ----- > 지문 인식 시작
    function start () {
        // console.log('----- > 지문 인식 시작');
        if (confirm("지문 등록 시 바로 승선 확인 됩니다.")) Fishking.getFingerprints();
    }
    // ----- > 지문 인식 결과
    function setFingerprintData(fingerprint) {
        // console.log('----- > 지문 인식 결과 : ' + fingerprint);
        $.ajax('/v2/api/sail/fingerprint/add', {
            method: 'POST',
            dataType: 'json',
            data: JSON.stringify({
                riderId : data['riderId'],
                username: data['username'],
                phone: data['phone'],
                fingerprint: fingerprint,
                fingerTypeNum: fingerTypeNum
            }),
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
                xhr.setRequestHeader('Content-Type', 'application/json');
            },
            error: function () {
                alert('지문인식 등록이 실패하였습니다.\n지문입력을 다시 시도바랍니다.');
            },
            success: function (response) {
                console.log(JSON.stringify(response));
                alert('지문등록이 완료되었습니다.');
                window.location.href = '/boarding/dashboard';
            }
        });
    }
    var data = null;
    $(document).ready(function () {
        data = JSON.parse(decodeURIComponent(location.search.substr(6,location.search.length)));
    });
</script>

</body>
</html>
