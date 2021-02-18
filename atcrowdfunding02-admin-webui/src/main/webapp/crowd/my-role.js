//填充分配权限的模态框中的树形结构数据
function fillAuthTree() {
    //1、发送ajax请求查询Auth数据
    var ajaxResult = $.ajax({
        url: "assign/get/all/auth.json",
        type:"post",
        dataType:"json",
        async:false
    });
    if (ajaxResult.status != 200) {
        layer.msg("失败！响应状态码【" + ajaxResult.status + "】，错误信息【 " + ajaxResult.statusText + "】！",{
            icon:5,
            time:2000
        });
        return; //只要出错，就要返回
    }
    //2、从响应结果中获取Auth的JSon数据
    var authList = ajaxResult.responseJSON.data;

    //3、准备对zTree进行设置的JSON对象
    var setting ={
        data:{
            simpleData:{
                //开启简单JSON功能，将树的结点交给zTree自行处理，而不是我们在Handler中处理后返回
                enable:true,
                //简单JSON功能，zTree将根据默认的Pid区查询其父节点，从而形成Tree，我们这里需要修改按照categoryId查找父节点
                pIdKey:"categoryId"
            },
            key:{
                //zTree默认根据name树形进行显示节点，这里将其改为根据title字段显示节点，不使用默认的name
                name:"title"
            }
        },
        check:{
            //所有树节点上都显示选择框
            enable:true
        }
    };

    //4、生成树形结构
    //<ul id="authTreeDemo" class="ztree"></ul>
    $.fn.zTree.init($("#authTreeDemo"),setting,authList);

    //生成树形结构后，获取zTreeObj对象，然后调用其方法展开整棵树
    var zTreeObject = $.fn.zTree.getZTreeObj("authTreeDemo");

    //调用zTreeObj的方法展开树
    zTreeObject.expandAll(true);

    //5、查询该角色--已分配的Auth的id组成的数组
    ajaxResult = $.ajax({
        url:"assign/get/assigned/auth/id/by/role/id.json",
        type:"post",
        data:{
            "roleId":window.roleId
        },
        dataType:"json",
        async:false
    });

    if (ajaxResult.status != 200) {
        layer.msg("失败！响应状态码【" + ajaxResult.status + "】，错误信息【 " + ajaxResult.statusText + "】！",{
            icon:5,
            time:2000
        });
        return; //出错，返回停止继续操作
    }

    //从响应结果中获取authIdArray
    var authIdArray = ajaxResult.responseJSON.data;


    //6,根据authIdArray把树形结构的节点勾选上，即【【回显】】
    //遍历authIdArray
    for(var i =0;i<authIdArray.length;i++){
        var authId = authIdArray[i];
        //根据id选择对应的树节点
        var treeNode = zTreeObject.getNodeByParam("id", authId);
        //表示节点是否勾选上的属性值
        var checked = true;
        //关闭联动，权限分配需要单独点
        var checkTypeFlag =false;

        //执行
        zTreeObject.checkNode(treeNode, checked, checkTypeFlag);
    }

}

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

        var checkBtn = "<button id='"+roleId+"' type='button' class='btn btn-success btn-xs checkBtn'><i class=' glyphicon glyphicon-check'></i></button>";
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

