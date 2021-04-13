<%--
  Created by IntelliJ IDEA.
  User: jinheelee
  Date: 2021/04/13
  Time: 11:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="/assets/smartsail/js/jquery.min.js"></script>
<script src="/assets/smartsail/js/bootstrap.min.js"></script>
<script src="/assets/smartsail/js/jquery.touchSwipe.min.js"></script>
<script src="/assets/smartsail/js/default.js"></script>
<script src="/assets/smartsail/js/swiper.min.js"></script>
<script src="/assets/smartsail/js/chart.js"></script>

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
