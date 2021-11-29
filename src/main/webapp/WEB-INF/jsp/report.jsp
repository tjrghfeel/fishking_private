<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="cmm_head.jsp" />
<body>
<!-- Navigation -->
<nav class="navbar fixed-top navbar-dark bg-primary">
    <a href="javascript:history.back();" class="nav-left" style="width: 44px; height: 40px; padding-top: 7px; top: 2px; left: 4px;"><img src="/assets/smartsail/img/svg/navbar-back.svg" alt="뒤로가기"/></a>
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
        <div><b id="shipname"></b></div>
        <div><b>출항시각: <span id="starttime"></span></b></div>
        <div><b>입항예정: <span id="endtime"></span></b></div>
        <div>승선인원: <span id="countStr"></span></div>
    </div>
    <div id="container">
    </div>
</div>
<!-- /end Filter -->

<a id="printbtn" class="add-circle" style="z-index: 50; bottom: 60px;">
    <img src="/assets/smartsail/img/svg/icon-note-white.svg" alt="" class="add-icon"/>
</a>
<!-- 하단버튼 -->
<div class="fixed-bottom" id="btm-btn">
    <div class="row no-gutters">
        <div class="col-12"><a id="sendBtn" class="btn btn-primary btn-lg btn-block">신고서 전송</a></div>
    </div>
</div>
<!--// 하단버튼 -->
<div id="mask" style="z-index: 1500; background-color: rgba(0,0,0,0.3); left:0; top:0; position: fixed; width: 100%; height: 100%; display: none; "></div>
<jsp:include page="reportprint.jsp" />
<jsp:include page="cmm_foot.jsp" />

<script>
  function fn_loadPageData () {
    pending = true;
    var goodsId = location.search.split("&")[0].split("=")[1]
    var date = location.search.split("&")[1].split("=")[1]
    console.log(goodsId, date)
    if (location.search.split("&").length > 2) {
      $('#btm-btn').hide()
    }

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
          var sailorCount = 0
          var rider = response['rider']
          var c;
          var r = []
          for (var i = 0; i < rider.length; i++) {
            var item = rider[i];
            var status = item['status']
            if (status == "미승선") continue
            if (status == "선원") {
              sailorCount += 1
              r.push(item)
            } else if (status == "선장") {
              c = item
            } else {
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
                                            <a class="btn btn-primary btn-lg btn-block rounded h-auto" style="font-size: 12px; padding: 11px 10px;" disabled="">' + status + '</a> \
                                        </div> \
                                    </div> \
                                </div> \
                            </div> \
                        ');
            $(tags).data('item', item);
            $(container).append(tags);
          }
          var numberStr = '<b>'+rider.length+'명</b> (선장 1명, 선원 '+sailorCount+'명 포함)'
          $('#shipname').text(response['shipName'] + ' (' + response['goodsName'] + ')');
          $('#starttime').text(response['startTime']);
          $('#endtime').text(response['endTime']);
          $('#countStr').append(numberStr);

          $('#shipname2').text(response['shipName']);
          var dts = response['startTime'].split(' ')[0].split('-');
          $('#date').text(dts[0] + '년 ' + dts[1] + '월 ' + dts[2] + '일');
          $('#n-c').text(c['name'])
          var b = c['birthday'].substring(0,2) + '.' + c['birthday'].substring(2,4) + '.' + c['birthday'].substring(4,6)
          $('#b-c').text(b)
          $('#g-c').text(c['sex'])
          $('#p-c').text(c['phone'])
          $('#em-c').text(c['emergencyPhone'])
          for (var i = 0; i < r.length; i++) {
            $('#n-'+String(i+1)).text(r[i]['name'])
            var b = r[i]['birthday'].substring(0,2) + '.' + r[i]['birthday'].substring(2,4) + '.' + r[i]['birthday'].substring(4,6)
            $('#b-'+String(i+1)).text(b)
            $('#g-'+String(i+1)).text(r[i]['sex'])
            $('#p-'+String(i+1)).text(r[i]['phone'])
            $('#em-'+String(i+1)).text(r[i]['emergencyPhone'])
            if (r[i]['status'] == '선원') $('#etc-'+String(i+1)).text('선원')
          }

          $('#sendBtn').click(function () {
            $('#mask').show()
            alert('등록중입니다. 잠시만 기다려주세요');
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
                $('#mask').hide()
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