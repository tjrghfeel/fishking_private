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
    <span class="navbar-title">승선관리</span>
    <a href="my-alarm.html" class="fixed-top-right new"><strong>N</strong><img src="/assets/smartsail/img/svg/icon-alarm.svg" alt="알림내역"/><span class="sr-only">알림내역</span></a>
</nav>
<!-- // Navigation -->

<!-- 탭메뉴 -->
<jsp:include page="cmm_tab.jsp" />
<!--// 탭메뉴 -->

<!--  Filter -->
<div class="filterlinewrap container nopadding">
    <ul class="nav nav-tabs nav-filter">
        <li>
            <div class="input-group">
                <label for="startDate" class="sr-only">예약일자</label>
                <input readonly type="text" class="form-control" id="startDate" placeholder="" value="">
                <span class="input-group-btn">
							<a onclick="javascript:$('#startDate').datepicker('show');" class="btn btn-default"><img src="/assets/smartsail/img/svg/input_cal.svg" alt=""/></a>
						</span>
                <span class="input-group-addon">~</span>
                <label for="endDate" class="sr-only">예약일자</label>
                <input readonly type="text" class="form-control" id="endDate" placeholder="" value="">
                <span class="input-group-btn">
							<a onclick="javascript:$('#endDate').datepicker('show');" class="btn btn-default"><img src="/assets/smartsail/img/svg/input_cal.svg" alt=""/></a>
						</span>
            </div><!-- /input-group -->
        </li>
        <li>
            <label for="status" class="sr-only">결제상태</label>
            <select class="form-control" id="status">
                <option value="">상태전체</option>
                <option value="wait">이용예정</option>
                <option value="complete">이용완료</option>
                <option value="cancel">취소완료</option>
            </select>
        </li>
        <li class="full-line">
            <div class="input-group keyword">
                <select class="custom-select" id="keywordType">
                    <option value="username">예약자명</option>
                    <option value="riderName">승선자명</option>
                </select>
                <input id="keyword" type="text" class="form-control" placeholder="검색어 입력" value="">
            </div><!-- /input-group -->
        </li>
        <li class="full">
            <p><a onclick="javascript:page=0;fn_loadPageData();" class="btn btn-primary btn-sm">검색</a><a onclick="javascript:fn_init();" class="btn btn-grey btn-sm">초기화</a></p>
        </li>
    </ul>
</div>
<!-- /end Filter -->
<p class="clearfix"></p>

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
    function fn_init () {
        $('#startDate').datepicker('setDate', new Date());
        var endDate = new Date();
        endDate.setMonth(endDate.getMonth() + 1);
        $('#endDate').datepicker('setDate', endDate);
        $('#status').val('');
        $('#keywordType').val('username');
        $('#keyword').val('');
    }
    function onclick_item (element) {
        var item = $(element.parentNode.parentNode).data('item');
        location.href = '/boarding/saildetail?orderId=' + item['orderId'];
    }
    function onclick_add (element) {
        var item = $(element.parentNode.parentNode.parentNode.parentNode).data('item');
        location.href = '/boarding/sailadd?orderId=' + item['orderId'];
    }
    function fn_loadPageData () {
        pending = true;

        var startDate = $('#startDate').val();
        var endDate = $('#endDate').val();
        var status = $('#status').val();
        var keywordType = $('#keywordType').val();
        var keyword = $('#keyword').val();

        if (status.length == 0) status = null;
        if (keyword.length == 0) {
            keywordType = null;
            keyword = null;
        }

        if (page == 0) {
            $('[name="list-item"]').remove();
            window.addEventListener('scroll', scrollEvent);
        }

        $.ajax('/v2/api/sail/riders/' + page, {
            method: 'GET',
            dataType: 'json',
            data: {
                startDate: startDate,
                endDate: endDate,
                status: status,
                keywordType: keywordType,
                keyword: keyword
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
            },
            success: function (response, status, xhr) {
                if (xhr.status == 204) {
                    if (page == 0) {
                        alert('조회된 데이터가 없습니다.');
                    }else{
                        window.removeEventListener('scroll', scrollEvent);
                    }
                }else{
                    var container = document.body;
                    for (var i = 0; i < response['content'].length; i++) {
                        var item = response['content'][i];
                        var tags = $(`
                                    <div class="container nopadding mt-2" name="list-item" data-index="` + i + `">
                                        <div class="card-round-grey">
                                            <span class="status ` + (item['status'] == '취소완료'? 'status6' : 'status2') + `">` + item['status'] + `</span>
                                            ` + (item['status'] != '취소완료'? `<span class="dday">D` + item['dateInterval'] + `</span>` : ``) + `
                                            <a onclick="javascript:onclick_item(this);">
                                                <div class="card card-sm">
                                                    <div class="row no-gutters">
                                                        <div class="cardimgWrap">
                                                            <img src="/assets/smartsail/img/sample/boat2.jpg" class="img-fluid" alt="">
                                                        </div>
                                                        <div class="cardInfoWrap">
                                                            <div class="card-body">
                                                                <h6>` + item['shipName'] + `</h6>
                                                                <p><span class="grey">` + item['goodsName'] + `</span></p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <hr class="mt-1 mb-1"/>
                                                    <div class="row no-gutters">
                                                        <div class="col-6 padding-sm"><small class="grey">이용일<br/>예약자</small></div>
                                                        <div class="col-6 padding-sm"><small>` + item['date'] + ` ~<br/>` + item['orderName'] + ` <span class="grey">|</span> ` + item['orderEmail'] + `</small></div>
                                                    </div>
                                                </div>
                                            </a>
                                            ` + (item['status'] != '취소완료'? `
                                            <div class="row no-gutters">
                                                <div class="col-12 padding-sm"><a onclick="javascript:onclick_add(this);" class="btn btn-info btn-block btn-sm mt-1 mb-1">승선자 추가  (현재 <strong>` + item['personnel'] + `명</strong>)</a></div>
                                            </div>
                                            ` : ``) + `
                                        </div>
                                    </div>
                        `);
                        $(tags).data('item', item);
                        $(container).append(tags);
                    }
                }
                pending = false;
            }
        });
    }
    $(document).ready(function () {
        $("#startDate").datepicker({
            onSelect: function (text, inst) { }
        }).datepicker('setDate', new Date());
        var endDate = new Date();
        endDate.setMonth(endDate.getMonth() + 1);
        $("#endDate").datepicker({
            onSelect: function (text, inst) { }
        }).datepicker('setDate', endDate);

        fn_loadPageData();
        window.addEventListener('scroll', scrollEvent);
    });
</script>

</body>
</html>
