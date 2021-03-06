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
    <span class="navbar-title">취소 및 환불 규정</span>
</nav>
<!-- // Navigation -->

<!-- 데이터 -->
<div class="container nopadding mt-0">
    <div class="padding policy">

        <table class="table table-bordered mt-3">
            <colgroup>
                <col style="width:20%">
                <col style="width:20%">
                <col style="width:60%">
            </colgroup>
            <tbody>
            <tr>
                <th rowspan="4">고객 변심에 의한 환불</th>
                <th rowspan="3">일반결제상품</th>
                <td>1) 이용일 기준 7일~3일 전 : 100% 환불</td>
            </tr>
            <tr>
                <td>2) 이용일 기준 2일 전 : 50% 환불(고객센터 운영시간 내)</td>
            </tr>
            <tr>
                <td>3) 이용일 기준 1일 전~당일, No-show : 환불불가</td>
            </tr>
            <tr>
                <th>티켓결제상품</th>
                <td>1) 구매일 기준 7일 이내 : 100% 환불. 단, 7일 이내 부분 취소 환불 및 7일 이후 환불 처리는 회사에 요청</td>
            </tr>
            <tr>
                <th rowspan="3" colspan="2">천재지변에 의한 환불 - 해상</th>
                <td>1) 적용업체 - 선상, 갯바위, 좌대낚시, 해상콘도</td>
            </tr>
            <tr>
                <td>2) 해당 지역에 풍랑, 폭풍, 태풍주의보 3종류의 주의보 발효시(기상청 발표기준) 100% 환불</td>
            </tr>
            <tr>
                <td>3) 이용 시작 이후에는 환불 조항이 적용되지 않는다.</td>
            </tr>
            <tr>
                <th rowspan="2">기타</th>
                <th rowspan="2">일반·티켓 결제상품</th>
                <td>1) 업체의 사정에 의해 환불이 필요할 경우 “회사”에게 요청한다.</td>
            </tr>
            <tr>
                <td>2) 일부상황에서 부분 취소가 불가한 경우, 당사의 환불 규정에 의거하여 적용된다.<br/>※ 부분 취소 불가 케이스- 할부 결제 이용 시, 카드사 포인트 결제, 세이브 서비스 이용, 기타 카드 (기프트, 포인트, 선불, 법인, 해외), 최초 결제일로부터 3개월 경과 시</td>
            </tr>
            </tbody>
        </table>
        <p class="clearfix"></p>

        <ul class="list">
            <li>업체 사정에 의해 취소 발생 시 100% 환불이 가능합니다.</li>
            <li>예약 상품 별 예약정보에 기재된 취소, 환불 규정을 반드시 확인 후 이용해주시기 바랍니다.</li>
            <li>예약 이후의 취소는 취소/환불 규정에 의거하여 적용됩니다.</li>
            <li>취소, 변경 불가 상품은 규정과 상관없이 취소, 변경이 불가합니다.</li>
            <li>당일 결제를 포함한 당일 취소는 취소, 변경이 불가합니다.</li>
            <li>전월 휴대폰 결제 건은 단순 변심의 사유로 예약 취소할 경우, 취소 규정 외에 환불 수수료 5%가 발생합니다.</li>
            <li>예약 취소가 불가능한 시간에 고객 사정에 의한 취소 시, 어복황제가 제공하는 모든 혜택에서 제외될 수 있으며(할인 쿠폰 미제공, 이벤트 대상자 제외, 혜택받기 포인트 미지급), 본 예약 시 사용한 쿠폰은 소멸됩니다.</li>
            <li>“회원”의 단순 변심에 의한 취소 및 환불일 경우 이의 처리에 발생하는 수수료는 “회원”이 부담합니다.</li>
            <li>구매 취소 시점과 해당 카드사의 환불 처리기준에 따라 취소금액의 환급 방법과 환급일은 다소 차이가 있을 수 있으며, 사용한 신용카드의 환불 정책은 신용카드회사에 직접 문의하여야 합니다.</li>
            <li>개별 "상품 등"의 성격에 따라 "회사"는 별도 계약 및 이용조건에 따른 구매취소 및 청약철회 관련 규정을 정할 수 있으며 이 경우 별도 계약 및 이용조건상의 구매취소 및 청약철회 규정이 우선 적용됩니다.</li>
            <li>환불 기간은 취소 요청일로부터 3일 이내(영업일 기준) 이내에 처리하며, 주말 및 휴일일 경우 다음 영업일에 환불 처리합니다.</li>
            <li>"회원"이 타인의 신용카드 또는 휴대전화 번호를 도용하는 등 본 약관에서 금지하는 부정한 방법으로 부당한 이익을 편취하였다고 의심되는 경우 "회사"는 "회원"의 티켓 구매를 취소처리 하거나 티켓의 사용을 제한할 수 있으며. "회원"이 충분한 소명 자료를 제출할 때까지 환불을 보류할 수 있습니다.</li>
            <li>업체의 사정으로 상세 정보가 수시로 변경될 수 있으며, 이로 인한 불이익은 어복황제가 책임지지 않습니다.</li>
            <li>업체 현장에서 제반관리 및 서비스로 인해 발생된 분쟁은 어복황제에서 책임지지 않습니다.</li>
        </ul>
        <p class="clearfix"><br/></p>
        <p class="clearfix"><br/></p>

    </div>
</div>
<!--// 데이터 -->


<!-- JS and JavaScript -->
<script src="/assets/smartsail/js/jquery.min.js"></script>
<script src="/assets/smartsail/js/bootstrap.min.js"></script>
<script src="/assets/smartsail/js/jquery.touchSwipe.min.js"></script>
<script src="/assets/smartsail/js/default.js"></script>
<script src="/assets/smartsail/js/swiper.min.js"></script>

<script>//Navigation Menu Slider
$(document).ready(function(){
    $('#nav-expander').on('click',function(e){
        e.preventDefault();
        $('body').toggleClass('nav-expanded');
    });
    $('#nav-close').on('click',function(e){
        e.preventDefault();
        $('body').removeClass('nav-expanded');
    });
});
</script>

<script>//Swipe
$(".carousel").swipe({
    swipe: function(event, direction, distance, duration, fingerCount, fingerData) {
        if (direction == 'left') $(this).carousel('next');
        if (direction == 'right') $(this).carousel('prev');
    },
    tap:function(event, target) {
        // navigateTo(url)
    },

    allowPageScroll:"vertical",
    excludedElements: "label, button, input, select, textarea, .noSwipe",
    threshold:1
});

$(document).swipe({
    swipe:function(event, direction, distance, duration, fingerCount) {
    },
    click:function (event, target) {
        $(target).click();
    },
    threshold:75
});
</script>

<script>
    $('.toggle_menu').on('click', function() {
        $('.toggle_menu>span').stop().toggleClass('on');
        $(this).stop().toggleClass('on');
    });

    chkNum = 0;
    $(".toggle_menu").click(function(){
        if(chkNum == 0){
            $(".toggle_menu>span").stop().addClass('on');
            $('nav').stop().addClass('view');
            $('.navbar').stop().addClass('on');
            $(this).stop().addClass('on');
            $(".allmenu").fadeIn();
            chkNum = 1;
        }else{
            $(".toggle_menu>span").stop().removeClass('on');
            $('nav').stop().removeClass('view');
            $('.navbar').stop().removeClass('on');
            $(this).stop().removeClass('on');
            $(".allmenu").fadeOut();
            chkNum = 0;
        }
    });
</script>

<script type="text/javascript">
    $(document).ready(function(){

        $(".toggle-btn").click(function() {
            $(".toggle-content").slideToggle("slow");
            $(this).toggleClass("active"); return false;
            $('.toggle-content').toggleClass('expanded');
        });
    });
</script>

</body>
</html>
