<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/2/16
  Time: 上午 11:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@ include file="/WEB-INF/include-head.jsp" %>
<link href="ztree/zTreeStyle.css" rel="stylesheet"/>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="crowd/my-menu.js"></script>
<script type="text/javascript">
    $(function () {
        generateTree();

        //给所有的添加角色按钮绑定单击事件
        $("#treeDemo").on("click", ".addBtn", function () {
            //将当前节点的id，作为新节点的pid保存到去哪句
            window.pid = this.id;
            //打开模态框
            $("#menuAddModal").modal("show");

            return false;
        });

        $("#menuSaveBtn").click(function () {
            // 收集表单项中用户输入的数据
            var name = $.trim($("#menuAddModal [name=name]").val());
            var url = $.trim($("#menuAddModal [name=url]").val());

            // 单选按钮要定位到“被选中”的那一个
            var icon = $("#menuAddModal [name=icon]:checked").val();

            // 发送 Ajax 请求
            $.ajax({
                "url": "menu/save.json",
                "type": "post",
                "data": {
                    "pid": window.pid,
                    "name": name,
                    "url": url,
                    "icon": icon
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！", {
                            time: 2000,
                            icon: 1
                        });
                        // 重新加载树形结构，注意：要在确认服务器端完成保存操作后再刷新
                        // 否则有可能刷新不到最新的数据，因为这里是异步的
                        generateTree();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败！" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + "" + response.statusText);
                }
            });


            // 关闭模态框
            $("#menuAddModal").modal("hide");

            // 清空表单
            // jQuery 对象调用 click()函数，里面不传任何参数，相当于用户点击了一下
            $("#menuResetBtn").click();
        });

        $("#treeDemo").on("click", ".editBtn", function () {
            //保存id到全局属性中
            window.id = this.id;
            //打开模态框
            $("#menuEditModal").modal("show");
            //获取zTreeObj对象，即整个Ztree
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
            // 请求参数的key
            //用来搜索节点的属性键/值对，根据这个键值对搜索整个Ztree上的对应的Node
            var key = "id";
            var value = window.id;
            var currentNode = zTreeObj.getNodeByParam(key, value);
            //执行回显
            $("#menuEditModal [name=name]").val(currentNode.name);
            $("#menuEditModal [name=url]").val(currentNode.url);
            $("#menuEditModal [name=icon]").val([currentNode.icon]);
            return false;
        });

        $("#menuEditBtn").click(function () {
            var name = $("#menuEditModal [name=name]").val();
            var url = $("#menuEditModal [name=url]").val();
            var icon = $("#menuEditModal [name=icon]:checked").val();

            $.ajax({
                url: "menu/update.json",
                type: "post",
                data: {
                    "id": window.id,
                    "name": name,
                    "url": url,
                    "icon": icon
                },
                dataType: "json",
                success: function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！", {
                            time: 2000,
                            icon: 1
                        });
                        // 重新加载树形结构，注意：要在确认服务器端完成保存操作后再刷新
                        // 否则有可能刷新不到最新的数据，因为这里是异步的
                        generateTree();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败！" + response.message);
                    }
                },
                error: function (rsponse) {
                    layer.msg(response.status + "" + response.statusText);
                }
            });
            $("#menuEditModal").modal("hide");
        });

        //给所有的删除按键绑定弹出动态狂的事件
        $("#treeDemo").on("click", ".removeBtn", function () {
            window.id = this.id;

            $("#menuConfirmModal").modal("show");

            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
            var currentNode = zTreeObj.getNodeByParam("id", window.id);

            $("#removeNodeSpan").html("【<i class='" + currentNode.icon + "'></i>"+":" + currentNode.name + "】");

            return false;
        });

        //给删除的确认按键绑定删除的单即事件
        $("#confirmBtn").click(function () {
            $.ajax({
                url: "menu/remove.json",
                type: "post",
                data: {
                    "id": window.id
                },
                dataType: "json",
                success: function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！", {
                            time: 2000,
                            icon: 1
                        });
                        // 重新加载树形结构，注意：要在确认服务器端完成保存操作后再刷新
                        // 否则有可能刷新不到最新的数据，因为这里是异步的
                        generateTree();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败！" + response.message);
                    }
                },
                error: function (rsponse) {
                    layer.msg(response.status + "" + response.statusText);
                }
            });
            $("#menuConfirmModal").modal("hide");
        });
    });


</script>
<body>

<%@ include file="/WEB-INF/include-nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%@ include file="/WEB-INF/include-sidebar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <div class="panel panel-default">
                <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <div class="panel-body">
                    <ul id="treeDemo" class="ztree" style="user-select: none;">
                        <%-- 填充ztree --%>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/modal-menu-add.jsp" %>
<%@ include file="/WEB-INF/modal-menu-edit.jsp" %>
<%@ include file="/WEB-INF/modal-menu-confirm.jsp" %>

</body>
</html>


