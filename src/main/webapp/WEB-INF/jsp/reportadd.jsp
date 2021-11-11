
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="cmm_head.jsp" />
<body>
<!-- Navigation -->
<nav class="navbar fixed-top navbar-dark bg-primary">
    <a href="javascript:history.back();" class="nav-left"><img src="/assets/smartsail/img/svg/navbar-back.svg" alt="뒤로가기"/></a>
    <span class="navbar-title">승선명부</span>
    <%--<a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>--%>
</nav>
<!-- // Navigation -->

<!-- 탭메뉴 -->
<%--<jsp:include page="cmm_tab.jsp" />--%>
<!--// 탭메뉴 -->

<div class="filterlinewrap container nopadding" id="container">
</div>

<!-- 하단버튼 -->
<div class="fixed-bottom">
    <div class="row no-gutters">
        <div class="col-6"><a onclick="javascript:moveToRegistOther();" class="btn btn-secondary btn-lg btn-block">선원추가</a></div>
        <div class="col-6"><a onclick="javascript:writeReport();" class="btn btn-primary btn-lg btn-block">출항신고서 작성</a></div>
    </div>
</div>
<!--// 하단버튼 -->

<jsp:include page="cmm_foot.jsp" />

<script>
  // ----- > 승선 확인
  function fn_fingerprint_confirm (element) {
    var item = $(element.parentNode.parentNode.parentNode.parentNode).data('item');
    if (item['status'] == '선장' || item['status'] == '선원') {
      return
    }
    var data = {
      riderId: item['riderId'],
      username: item['name'],
      phone: item['phone'],
      fingerTypeNum: 1
    }
    window.location.href = '/boarding/fingerprint?data=' + encodeURIComponent(JSON.stringify(data));
  }
  function writeReport () {
    location.href = '/boarding/report' + location.search;
  }
  function fn_loadPageData () {
    pending = true;
    var goodsId = location.search.split("&")[0].split("=")[1]
    var date = location.search.split("&")[1].split("=")[1]

    $.ajax('/v2/api/sail/report/detail', {
      method: 'GET',
      dataType: 'json',
      data: {
        goodsId: goodsId,
        date: date,
      },
      beforeSend: function (xhr) {
        xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
      },
      success: function (response, status, xhr) {
        if (xhr.status == 204) {
          alert('조회된 데이터가 없습니다.');
        } else {
          var container = $('#container');
          for (var i = 0; i < response.length; i++) {
            var item = response[i];
            var status = item['status']
            var tags = $(' \
                            <div class="container nopadding mt-2" name="list-item" data-index="' + i + '"> \
                                <div class="card-round-grey"> \
                                    <div class="card card-sm flex-row align-items-center" style="margin: 0;"> \
                                        <div class="w-75"> \
                                            <p><small>승선자명: </small>' + item['name'] + '</p> \
                                            <p><small>휴대전화: </small>' + item['phone'] + '</p> \
                                            <p><small>비상연락: </small>' + item['emergencyPhone'] + '</p> \
                                            <p><small>생년월일: </small>' + item['birthday'] + '</p> \
                                            <p><small>주소: </small>' + item['address'] + '</p> \
                                        </div> \
                                        <div class="w-25"> \
                                            <a class="btn btn-primary btn-lg btn-block rounded h-auto" onclick="javascript:fn_fingerprint_confirm(this)">' + status + '</a> \
                                        </div> \
                                    </div> \
                                </div> \
                            </div> \
                        ');
            $(tags).data('item', item);
            $(container).append(tags);
          }
          pending = false;
        }
      }
    });
  }
  $(document).ready(function () {
    fn_loadPageData();
  });
</script>