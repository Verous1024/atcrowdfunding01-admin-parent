//声明专门的函数显示确认模态框
function showConfirmModal(roleArray) {
    //打开模态框
    $("#confirmModal").modal("show");

    //清空span中的数据
    $("#roleNameDiv ").empty();

    //创建全局变量，使得弹出模态框时，不显示的id被保存在全局变量中，方便删除
    //并且能够清空上一次的删除记录
    window.roleIdArray = [];

    //遍历roleArray数组
    for (var i = 0;i<roleArray.length;i++) {
        var role = roleArray[i];
        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName+"</br>");
        //保存每一个需要删除的id值
        window.roleIdArray.push(role.roleId);
    }
}

//执行分页，生成页面效果，任何时候调用这个函数都会重新加载页面
function generatePage() {
    var pageInfo = getPageInfoRemote();
    fillTableBody(pageInfo);
}

//获取访问服务器端程序获取pageinfo数据
function getPageInfoRemote() {

    var ajaxResult = $.ajax({
        url: "role/get/page/info.json",
        data: {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        type: "POST",
        async: false,
        dataType: "json"
    });

    //判断当前状态码
    var statusCode = ajaxResult.status;


    // 如果当前请求失败，请求码非200
    if (statusCode != 200) {
        layer.msg("失败！响应状态码=" + statusCode + "说明信息=" + ajaxResult.statusText,
            {
                time: 2000,//2s后自动关闭
                btn: ['知道了', '关闭'],
                icon: 5
            });
        return null;
    }

    //如果响应码为200，表名成功
    var resultEntity = ajaxResult.responseJSON;
    var result = resultEntity.result;

    if (result == "FAILED") {
        layer.msg(resultEntity.message,
            {
                time: 2000,//2s后自动关闭
                btn: ['知道了', '关闭'],
                icon: 2
            });
    }
    var pageInfo = resultEntity.data;
    return pageInfo;
}

//填充表格
function fillTableBody(pageInfo) {
    $("#rolePageBody").empty();
    $("#Pagination").empty();

    //判断pageInfo是否有效
    if (pageInfo == null || pageInfo == undefined || pageInfo.list == null || pageInfo.list.length == 0) {
        $("#rolePageBody").append("<tr><td colspan='4'> 抱歉！没有查询到您搜索的数据！</td></tr>");
        return ;
    }

    //使用pageInfo填充表格
    for(var i=0; i<pageInfo.list.length ; i++){
        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.name;
        var numberTd = "<td>"+(i+1)+"</td>";
        var checkboxTd = "<td><input roleId='"+roleId+"' type='checkbox'class='itemBox'></td>";
        var roleNameTD = "<td>"+roleName+"</td>";
        var checkBtn = "<button type='button' class='btn btn-success btn-xs'><i class=' glyphicon glyphicon-check'></i></button>";
        /* 自定义属性 roleId， 自定义cllas标签 pencilBtn*/
        var pencilBtn = "<button roleId='"+roleId+"' type='button' class='btn btn-primary btn-xs pencilBtn'><i class=' glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button roleId='"+roleId+"' type='button' class='btn btn-danger btn-xs removeBtn'><i class=' glyphicon glyphicon-remove'></i></button>";
        var buttonTd = "<td>"+checkBtn+" "+pencilBtn+" "+removeBtn+"</td>";
        var tr = "<tr>"+numberTd+checkboxTd+roleNameTD+buttonTd+"</tr>";
        $("#rolePageBody").append(tr);
    }

    generateNavigator(pageInfo);
}

//生成分页的导航条
function generateNavigator(pageInfo) {
    //总记录数
    var totalRecord =pageInfo.total;

    //相关属性设置
    var properties = {
        num_edge_entries: 3,
        items_per_page:pageInfo.pageSize,
        num_display_entries: 5,
        current_page:pageInfo.pageNum-1,
        next_text: "下一页",
        prev_text: "上一页",
        callback: paginationCallBack
    };

    //调用pagination函数
    $("#Pagination").pagination(totalRecord, properties);
}

//分页按钮的callBack函数
function paginationCallBack(pageIndex, jQuery) {
    var pageNum = pageIndex + 1;
    window.pageNum = pageNum;
    generatePage();
    //取消按钮的默认行为
    return false;
}

