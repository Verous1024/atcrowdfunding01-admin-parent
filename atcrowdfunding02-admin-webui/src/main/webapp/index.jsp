<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/2/15
  Time: 下午 06:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <base href="http://${pageContext.request.serverName }:${pageContext.request.serverPort }${pageContext.request.contextPath}/"/>
    <title>Title</title>
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#btn1").click(function () {
                alert("开始ajax请求！");
                $.ajax(
                    {
                        url:"test/btn1.json",
                        type: "GET",
                        success:function (result) {
                            console.log();
                            alert("结束over！");
                        }
                    }
                )
            });

            $("#btn2").click(function () {
                layer.msg("layer的弹框");
            });
        });
    </script>
</head>
<body>

<a href="test/ssm.html">测试SSM整和环境</a><br/>

<button id="btn1">send easy</button><br/>

<button id="btn2">点我弹框</button><br/>
</body>
</html>
