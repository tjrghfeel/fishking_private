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
    <a href="javascript:location.href = '/boarding/dashboard';" class="nav-left"><img src="/assets/smartsail/img/svg/navbar-back.svg" alt="뒤로가기"/></a>
    <span class="navbar-title">고객센터</span>
    <a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>
</nav>
<!-- // Navigation -->

<!-- 탭메뉴 -->
<jsp:include page="cs_tab.jsp" />
<!--// 탭메뉴 -->

<!-- 데이터 -->
<div class="container nopadding">
    <div class="accordion" id="accordionFaq">

    </div>
    <p class="space mt-1"></p>
</div>
<!--// 데이터 -->
<jsp:include page="cmm_foot.jsp" />
<script>
    var page = 0;
    var pending = false;
    var scrollEvent = function () {
        var scrollHeight = document.scrollingElement.scrollHeight - window.outerHeight;
        var itemHeight = 80;
        var scrollPosition = window.pageYOffset;
        if (!pending && scrollPosition + itemHeight >= scrollHeight) {
            page = page + 1;
            fn_loadPageData();
        }
    }
    function fn_loadPageData () {
        pending = true;
        $.ajax('/v2/api/faq/' + page, {
            method: 'GET',
            dataType: 'json',
            data: {
                role: 'shipowner'
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
            },
            success: function (response) {
                if (response['content'].length === 0) {
                    window.removeEventListener('scroll', scrollEvent);
                }else{
                    var container = document.getElementById('accordionFaq');
                    for (var i = 0; i < response['content'].length; i++) {
                        var item = response['content'][i];
                        var tags = `
                                    <div class="card">
                                        <div class="card-header" id="heading` + page + '-' + i + `">
                                            <h2 class="mb-0">
                                                <a class="btn btn-block text-left collapsed" data-toggle="collapse" data-target="#collapse` + page + '-' + i + `" aria-expanded="true" aria-controls="collapse` + page + '-' + i + `">
                                                    [` + item['questionType'] + `] ` + item['title'] + `
                                                </a>
                                            </h2>
                                        </div>

                                        <div id="collapse` + page + '-' + i + `" class="collapse" aria-labelledby="heading` + page + '-' + i + `" data-parent="#accordionFaq">
                                            <div class="card-body">
                                                ` + item['contents'] + `
                                            </div>
                                        </div>
                                    </div>
                        `;
                        $(container).append(tags);
                    }
                }
                pending = false;
            }
        });
    }
    $(document).ready(function () {
        fn_loadPageData();
        window.addEventListener('scroll', scrollEvent);
    });
</script>
</body>
</html>
