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

<!-- 상품이미지 -->
<div id="carousel-visual-detail" class="carousel slide" data-ride="carousel">
    <div class="float-top-left"><a href="javascript:history.back();"><img src="/assets/smartsail/img/svg/navbar-back.svg" alt="뒤로가기"/></a></div>
    <!-- <ol class="carousel-indicators">
        <li data-target="#carousel-visual-detail" data-slide-to="0" class="active"></li>
        <li data-target="#carousel-visual-detail" data-slide-to="1"></li>
        <li data-target="#carousel-visual-detail" data-slide-to="2"></li>
    </ol>-->
    <div class="carousel-inner">
        <div class="carousel-item active">
            <img id="shipProfileImage" src="/assets/smartsail/img/sample/boat1.jpg" class="d-block w-100" alt="">
        </div>
    </div>
</div>
<!--// 상품이미지 -->

<!-- 상품타이틀 -->
<div class="container nopadding">
    <div class="card mt-3">
        <h4 class="text-center" id="shipHead"></h4>
    </div>
</div>
<!--// 상품타이틀 -->


<a onclick="javascript:moveToAdd();" class="add-circle"><img src="/assets/smartsail/img/svg/icon-add-user.svg" alt="" class="add-icon"/></a>

<jsp:include page="cmm_foot.jsp" />
<script>
    function moveToAdd () {
        // var orderId = new URLSearchParams(location.search).get('orderId');
        var orderId = location.search.split("=")[1]
        location.href = '/boarding/sailadd?orderId=' + orderId;
    }
    function fn_delete (riderId) {
      if (confirm("정말 삭제하시겠습니까?")) {
        $.ajax('/v2/api/sail/riders/del', {
          method: 'POST',
          dataType: 'json',
          data: JSON.stringify({
            riderId: riderId
          }),
          beforeSend: function (xhr) {
            xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
            xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
          },
          success: function (response, status, xhr) {
            if (response.status != 'fail') {
              location.reload();
            }else{
              alert(response.message);
            }
          }
        })
      }
    }
    function fn_loadPageData () {
        // var orderId = new URLSearchParams(location.search).get('orderId');
        var orderId = location.search.split("=")[1];
        $.ajax('/v2/api/sail/riders/detail/' + orderId, {
            method: 'GET',
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
            },
            success: function (response) {
                let reserveComment = (response['reserveComment'])? response['reserveComment'] : '없음';
                document.getElementById('shipProfileImage').src = response['shipProfileImage'];
                document.getElementById('shipHead').innerHTML =
                  response['shipName'] + ''
                  + '<small class="grey">|</small> '
                  + '<small>현재 <strong class="large orange">'
                  + response['personnel']
                  + '</strong>명</small><br/><small class="grey">'
                  + response['date']
                  + '</small><br/>'
                +'<small>요청사항 : '+ reserveComment + '</small>';

                var container = document.body;
                for (var i = 0; i < response['riders'].length; i++) {
                    var item = response['riders'][i];
                    var tags = $(' \
                                    <div class="container nopadding mt-2"> \
                                        <div class="card-round-grey"> \
                                            <div class="card card-sm"> \
                                                <div class="row no-gutters d-flex align-items-center"> \
                                                    <div class="col-6"> \
                                                        <a> \
                                                            <p> \
                                                                승선자명: <strong class="large">' + item['name'] + '</strong><br/> \
                                                                연락처: ' + item['phone'] + '<br/> \
                                                                비상시: ' + item['emergencyPhone'] + ' \
                                                            </p> \
                                                        </a> \
                                                    </div> \
                                                    <div class="col-4 text-right"> \
                                                        <span class="status relative ' + (item['isRide'] != '확인실패'? 'status3' : 'status7') + '">' + item['isRide'] + '</span> \
                                                    </div> \
                                                    <div class="col-2 text-right"> \
                                                        <a onclick="javascript:fn_delete(' + item['riderId'] + ');" class="btn btn btn-round-grey btn-xs-round">― 삭제</a> \
                                                    </div> \
                                                </div> \
                                            </div> \
                                        </div> \
                                    </div> \
                    ');
                    $(container).append(tags);
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
