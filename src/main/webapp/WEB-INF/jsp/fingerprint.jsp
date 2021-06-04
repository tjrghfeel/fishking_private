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
    <span class="navbar-title">지문인식</span>
    <%--<a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>--%>
</nav>
<!-- // Navigation -->
<p class="clearfix"></p>


<!-- 안내 -->
<div class="container nopadding mt-3">
    <h2>
        <small>잠깐!!!</small><br/>
        <strong class="red" id="txt-finger-type">오른손 엄지</strong><small id="txt-finger-action">를 이용해 주십시오.</small>
    </h2>
    <div class="card-round-grey mt-4">
        <div class="card card-sm">
            <div class="row no-gutters mt-5 mb-5 text-center">
                <div class="col-12">
                    <p class="clearfix"><br/></p>
                    <img src="/assets/smartsail/img/svg/fingerprint.svg" class="img-fluid" alt="">
                    <p class="clearfix"><br/><br/></p>
                </div>
            </div>
        </div>
    </div>
</div>
<!--// 안내 -->


<!-- 하단버튼 -->
<div class="fixed-bottom">
    <div class="row no-gutters">
        <div class="col-6"><a onclick="javascript:start();" class="btn btn-primary btn-lg btn-block">지문 인식 시작</a></div>
        <div class="col-6"><a onclick="javascript:moveToRegistOther();" class="btn btn-secondary btn-lg btn-block">다른 손가락으로 등록</a></div>
    </div>
</div>
<!--// 하단버튼 -->

<div id="complexConfirm" style="display: none">
    등록했던 지문과 일치하지 않습니다. <br/>
    기존에 등록했던 손가락으로 지문인식을 시도해 주시고, 그럴 수 없는 상황이라면 다른 손가락으로 등록을 선택해서 신규 지문으로 등록해 주세요.
</div>

<jsp:include page="cmm_foot.jsp" />
<script>
    function moveToRegistOther () {
        window.location.href = '/boarding/fingerprintother?data=' + encodeURIComponent(JSON.stringify(data));
    }

    function failConfirm() {
    $("#complexConfirm").dialog({
      modal: true,
      resizeable : false,
      buttons: {
        "재시도": function() { $(this).dialog('close'); },
        "다른 손가락 등록": function() {
          $(this).dialog('close');
          window.location.href = '/boarding/fingerprintregist?data=' + location.search.substr(6,location.search.length);
        },
      }
    });
    $('.ui-dialog-titlebar-close').hide()
    }

    var data = null;
    // ----- > 지문 인식 시작
    function start () {
        // console.log('----- > 지문 인식 시작');
        Fishking.getFingerprints();
    }
    // ----- > 지문 인식 결과
    function setFingerprintData(fingerprint) {
        data = JSON.parse(decodeURIComponent(location.search.substr(6,location.search.length)));
        // console.log('----- > 지문 인식 결과 : ' + fingerprint);
        if ((fingerprint || '').length == 0) {
            alert('승선확인이 실패하였습니다.\n지문입력을 다시 시도바랍니다.');
        }else{
            $.ajax('/v2/api/sail/fingerprint/check', {
                method: 'POST',
                dataType: 'json',
                data: JSON.stringify({
                    riderId : data['riderId'],
                    username: data['username'],
                    phone: data['phone'],
                    fingerprint: fingerprint,
                    fingerTypeNum: data['fingerTypeNum']
                }),
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
                    xhr.setRequestHeader('Content-Type', 'application/json');
                },
                error: function () {
                    alert('승선확인이 실패하였습니다.\n지문입력을 다시 시도바랍니다.');
                },
                success: function (response) {
                    // console.log(JSON.stringify(response));
                    if (response['status'] === 'success') {
                      alert('승선확인이 완료되었습니다.');
                      window.location.href = '/boarding/dashboard';
                    } else {
                      failConfirm()
                    }
                }
            })
        }
    }
    $(document).ready(function () {
        data = JSON.parse(decodeURIComponent(location.search.substr(6,location.search.length)));
        if (data['fingerType']) {
          document.getElementById('txt-finger-type').textContent = data['fingerType'];
          document.getElementById('txt-finger-action').textContent = '를 이용해 주십시오.';
        } else {
          document.getElementById('txt-finger-type').textContent = '오른손 엄지';
          document.getElementById('txt-finger-action').textContent = '를 권장합니다.';
        }
    });
</script>

</body>
</html>
