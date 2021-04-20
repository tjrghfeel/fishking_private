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
<head>
    <meta charset="utf-8" />
    <meta name="theme-color" content="#000000" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no" />
    <meta name="description" content="어복황제 - 똑똑한 바다낚시">
    <meta name="keywords" content="어복황제, 바다낚시, 갯바위낚시, 어복스토리, 낚시, 선상 낚시, 갯바위, 물때, 실시간 조황, 어복TV, 조황일지, 조행기, 국내최초 실시간 영상 서비스, 실시간조황, 전국 조황 영상 라이브, 인기 포인트, 실시간 예약, 맞춤 바다낚시, 100% 신뢰 전국 낚시정보">
    <title>어복황제 :: 똑똑한 바다낚시</title>

    <!-- CSS -->
    <link href="/assets/smartsail/css/bootstrap.css" rel="stylesheet">
    <link href="/assets/smartsail/css/style.css" rel="stylesheet">

    <!-- Favicons -->
    <link rel="apple-touch-icon" href="/assets/cust/brand/apple-touch-icon.png" sizes="180x180">
    <link rel="icon" href="/assets/cust/brand/favicon.png">

    <!-- Facebook -->
    <meta property="og:url" content="">
    <meta property="og:title" content="어복황제">
    <meta property="og:description" content="어복황제 - 똑똑한 바다낚시">
    <meta property="og:image" content="/assets/brand/fishking-social.png">
    <meta property="og:image:type" content="image/png">
    <meta property="og:image:width" content="1200">
    <meta property="og:image:height" content="630">
</head>
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
