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
    <span class="navbar-title">공지사항</span>
<%--<a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>--%>
</nav>
<!-- // Navigation -->

<!-- 탭메뉴 -->
<jsp:include page="cs_tab.jsp" />
<!--// 탭메뉴 -->

<!-- 리스트 -->
<div class="container nopadding">
    <div class="pt-0" id="list-container">
        <hr class="full mt-0 mb-3"/>

    </div>
</div>
<!--// 리스트 -->

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
        $.ajax('/v2/api/notice/' + page, {
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
                    var container = document.getElementById('list-container');
                    for (var i = 0; i < response['content'].length; i++) {
                        var item = response['content'][i];
                        var tags = ' \
                                <a href="/boarding/csnoticedetail?postId=' + item['id'] + '"> \
                                    <div class="row no-gutters align-items-center"> \
                                        <div class="col-11 pl-2"><strong class="text-primary">[' + item['channelType'] + '] </strong>' + item['title'] + '<br/><small class="grey">' + item['date'].substr(0,10).replace(/[-]/g,'.') + '</small></div> \
                                        <div class="col-1 text-right pl-1"><img src="/assets/smartsail/img/svg/cal-arrow-right.svg" alt=""/></div> \
                                    </div> \
                                </a>';
                        $(container).append(tags);
                        $(container).append('<hr class="full mt-3 mb-3"/>');
                    }
                }
                pending = false;
            }
        })
    }
    $(document).ready(function () {
        fn_loadPageData();
        window.addEventListener('scroll', scrollEvent);
    });
</script>

</body>
</html>
