<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/2/16
  Time: 上午 11:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@ include file="/WEB-INF/include-head.jsp" %>
<script type="text/javascript">
    $(function () {

        var currentUserName = "${sessionScope.loginAdmin.userName}";
        var editUserName = "${requestScope.admin.loginAcct}";
        if (currentUserName == editUserName) {
            $("#updateUserPswdDiv").removeAttr("hidden");
        }

        $("#updateLoginAcct").change(function () {
            var loginAcct = $("#updateLoginAcct").val();
            if (editUserName == loginAcct) {
                $("#update_account_reminder").empty();
            } else {
                $("#update_account_reminder").empty();
                $.ajax({
                    url: "admin/verifyLoginAcct.json",
                    data: "loginAcct=" + loginAcct,
                    type: "POST",
                    success: function (result) {
                        if (result) {
                            $("#update_account_reminder").removeClass().addClass("text-danger").append("用户名已被占用！");
                        } else {
                            $("#update_account_reminder").removeClass().addClass("text-success").append("用户名可用！");
                        }
                    }
                });
            }
        });
    })
</script>

<body>
<%@ include file="/WEB-INF/include-nav.jsp" %>


<div class="container-fluid">
    <div class="row">

        <%@ include file="/WEB-INF/include-sidebar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="admin/to/main/page.html">首页</a></li>
                <li><a href="admin/get/page.html">数据列表</a></li>
                <li class="active">更新</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-heading">表单数据
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <div class="panel-body">
                    <form action="admin/update.html" method="post" role="form">
                        <input type="hidden" name="id" value="${requestScope.admin.id}"/>
                        <input type="hidden" name="pageNum" value="${param.pageNum}"/>
                        <input type="hidden" name="keyword" value="${param.keyword}"/>
                        <%-- <p>${requestScope.exception.message}</p>--%>
                        <div class="form-group">
                            <label for="updateLoginAcct">登录账号</label>
                            <input name="loginAcct" value="${requestScope.admin.loginAcct}" type="text"
                                   class="form-control" id="updateLoginAcct" placeholder="请输入登录账号">
                            <p id="update_account_reminder"></p>
                        </div>
                        <div id="updateUserPswdDiv" class="form-group" hidden="hidden">
                            <label for="updateUserPswd">登录密码</label>
                            <input name="userPswd" type="password" class="form-control" id="updateUserPswd"
                                   placeholder="请输入登录密码">
                        </div>
                        <div class="form-group">
                            <label for="updateUserName">用户昵称</label>
                            <input name="userName" value="${requestScope.admin.userName}" type="text"
                                   class="form-control" id="updateUserName" placeholder="请输入用户昵称">
                        </div>
                        <div class="form-group">
                            <label for="updateEmail">邮箱地址</label>
                            <input name="email" value="${requestScope.admin.email}" type="email" class="form-control"
                                   id="updateEmail" placeholder="请输入邮箱地址">
                            <p class="help-block label label-warning">请输入合法的邮箱地址, 格式为： xxxx@xxxx.com</p>
                        </div>
                        <button type="submit" class="btn btn-success"><i class="glyphicon glyphicon-edit"></i>更新
                        </button>
                        <button type="reset" class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 重置
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>


