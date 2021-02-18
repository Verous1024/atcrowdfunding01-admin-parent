<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        //给添加到分配角色表添加效果
        $("#toRightBtn").click(function () {
            $("select:eq(0)>option:selected").appendTo("select:eq(1)");
        });

        //给添加到未分配角色表添加效果
        $("#toLeftBtn").click(function () {
            $("select:eq(1)>option:selected").appendTo("select:eq(0)");
        })

        //将未分配列表的所有表示为选中装填，这样才会发送过去
        $("#submitBtn").click(function(){
            $("select:eq(1)>option").prop("selected", "selected");
        })

    });

</script>

<body>

<%@ include file="/WEB-INF/include-nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%@ include file="/WEB-INF/include-sidebar.jsp" %>

        <div class="panel panel-default">
            <div class="panel-body">
                <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                    <ol class="breadcrumb">
                        <li><a href="admin/to/main/page.html">首页</a></li>
                        <li><a href="admin/get/page.html">数据列表</a></li>
                        <li class="active">分配角色</li>
                    </ol>
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <form action="assign/do/role/assign.html" method="post" role="form" class="form-inline">
                                <input type="hidden" name="adminId" value="${param.adminId}"/>
                                <input type="hidden" name="pageNum" value="${param.pageNum}"/>
                                <input type="hidden" name="keyword" value="${param.keyword}"/>
                                <div class="form-group">
                                    <label for="exampleInputPassword1">未分配角色列表</label><br>
                                    <select id="exampleInputPassword1" class="form-control" multiple="multiple" size="10"
                                            style="width:100px;overflow-y:auto;">

                                        <c:forEach items="${requestScope.unAssignedRoleList}" var="role">
                                            <option value="${role.id}">${role.name}</option>
                                        </c:forEach>

                                    </select>
                                </div>
                                <div class="form-group">
                                    <ul>
                                        <li id="toRightBtn"
                                            class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                        <br>
                                        <li id="toLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left"
                                            style="margin-top:20px;"></li>
                                    </ul>
                                </div>
                                <div class="form-group" style="margin-left:40px;">
                                    <label for="exampleInputPassword2">已分配角色列表</label><br>
                                    <select name="roleIdList" id="exampleInputPassword2" class="form-control"
                                            multiple="multiple" size="10"
                                            style="width:100px;overflow-y:auto;">

                                        <c:forEach items="${requestScope.assignedRoleList}" var="role">
                                            <option value="${role.id}">${role.name}</option>
                                        </c:forEach>

                                    </select>
                                </div>
                                <div style="width:450px;margin:20px auto 10px 0px" class="alert alert-warning alert-dismissible" role="alert">
                                    <button  type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                                            aria-hidden="true">&times;</span></button>
                                    <strong>Tips:</strong> 使用alt或者shift能够同时选择多个角色继续分配。
                                </div>
                                    <button id="submitBtn" type="submit" style="width: 100px; margin: 10px auto 0px 120px"
                                            class="btn btn-sm btn-success btn-block">保存
                                    </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

</body>
</html>


