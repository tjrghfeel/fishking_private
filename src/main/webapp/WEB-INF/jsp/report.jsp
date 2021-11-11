<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="cmm_head.jsp" />
<body>
<!-- Navigation -->
<nav class="navbar fixed-top navbar-dark bg-primary">
    <a href="javascript:history.back();" class="nav-left"><img src="/assets/smartsail/img/svg/navbar-back.svg" alt="뒤로가기"/></a>
    <span class="navbar-title">출항신고서</span>
    <%--<a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>--%>
</nav>
<!-- // Navigation -->

<!-- 탭메뉴 -->
<%--<jsp:include page="cmm_tab.jsp" />--%>
<!--// 탭메뉴 -->

<!--  Filter -->
<div class="filterlinewrap container nopadding">
    <div class="mt-3 mb-3" style="text-align: center">
        <div><b>선박명 (상품명) 출항신고</b></div>
        <div><b>출항시각: 2021-11-10 10:00</b></div>
        <div><b>입항예정: 2021-11-10 22:00</b></div>
        <div>승선인원: <b>1명</b> (선장 1명, 선원 0명 포함)</div>
    </div>
    <div id="container">
    </div>
</div>
<!-- /end Filter -->

<!-- 하단버튼 -->
<div class="fixed-bottom">
    <div class="row no-gutters">
        <div class="col-12"><a id="sendBtn" class="btn btn-primary btn-lg btn-block">신고서 전송</a></div>
    </div>
</div>
<!--// 하단버튼 -->

<jsp:include page="cmm_foot.jsp" />

<script>
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
            if (status == "미승선") continue
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
                                            <a class="btn btn-primary btn-lg btn-block rounded h-auto" disabled="">' + status + '</a> \
                                        </div> \
                                    </div> \
                                </div> \
                            </div> \
                        ');
            $(tags).data('item', item);
            $(container).append(tags);
          }
          $('#sendBtn').click(function () {
            $.ajax('/v2/api/sail/report/send', {
              method: 'POST',
              dataType: 'json',
              data: JSON.stringify({
                goodsId: goodsId,
                date: date,
              }),
              beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
                xhr.setRequestHeader('Content-Type', 'application/json');
              },
              complete: function(jqXHR) {
                if (jqXHR.readyState == 4) {
                  window.location.href = '/boarding/sail'
                } else {
                  alert("잠시 후에 다시 시도해주세요")
                }
              }
            })
          })
          pending = false;
        }
      }
    });
  }
  $(document).ready(function () {
    fn_loadPageData();
  });
</script>