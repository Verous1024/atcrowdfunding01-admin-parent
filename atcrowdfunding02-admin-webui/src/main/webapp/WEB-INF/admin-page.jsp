<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/2/16
  Time: 上午 11:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">

<%@ include file="/WEB-INF/include-head.jsp" %>
<link rel="stylesheet" href="css/pagination.css"/>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>

<script type="text/javascript">
    $(function () {
        initPagination();
    });

    function initPagination() {

        var totalRecord =${requestScope.pageInfo.total};

        var properties = {
            num_edge_entries: 3,
            items_per_page:${requestScope.pageInfo.pageSize},
            num_display_entries: 5,
            current_page:${requestScope.pageInfo.pageNum-1},
            next_text: "下一页",
            prev_text: "上一页",
            callback: pageSelectCallback
        };
        $("#Pagination").pagination(totalRecord, properties);
    }

    function pageSelectCallback(pageIndex, jQuery) {
        var pageNum = pageIndex + 1;
        window.location.href = "admin/get/page.html?pageNum=" + pageNum + "&keyword=${param.keyword}";
        return false;
    }

    //删除按键，删除单个用户，注意点：由于多个按键使用foreach生成，不能使用id，应该使用class
    //url是自定义属性，可以通过attr获取，直接在脚本中使用，无法获取到admin.id，这点需要注意！
    //绑定点击事件，由于按键是后面创建的，不能使用xx.click的方式，只能使用onclik
    /*    $(document).on("click",".delete_single_user_btn",function () {
            if ( confirm("确定要删除"+$(this).parents("tr").find("td:eq(2)").text())+"用户吗？" ) {
                window.location.href = $(this).attr("url");
            } else {
                return false;
            }
        });*/
    $(document).on("click", ".delete_single_user_btn", function () {
        var url = $(this).attr("url");
        var information = "确定要删除【" + $(this).parents("tr").find("td:eq(2)").text() + "】用户吗？";
        layer.confirm(information,
            {icon: 3, title: '提示', btn: ['确定', '取消']},
            function (cindex) { //确定回调
                window.location.href = url;
                layer.close(cindex);
            },
            function (cindex) { //取消回调
                layer.close(cindex);
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
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form action="admin/get/page.html" method="post" class="form-inline" role="form"
                          style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input name="keyword" class="form-control has-success" type="text"
                                       value="${param.keyword == null ? "":param.keyword}"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <%--<button type="button" class="btn btn-primary" style="float:right;"
                            onclick="window.location.href='add.html'"><i class="glyphicon glyphicon-plus"></i> 新增
                    </button>--%>
                    <a href="admin/to/add/page.html" type="button" class="btn btn-primary" style="float:right;"
                       onclick="window.location.href='add.html'"><i class="glyphicon glyphicon-plus"></i> 新增
                    </a>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox"></th>
                                <th>账号</th>
                                <th>名称</th>
                                <th>邮箱地址</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>

                            <tbody>

                            <c:if test="${empty requestScope.pageInfo.list}">
                                <td colspan="6" align="center">抱歉！没有查询到您要的数据！</td>
                            </c:if>
                            <c:if test="${!empty requestScope.pageInfo.list}">
                                <c:forEach items="${requestScope.pageInfo.list}" var="admin" varStatus="myStatus">
                                    <tr>
                                        <td>${myStatus.count}</td>
                                        <td><input type="checkbox"></td>
                                        <td>${admin.loginAcct}</td>
                                        <td>${admin.userName}</td>
                                        <td>${admin.email}</td>
                                        <td>
                                                <%-- <button type="button" class="btn btn-success btn-xs">
                                                  <i class=" glyphicon glyphicon-check"></i></button>--%>
                                            <a href="assign/to/assign/role/page.html?adminId=${admin.id }&pageNum=${requestScope.pageInfo.pageNum }&keyword=${param.keyword }"
                                               class="btn btn-success btn-xs">
                                                <i class=" glyphicon glyphicon-check"></i></a>
                                                <%--<button type="button" class="btn btn-primary btn-xs">
                                                    <i class=" glyphicon glyphicon-pencil"></i></button>--%>
                                            <a href="admin/to/edit/page.html?adminId=${admin.id}&pageNum=${requestScope.pageInfo.pageNum}&keyword=${param.keyword}"
                                               class="btn btn-primary btn-xs">
                                                <i class=" glyphicon glyphicon-pencil"></i></a>
                                                <%--<a href="admin/remove/${admin.id}/${requestScope.pageInfo.pageNum}/${param.keyword}.html"
                                                     class="btn btn-danger btn-xs">
                                                     <i class=" glyphicon glyphicon-remove"></i></a>--%>
                                            <button url="admin/remove/${admin.id}/${requestScope.pageInfo.pageNum}/${param.keyword}.html"
                                                    type="button" class="btn btn-danger btn-xs delete_single_user_btn">
                                                <i class=" glyphicon glyphicon-remove"></i></button>
                                        </td>
                                    </tr>
                                </c:forEach>

                            </c:if>

                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div> 一共查询到 <b class="bg-success">${requestScope.pageInfo.total}</b> 条记录！</div>
                                    <div id="Pagination" class="pagination"> <!-- 这里显示分页 -->
                                    </div>
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


