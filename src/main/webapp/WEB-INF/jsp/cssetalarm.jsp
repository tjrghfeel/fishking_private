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
    <a href="javascript:history.back();" class="nav-left" style="width: 44px; height: 40px; padding-top: 7px; top: 2px; left: 4px;"><img src="/assets/smartsail/img/svg/navbar-back.svg" alt="뒤로가기"/></a>
    <span class="navbar-title">알림 설정</span>
</nav>
<!-- // Navigation -->


<jsp:include page="cmm_foot.jsp" />
<script>
    function fn_setAlarm(code, isSet) {
        $.ajax('/v2/api/setting/alertSet/' + code + '/' + isSet, {
            method: 'PUT',
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
            },
            success: function (response) {
                console.log(JSON.stringify(response));
            }
        });
    }
    $(document).ready(function () {
        $.ajax('/v2/api/setting/alertSet', {
            method: 'GET',
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', localStorage.getItem('@accessToken'));
            },
            success: function (response) {
                for (var i = 0; i < response.length; i++) {
                    var item = response[i];
                    var container = document.body;
                    var tags
                    if (i == 0) {
                      tags = `<div class="container nopadding mt-3">`
                    } else {
                      tags = `<div class="container nopadding">`
                    }
                    tags += `
                                    <div class="row no-gutters mt-3 mb-2">
                                        <div class="col">
                                            <strong>` + item['codeName'] + `</strong><br/>
                                            <small class="grey"></small>
                                        </div>
                                        <div class="col">
                                            <nav>
                                                <div class="nav nav-tabs btn-set" id="nav-tab" role="tablist">
                                                    <a onclick="javascript:fn_setAlarm('` + item['code'] + `',true);" class="nav-link btn btn-on ` + (item['isSet']? 'active' : '') + ` " id="nav-home-tab" data-toggle="tab" href="#nav-on" role="tab" aria-controls="nav-on" aria-selected="true">ON</a>
                                                    <a onclick="javascript:fn_setAlarm('` + item['code'] + `',false);" class="nav-link btn btn-off ` + (!item['isSet']? 'active' : '') + `" id="nav-profile-tab" data-toggle="tab" href="#nav-off" role="tab" aria-controls="nav-off" aria-selected="false">OFF</a>
                                                </div>
                                            </nav>
                                        </div>
                                    </div>
                                    <p class="space"></p>
                                </div>
                    `;
                    $(container).append(tags);
                }
            }
        })
    });
</script>

</body>
</html>
