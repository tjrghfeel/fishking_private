
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="cmm_head.jsp" />
<body>
<!-- Navigation -->
<nav class="navbar fixed-top navbar-dark bg-primary">
    <a href="javascript:history.back();" class="nav-left" style="width: 44px; height: 40px; padding-top: 7px; top: 2px; left: 4px;"><img src="/assets/smartsail/img/svg/navbar-back.svg" alt="뒤로가기"/></a>
    <span class="navbar-title">승선명부</span>
    <%--<a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>--%>
</nav>
<!-- // Navigation -->

<!-- 탭메뉴 -->
<%--<jsp:include page="cmm_tab.jsp" />--%>
<!--// 탭메뉴 -->

<div class="filterlinewrap container nopadding" id="container">
</div>

<a id="printbtn" class="add-circle" style="z-index: 50; bottom: 60px; right: 60px;">
    <img src="/assets/smartsail/img/svg/icon-note-white.svg" alt="" class="add-icon"/>
</a>
<a onclick="javascript:moveToAdd();" class="add-circle" style="z-index: 50; bottom: 60px;">
    <img src="/assets/smartsail/img/svg/icon-add-user.svg" alt="" class="add-icon"/>
</a>

<!-- 하단버튼 -->
<div class="fixed-bottom">
    <div class="row no-gutters">
        <div class="col-6"><a onclick="javascript:addSailor();" class="btn btn-secondary btn-lg btn-block">선원추가</a></div>
        <div class="col-6"><a onclick="javascript:writeReport();" class="btn btn-primary btn-lg btn-block">출항신고서 작성</a></div>
    </div>
</div>
<!--// 하단버튼 -->
<input id="canadd" type="hidden" />
<jsp:include page="reportprint.jsp" />
<jsp:include page="cmm_foot.jsp" />

<script>
  // 승선객등록
  function moveToAdd () {
    if ($('#canadd').val() == 'true') {
      location.href = '/boarding/sailadd2' + location.search;
    } else {
      alert('배의 최대 승선인원을 초과하여 추가할 수 없습니다.')
    }
  }
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
  function addSailor () {
    if ($('#canadd').val() == 'true') {
      location.href = '/boarding/sailoradd' + location.search;
    } else {
      alert('배의 최대 승선인원을 초과하여 추가할 수 없습니다.')
    }
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
          var rider = response['rider']
          var cap;
          var r = []
          $('#canadd').val(response['counts'] > rider.length)
          for (var i = 0; i < rider.length; i++) {
            var item = rider[i];
            var status = item['status']
            var c = ''
            if (status == '미승선') {
              c = 'background-color: #101010;'
            }
            if (status == "선장") {
              cap = item
            } else if (status == "선원" || status == "승선완료") {
              r.push(item)
            }
            var tags = $(' \
                            <div class="container nopadding mt-2" name="list-item" data-index="' + i + '"> \
                                <div class="card-round-grey"> \
                                    <div class="card card-sm flex-row align-items-center" style="margin: 0;"> \
                                        <div class="w-75"> \
                                            <p style="margin:0;line-height:120%;"><small>승선자명: </small>' + item['name'] + ' (' + item['phone'] + ')</p> \
                                            <p style="margin:0;line-height:120%;"><small>생년월일: </small>' + item['birthday'] + '</p> \
                                            <p style="margin:0;line-height:120%;"><small>주소: </small>' + item['address'] + '</p> \
                                        </div> \
                                        <div class="w-25"> \
                                            <a class="btn btn-primary btn-lg btn-block rounded h-auto" style="font-size: 12px; padding: 11px 10px;' + c + '" onclick="javascript:fn_fingerprint_confirm(this)">' + status + '</a> \
                                        </div> \
                                    </div> \
                                </div> \
                            </div> \
                        ');
            $(tags).data('item', item);
            $(container).append(tags);
          }

          $('#shipname2').text(response['shipName']);
          var dts = response['startTime'].split(' ')[0].split('-');
          $('#date').text(dts[0] + '년 ' + dts[1] + '월 ' + dts[2] + '일');
          $('#n-c').text(cap['name'])
          var b = cap['birthday'].substring(0,2) + '.' + cap['birthday'].substring(2,4) + '.' + cap['birthday'].substring(4,6)
          $('#b-c').text(b)
          $('#g-c').text(cap['sex'])
          $('#p-c').text(cap['phone'])
          $('#em-c').text(cap['emergencyPhone'])
          for (var i = 0; i < r.length; i++) {
            $('#n-'+String(i+1)).text(r[i]['name'])
            var b = r[i]['birthday'].substring(0,2) + '.' + r[i]['birthday'].substring(2,4) + '.' + r[i]['birthday'].substring(4,6)
            $('#b-'+String(i+1)).text(b)
            $('#g-'+String(i+1)).text(r[i]['sex'])
            $('#p-'+String(i+1)).text(r[i]['phone'])
            $('#em-'+String(i+1)).text(r[i]['emergencyPhone'])
            if (r[i]['status'] == '선원') $('#etc-'+String(i+1)).text('선원')
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