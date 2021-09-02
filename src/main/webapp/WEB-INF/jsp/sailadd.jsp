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
    <span class="navbar-title">승선자 추가</span>
</nav>
<!-- // Navigation -->

<!-- 정보 -->
<div class="container nopadding mt-1">
    <h5 class="text-center" id="pageHead"></h5>
</div>
<!--// 정보 -->
<div class="space mt-1 mb-4"></div>

<!-- 입력 -->
<div class="container nopadding mt-3">
    <p class="text-right"><strong class="required"></strong> 필수입력</p>
    <form>
        <div class="form-group">
            <label for="name">승선자명 <strong class="required"></strong></label>
            <input type="text" class="form-control" id="name" placeholder="승선자명을 입력하세요.">
        </div>
        <div class="form-group">
            <label for="phone">휴대폰번호 <strong class="required"></strong></label>
            <input type="text" class="form-control" id="phone" placeholder="휴대폰번호를 입력해 주세요.">
        </div>
        <div class="form-group">
            <label for="emergencyPhone">비상연락처 <strong class="required"></strong></label>
            <input type="text" class="form-control" id="emergencyPhone" placeholder="비상연락처를 입력해 주세요.">
        </div>
        <div class="form-group">
            <label for="birthDate">생년월일 <strong class="required"></strong></label>
            <input type="number" class="form-control" id="birthDate" placeholder="생년월일을 입력해 주세요.(예: 19800707)">
        </div>
        <div class="form-group">
            <label for="sex">성별선택 <strong class="required"></strong></label>
            <select class="form-control" id="sex">
                <option value="">성별을 선택하세요</option>
                <option value="M">남성</option>
                <option value="F">여성</option>
            </select>
        </div>
        <div class="form-group">
            <label for="addr">주소 <strong class="required"></strong></label>
            <input type="text" class="form-control" id="addr" placeholder="주소를 입력해 주세요.">
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
<script>
    function fn_submit () {
        // var orderId = new URLSearchParams(location.search).get('orderId');
        var orderId = location.search.split('=')[1]
        var name = $('#name').val();
        var phone = $('#phone').val();
        var emergencyPhone = $('#emergencyPhone').val();
        var sex = $('#sex').val();
        var addr = $('#addr').val();
        var birthDate = $('#birthDate').val();
        var checked = $('#chkagree').is(':checked');

        if (name.length == 0) {
            alert('승선자명을 입력해주세요.');
            return;
        }else if (phone.length == 0) {
            alert('휴대폰번호를 입력해주세요.');
            return;
        }else if (emergencyPhone.length == 0) {
            alert('비상연락처를 입력해주세요.');
            return;
        }else if (birthDate.length == 0) {
            alert('생년월일을 입력해주세요.');
            return;
        }else if (sex == "") {
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
        $.ajax('/v2/api/sail/riders/add', {
            method: 'POST',
            dataType: 'json',
            data: JSON.stringify({
                name: name,
                phone: phone,
                emergencyPhone: emergencyPhone,
                birthDate: birthDate,
                orderId: orderId,
                sex: sex,
                addr: addr
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
        // var orderId = new URLSearchParams(location.search).get('orderId');
        var orderId = location.search.split('=')[1]
        $.ajax('/v2/api/sail/riders/detail/' + orderId, {
            method: 'GET',
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
            },
            success: function (response) {
                document.getElementById('pageHead').innerHTML = response['shipName'] + '<br/><small class="red">' + response['date'] + '</small>';
            }
        });
    }
    $(document).ready(function () {
        fn_loadPageData();
    });
</script>

</body>
</html>
