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
    <%--<a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>--%>
</nav>
<!-- // Navigation -->

<!-- 탭메뉴 -->
<jsp:include page="cs_tab.jsp" />
<!--// 탭메뉴 -->

<!-- 탭메뉴 -->
<jsp:include page="cs_qna_tab.jsp" />
<!--// 탭메뉴 -->

<!-- 데이터 -->

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
        $.ajax('/v2/api/qna/' + page, {
            method: 'GET',
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
            },
            success: function (response) {
                console.log(JSON.stringify(response));
                if (response['content'].length === 0) {
                    window.removeEventListener('scroll', scrollEvent);
                }else{
                    var container = document.body;
                    for (var i = 0; i < response['content'].length; i++) {
                        var item = response['content'][i];
                        var tags = ' \
                                    <div class="container nopadding ' + (page == 0 && i == 0? 'mt-3' : '') + '"> \
                                        <a href="/boarding/csqnadetail?postId=' + item['id'] + '"> \
                                            <div class="row no-gutters align-items-center"> \
                                                <div class="col-9 text-left"> \
                                                    <strong>' + item['questionType'] + '</strong><br/> \
                                                    <small class="grey">' + item['createdDate'].substr(0, 10) + '</small> \
                                                </div> \
                                                <div class="col-3 text-right"> \
                                                    <span class="status-icon ' + (item['replied']? 'status3' : 'status2') + '">' + (item['replied']? '답변완료' : '답변대기') + '</span> \
                                                </div> \
                                            </div> \
                                        </a> \
                                    </div> \
                                    <hr/> \
                        ';
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
