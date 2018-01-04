<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="<c:url value="/resources/js/jquery.js" />"></script>
    <script src="<c:url value="/resources/js/registration.js" />"></script>

    <style>
        td {
            color: #979999;
            font-family: sans-serif;
        }
        img {
            height: 20px;
            width: 20px;
        }
    </style>

    <title>Profile</title>
</head>
<body>
    <%@include  file="header.jsp"%>
    <div id="main">
        <table>
            <tr>
                <td>Id</td>
                <td><%=((Accaunt) session.getAttribute("accaunt")).getId()%></td>
                <td><button onclick="save">Сохранить</button></td>
            </tr>
        </table>
    </div>
</body>
</html>
