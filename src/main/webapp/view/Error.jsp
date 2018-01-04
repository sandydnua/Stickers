<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<script src="<c:url value="/resources/js/jquery.js" />"></script>
<script src="<c:url value="/resources/js/jquery.tmpl.js" />"></script>

<head>
    <title>Error</title>
</head>
<body>
<%@include  file="header.jsp"%>
<div id="error">
    <h3><%= (String)session.getAttribute("errorMsg")%></h3>
</div>

</body>
</html>
