<%@ page import="stickers.database.entity.Accaunt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    .header { //TODO надо както подключить подругому. это не видит код
        background-color: #90ffff;
    }
</style>
<div id="header" style="background-color: aliceblue">
    <% if( session.getAttribute("accaunt") != null) {%>
        Id: <strong><%=((Accaunt)session.getAttribute("accaunt")).getId()%></strong><br>
        Имя: <strong><%=((Accaunt)session.getAttribute("accaunt")).getFirstName()%></strong><br>
        <a href="profile">Редактировать профиль</a><br>
        <a href="signout">Выйти</a><br>
    <% } else {%>
        <div id="needsignin">Надо войти</div>
    <% } %>
</div>