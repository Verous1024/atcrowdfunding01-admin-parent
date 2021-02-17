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
<link rel="stylesheet" href="css/pagination.css"/>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<script type="text/javascript" src="crowd/my-role.js" charset="UTF-8"></script>
<script type="text/javascript">
    $(function () {
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";
        generatePage();

        $("#searchBtn").click(function () {
            window.keyword = $("#keywordInput").val();
            generatePage();
        });

        $("#showAddModalBtn").click(function () {
            $("#addModal").modal({
                show: true
            })
        });

        $("#saveRoleBtn").click(function () {
            var roleName = $.trim($("#addModal [name=roleName]").val());
            $.ajax({
                url: "role/save.json",
                type: "post",
                data: {
                    "name": roleName
                },
                dataType: "json",
                success: function (response) {
                    var result = response.result;

                    if (result == "SUCCESS") {
                        layer.msg("操作成功！",
                            {
                                time: 1000,//2s后自动关闭
                                icon: 1
                            });
                        //重新加载分页数据，并重新生成页面
                        window.pageNum = 999999;
                        generatePage();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败" + response.message,
                            {
                                time: 2000,//2s后自动关闭
                                icon: 5
                            });
                    }
                },
                error: function (response) {
                    layer.msg("失败！响应状态码【" + response.status + "】，错误信息【 " + response.statusText + "】！",
                        {
                            time: 2000,//2s后自动关闭
                            btn: ['知道了', '关闭'],
                            icon: 5
                        });
                }
            });

            //不管成功与否，都要关闭模态框
            $("#addModal").modal("hide");

            //清理模态框的内容
            $("#addModal [name=roleName]").val("");

        });

        //点击pencil标签回显数据的模态框
        $("#rolePageBody").on("click", ".pencilBtn", function () {
            $("#editModal").modal("show");
            //获取当前待编辑的角色名称
            var roleName = $(this).parent().prev().text();
            //获取当前角色的id,并存放在全局上，方便后面执行更新按钮时获取该值
            window.roleId = $(this).attr("roleId");
            //回显在模态框
            $("#editModal [name=roleName]").val(roleName);
        });

        //点击修改模态框的保存按钮，发送ajax请求保存数据
        $("#updateRoleBtn").click(function () {
            //获取文本框新的角色名
            var roleName = $("#editModal [name=roleName]").val();
            //发送ajax请求，执行更新
            $.ajax({
                url: "role/update.json",
                data: {
                    "id": window.roleId,
                    "name": roleName
                },
                type: "post",
                success: function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！",
                            {
                                time: 1000,//2s后自动关闭
                                icon: 1
                            });
                        //重新加载分页数据，并重新生成页面

                        generatePage();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败" + response.message,
                            {
                                time: 2000,//2s后自动关闭
                                icon: 5
                            });
                    }
                },
                error: function (response) {
                    layer.msg("失败！响应状态码【" + response.status + "】，错误信息【 " + response.statusText + "】！",
                        {
                            time: 2000,//2s后自动关闭
                            btn: ['知道了', '关闭'],
                            icon: 5
                        });
                }
            });

            //不管成功与否，都要关闭模态框
            $("#editModal").modal("hide");
        });

        //点击确认模态框的确认删除按钮执行删除
        $("#removeRoleBtn").click(function () {
            console.log(window.roleIdArray);
            var responseBody = JSON.stringify(window.roleIdArray);
            $.ajax({
                url:"role/remove/by/role/id/array.json",
                data:responseBody,
                type:"POST",
                contentType:"application/json;charset=UTF-8",
                dataType:"json",
                success:function(response){
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！",
                            {
                                time: 1000,//2s后自动关闭
                                icon: 1
                            });
                        generatePage();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败" + response.message,
                            {
                                time: 2000,//2s后自动关闭
                                icon: 5
                            });
                    }
                },
                error:function (response) {
                    layer.msg("失败！响应状态码【" + response.status + "】，错误信息【 " + response.statusText + "】！",
                        {
                            time: 2000,//2s后自动关闭
                            btn: ['知道了', '关闭'],
                            icon: 5
                        });
                }
            });

            //不管成功与否，都要关闭模态框
            $("#editModal").modal("hide");
        });

        //给单条删除按钮绑定单击响应事件
        $("#rolePageBody").on("click", ".removeBtn", function () {
            //获取对应删除按钮的roleName
            //注意：roleName用来回显，roleId用来作为删除条件
            var roleName = $(this).parent().prev().text();
            var roleArray =[{
                roleId:$(this).attr("roleId"),
                roleName:roleName
            }];

            //调用专门函数打开模态框
            showConfirmModal(roleArray);
        });

        //给总的checkBOx绑定单击事件
        $("#summaryBox").click(function () {
            //获取当前总的按钮的状态
            var currentStatus = this.checked;
            //给所有子按钮
            $(".itemBox").prop("checked", currentStatus);
        })

        //全选、全不选的反向查欧洲
        $("#rolePageBody").on("click",".itemBox",function () {
            var totalBox = $(".itemBox").length;
            var checkedBox = $(".itemBox:checked").length;

            $("#summaryBox").prop("checked", totalBox == checkedBox);
        });

        //给批量删除的按钮绑定单击响应事件
        $("#batchRemoveBtn").click(function () {

            //创建一个数组对象存放Role
            var roleArray = [];

            //获取当前被选中的框的数量
            var checkedBox = $(".itemBox:checked").length;

            //如果选中0需要提醒
            if (checkedBox == 0) {
                layer.msg("请至少选中一个！");
                return;
            } else { //非0各就存储到数组中
                $(".itemBox:checked").each(function () {
                    //获取RoleID
                    var roleId = $(this).attr("roleId");
                    //获取roleName
                    var roleName = $(this).parent().next().text();
                    roleArray .push({
                        "roleId":roleId,
                        "roleName":roleName
                    });
                });
                showConfirmModal(roleArray);
            }
        });
    });

</script>

<body>
<%@ include file="/WEB-INF/modal-role-add.jsp" %>
<%@ include file="/WEB-INF/modal-role-edit.jsp" %>
<%@ include file="/WEB-INF/modal-role-confirm.jsp" %>
<%@ include file="/WEB-INF/include-nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%@ include file="/WEB-INF/include-sidebar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i
                                class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button id="batchRemoveBtn" type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button id="showAddModalBtn" type="button" class="btn btn-primary" style="float:right;"><i
                            class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody">

                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"/> <!-- 这里显示分页 -->
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>


