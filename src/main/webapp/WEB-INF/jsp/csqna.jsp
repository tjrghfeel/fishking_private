<%--
  Created by IntelliJ IDEA.
  User: jinheelee
  Date: 2021/04/13
  Time: 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="cmm_head.jsp" />
<body>
<!-- Navigation -->
<nav class="navbar fixed-top navbar-dark bg-primary">
    <a href="javascript:location.href = '/boarding/dashboard';" class="nav-left"><img src="/assets/smartsail/img/svg/navbar-back.svg" alt="뒤로가기"/></a>
    <span class="navbar-title">고객센터</span>
    <a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>
</nav>
<!-- // Navigation -->

<!-- 탭메뉴 -->
<jsp:include page="cs_tab.jsp" />
<!--// 탭메뉴 -->

<!-- 탭메뉴 -->
<jsp:include page="cs_qna_tab.jsp" />
<!--// 탭메뉴 -->

<!-- 데이터 -->
<div class="container nopadding mt-3">
    <p class="text-right"><strong class="required"></strong> 필수항목입니다.</p>

    <form>
        <div class="row">
            <div class="col-xs-12 col-sm-12 apply ">

                <div class="form-group">
                    <div class="input-group">
                        <label class="input-group-addon">카테고리<strong class="required"></strong></label>
                        <select class="form-control" id="questionType">
                            <option value="">카테고리를 선택하세요</option>
                            <c:forEach var="item" items="${questionType}">
                                <option value="${item.key}">${item.value}</option>
                                ${item.key}
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <label class="input-text-addon" for="contents">내용<strong class="required"></strong></label>
                        <textarea class="form-control" rows="9" placeholder="내용을 작성하세요" id="contents"></textarea>
                    </div>
                </div>

                <label class="control radio mt-3 mr-3">
                    <input type="radio" name="returnType" value="tel" class="add-contrast" data-role="collar" checked>
                    <span class="control-indicator"></span><span class="control-text">연락처</span>
                </label>
                <label class="control radio mt-3">
                    <input type="radio" name="returnType" value="email" class="add-contrast" data-role="collar">
                    <span class="control-indicator"></span><span class="control-text">이메일</span>
                </label>

                <div class="form-group">
                    <div class="input-group">
                        <label class="input-group-addon" for="returnAddress">연락처<strong class="required"></strong></label>
                        <input type="email" class="form-control" id="returnAddress" placeholder="답변 받을 연락처를 입력해 주세요. ">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <label class="input-group-addon" for="file">첨부파일</label>
                        <input type="file" class="form-control" id="file" onchange="javascript:fn_upload();" placeholder="파일을 첨부하세요.">
                    </div>
                </div>

            </div>
        </div>
    </form>
</div>
<!--// 데이터 -->

<!-- 하단버튼 -->
<div class="fixed-bottom">
    <div class="row no-gutters">
        <div class="col-12"><a onclick="javascript:fn_submit();" class="btn btn-primary btn-lg btn-block">문의하기</a></div>
    </div>
</div>
<!--// 하단버튼 -->

<!-- Modal 등록완료 -->
<div class="modal fade" id="inputModal" tabindex="-1" aria-labelledby="inputModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-center" id="inputModalLabel">등록완료</h5>
            </div>
            <div class="modal-body text-center">
                <p>문의하신 내용이 정상적으로 등록되었습니다.<br/>담당자가 빠른 시일내에 답변드리도록 하겠습니다.<br/>답변내용은 문의내역에서 확인이 가능합니다.</p>
            </div>
            <div class="modal-footer-btm">
                <div class="row no-gutters">
                    <div class="col-12"><a href="/boarding/csqna" class="btn btn-primary btn-lg btn-block" data-dismiss="modal">확인</a></div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--// Modal 등록완료 -->

<jsp:include page="cmm_foot.jsp" />
<script>
    var fileList = [];
    function fn_upload () {
        var file = document.getElementById('file').files[0] || null;
        var form = new FormData();
        form.append("file", file);
        form.append("filePublish", "one2one");
        $.ajax({
            type: 'POST',
            enctype: 'multipart/form-data',
            url: '/v2/api/filePreUpload',
            data: form,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
            },
            success: function (response) {
                fileList = [response['fileId']];
            }
        });
    }
    function fn_submit () {
        var questionType = document.getElementById('questionType').selectedOptions[0].value;
        var contents = document.getElementById('contents').value;
        var returnType = document.querySelector('[name="returnType"]:checked').value;
        var returnAddress = document.getElementById('returnAddress').value;
        var file = document.getElementById('file').files[0] || null;
        var targetRole = 'shipowner';

        if (questionType == '') {
            alert('카테고리를 선택해주세요.');
            return;
        }else if (contents == '') {
            alert('내용을 작성해주세요.');
            return;
        }else if (returnAddress == '') {
            alert('연락처를 입력해주세요.');
            return;
        }

        $.ajax('/v2/api/post/one2one', {
            method: 'POST',
            dataType: 'json',
            data: JSON.stringify({
                questionType: questionType,
                contents: contents,
                returnType: returnType,
                returnAddress: returnAddress,
                fileList: fileList,
                targetRole: targetRole
            }),
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
                xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
            },
            success: function (response) {
                if (response) {
                    $('#inputModal').modal('show');
                }
            }
        });
    }
</script>

</body>
</html>
