<%--
  Created by IntelliJ IDEA.
  User: mac
  Date: 2021/04/12
  Time: 4:30 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="cmm_head.jsp" />
<body>

<script src="/assets/smartsail/js/jquery.min.js"></script>
<script>
    $(document).ready(function () {
        var ua = navigator.userAgent;
        if (ua.indexOf('Android') !== -1 || ua.indexOf('android') !== -1) {
            location.href = 'intent://action/#Intent;scheme=fishking;package=com.native_fishking;end';
        }else{
            setTimeout(function () {
                location.href = 'https://itunes.apple.com/app/native_fishking';
            }, 100);
            location.href = "fishking://";
        }
    });
</script>
</body>
</html>
