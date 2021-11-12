<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="cmm_head.jsp" />
<body>
<!-- Navigation -->
<nav class="navbar fixed-top navbar-dark bg-primary">
    <a href="javascript:history.back();" class="nav-left"><img src="/assets/smartsail/img/svg/navbar-back.svg" alt="뒤로가기"/></a>
    <span class="navbar-title">선원 추가</span>
</nav>
<!-- // Navigation -->

<!-- 정보 -->
<div class="container nopadding mt-1">
    <div class="mt-3 mb-3" style="text-align: center">
        <div><b><span id="shipname"></span> (<span id="goodsname"></span>)</b></div>
        <div id="datestr"></div>
    </div>
</div>
<!--// 정보 -->
<div class="space mt-1 mb-4"></div>

<!-- 입력 -->
<div class="container nopadding mt-3">
    <select id="sailorId" class="form-control rounded-0">
        <option value="0">신규등록</option>
    </select>
    <hr />
    <p class="text-right"><strong class="required"></strong> 필수입력</p>
    <form>
        <div class="form-group">
            <label for="name">선원이름 <strong class="required"></strong></label>
            <input type="text" class="form-control" id="name" placeholder="선원이름을 입력하세요.">
        </div>
        <div class="form-group">
            <label for="phone">연락처 <strong class="required"></strong></label>
            <input type="text" class="form-control" id="phone" placeholder="휴대폰번호를 입력해 주세요.">
        </div>
        <div class="form-group">
            <label for="emergencyPhone">비상연락처 <strong class="required"></strong></label>
            <input type="text" class="form-control" id="emergencyPhone" placeholder="비상연락처를 입력해 주세요.">
        </div>
        <div class="form-group">
            <label for="idNum">주민등록번호 <strong class="required"></strong></label>
            <input type="number" class="form-control" id="idNum" placeholder="주민등록번호를 입력해 주세요.(예: 8007071111222)">
        </div>
        <div class="form-group">
            <label for="addr">주소 <strong class="required"></strong></label>
            <div class="d-flex flex-row">
                <input type="text" class="form-control" style="width: 75%;" id="addr" placeholder="주소를 입력해 주세요." readonly>
                <a class="btn btn-block btn-grey rounded-0" style="width: 25%; font-size: 12px; height: 33px; !important" onclick="javascript:postCode()">주소찾기</a>
            </div>
            <%--주소찾기--%>
            <div id="wrap" style="display:none;border:1px solid;height:300px;margin:5px 0;position:relative">
                <img src="//t1.daumcdn.net/postcode/resource/images/close.png" id="btnFoldWrap" style="cursor:pointer;position:absolute;right:0px;top:-1px;z-index:1" onclick="foldDaumPostcode()" alt="접기 버튼">
            </div>
        </div>
        <div class="space mb-4"></div>
        <div class="form-group mt-0 mb-0">
            <label for="chkagree" class="d-block"><a href="/boarding/cssetpolicyterms" class="text-primary">이용약관</a> 및 <a href="/boarding/cssetpolicyprivacy" class="text-primary">개인정보 수집/이용</a><br/>개인정보 제 3자 이용약관, 취소규정 동의(필수)</small></a></label>
            <label class="control checkbox">
                <input id="chkagree" type="checkbox" class="add-contrast" data-role="collar">
                <span for="chkagree" class="control-indicator"></span><span class="control-text">위 내용을 확인하였으며 동의합니다. </span>
            </label>
        </div>

        <%--        <a href="#none" class="btn btn-round btn-dark btn-block mt-3"><img src="/assets/smartsail/img/svg/icon-jimun.svg" class="vam">지문입력</a>--%>
        <%--        <div class="space mb-4"></div>--%>


    </form>
    <p class="clearfix"><br/><br/></p>
</div>
<!--// 입력 -->
<!-- 하단버튼 -->
<div class="fixed-bottom">
    <div class="row no-gutters">
        <!-- <div class="col-12"><a href="#none" class="btn btn-grey btn-lg btn-block">확인</a></div> 비활성버튼 -->
        <div class="col-12"><a onclick="fn_submit();" class="btn btn-primary btn-lg btn-block">확인</a></div>
    </div>
</div>
<!--// 하단버튼 -->


<div id="mask" style="z-index: 999; background-color: rgba(0,0,0,0.3); left:0; top:0; position: fixed; width: 100%; height: 100%; display: none; "></div>
<jsp:include page="cmm_foot.jsp" />

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
  function fn_submit () {
    // var orderId = new URLSearchParams(location.search).get('orderId');
    var goodsId = location.search.split("&")[0].split("=")[1]
    var date = location.search.split("&")[1].split("=")[1]
    var name = $('#name').val();
    var phone = $('#phone').val();
    var emergencyPhone = $('#emergencyPhone').val();
    var idNum = $('#idNum').val();
    var addr = $('#addr').val();
    var id = $('#sailorId').val();
    var checked = $('#chkagree').is(':checked');

    if (name.length == 0) {
      alert('선원이름을 입력해주세요.');
      return;
    }else if (phone.length == 0) {
      alert('휴대폰번호를 입력해주세요.');
      return;
    }else if (emergencyPhone.length == 0) {
      alert('비상연락처를 입력해주세요.');
      return;
    }else if (idNum == "") {
      alert('성별을 선택해주세요.');
      return;
    }else if (addr.length == 0) {
      alert('주소를 입력해주세요.');
      return;
    }else if (!checked) {
      alert('약관 내용을 확인하고 동의해주세요.');
      return;
    }

    $('#mask').show()
    alert('등록중입니다. 잠시만 기다려주세요');
    $.ajax('/v2/api/sail/sailor/add', {
      method: 'POST',
      dataType: 'json',
      data: JSON.stringify({
        goodsId: goodsId,
        date: date,
        name: name,
        phone: phone,
        emergencyPhone: emergencyPhone,
        idNumber: idNum,
        address: addr,
        id: id,
      }),
      beforeSend: function (xhr) {
        xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
        xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
      },
      success: function (response) {
        $('#mask').hide()
        alert(response['message']);
        if (response['status'] == 'success') {
          history.back();
        }
      }
    });
  }
  function fn_loadPageData () {
    // // var orderId = new URLSearchParams(location.search).get('orderId');
    // var orderId = location.search.split('=')[1]

    $.ajax('/v2/api/sail/sailor' + location.search, {
      method: 'GET',
      dataType: 'json',
      beforeSend: function (xhr) {
        xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
      },
      success: function (response) {
        $('#shipname').text(response['shipName'])
        $('#goodsname').text(response['goodsName'])
        var dayOfWeek = new Date(response['date']).getDay()
        var weekday = isNaN(dayOfWeek) ? "" :
          ['일', '월', '화', '수', '목', '금', '토'][dayOfWeek];
        $('#datestr').text(response['date'] + ' (' + weekday + ')')

        var sailors = response['sailor']
        var container = $('#sailorId');
        for (var i=0 ; i<sailors.length ; i++) {
          var item = sailors[i];
          var tags = $(' \
                            <option value="' + item['id'] + '" id="' + item['id'] + '"> \
                              ' + item['name'] + ' \
                            </option> \
                        ');
          $(tags).data('item', item);
          $(container).append(tags);
        }
        container.change(function() {
          var id = $(this).val()
          if (id == 0) {
            var item = $(this).children('#'+id).data('item')
            var name = $('#name').val('');
            var phone = $('#phone').val('');
            var emergencyPhone = $('#emergencyPhone').val('');
            var idNum = $('#idNum').val('');
            var addr = $('#addr').val('');
          } else {
            var item = $(this).children('#'+id).data('item')
            var name = $('#name').val(item['name']);
            var phone = $('#phone').val(item['phone']);
            var emergencyPhone = $('#emergencyPhone').val(item['emerNum']);
            var idNum = $('#idNum').val(item['idNumber']);
            var addr = $('#addr').val(item['addr']);
          }

        })
      }
    });
  }
  $(document).ready(function () {
    fn_loadPageData();
  });


  // 우편번호 찾기 찾기 화면을 넣을 element
  var element_wrap = document.getElementById('wrap');

  function foldDaumPostcode() {
    // iframe을 넣은 element를 안보이게 한다.
    element_wrap.style.display = 'none';
  }

  function postCode() {
    // 현재 scroll 위치를 저장해놓는다.
    var currentScroll = Math.max(document.body.scrollTop, document.documentElement.scrollTop);
    new daum.Postcode({
      oncomplete: function(data) {
        // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

        // 각 주소의 노출 규칙에 따라 주소를 조합한다.
        // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
        var addr = ''; // 주소 변수
        var extraAddr = ''; // 참고항목 변수

        addr = data.roadAddress;

        document.getElementById("addr").value = addr;
        element_wrap.style.display = 'none';

        document.body.scrollTop = currentScroll;
      },
      // 우편번호 찾기 화면 크기가 조정되었을때 실행할 코드를 작성하는 부분. iframe을 넣은 element의 높이값을 조정한다.
      onresize : function(size) {
        element_wrap.style.height = size.height+'px';
      },
      width : '100%',
      height : '100%'
    }).embed(element_wrap);

    // iframe을 넣은 element를 보이게 한다.
    element_wrap.style.display = 'block';
  }
</script>

</body>
</html>