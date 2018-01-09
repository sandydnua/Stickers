<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <script src="<c:url value="/resources/js/jquery.js" />"></script>
    <script src="<c:url value="/resources/js/jquery.tmpl.js" />"></script>
    <script src="<c:url value="/resources/js/mainpage.js" />"></script>
    <style type="text/css">
        .memberOfGroup {
            color: white;
            padding: 8px;
            font-family: Arial;
            background-color:  #4CAF50;
            border-spacing: 5px;
        }
    </style>
    <script type="text/javascript">
        $(window).on("load", function(){
            // TODO тут полно костылей !!!
            <%
                boolean registration = false;
                if( session.getAttribute("accaunt") != null){
                    registration = true;
                }
                Integer requestedBoardId = -1;
                if( session.getAttribute("requestedBoardId") != null){
                    requestedBoardId = (Integer) session.getAttribute("requestedBoardId");
                }
            %>
            if ( "<%= registration%>" == "false" ) {
                if ("<%= requestedBoardId%>" != -1 ) {
                    var divForStickers = $("#stickersInBoards");
                    $("#main").empty();
                    $("#main").append(divForStickers);
                    checkBoard("<%= requestedBoardId%>");
                    hideAll();
                    showStickers();
                } else {
                    // TODO вроде attr плохо работает или устарел. надо посмотреть .prop()
                    $(location).attr('href',"mainpage");
                }
            } else {
                loadBoardsForEdit();
                loadBoardForViewStickers();
                hideAll();
                showBoards();
            }
        });
    </script>


    <script id="boardsTmpl" type="text/x-jquery-tmpl">
         <table border="1" id="boardsTable">
            <tr>
                <th>Title</th>
                <th></th>
                <th></th>
            </tr>
            {%each $data%}
                <tr>
                    <td>{%= $value.title %}</td>
                    <td><button  onclick='editBoard("{%= $value.id %}")'>Edit</button></td>
                    <td><button  onclick='deleteBoard("{%= $value.id %}")'>Delete</button></td>
                </tr>
            {%/each%}
            <tr>
                <td><form id='insertBoard' method='post'>
                    <input  type='text' name='titleNewBoard'/>
                </td>
                <td><button  onclick='insertBoard()'>Добавить</button></form></td>
                <td></td>
            </tr>
        </table>
    </script>
    <script id="stickersUpDelTmpl" type="text/x-jquery-tmpl">
        {%each $data%}
             <div id="sticker_{%= $value.id%}">
                <div id="stickerText">
                    {%= $value.text %}
                </div>
                <button onclick='deleteSticker("{%= $value.id %}")'>Удалить</button>
                <button onclick='editSticker("{%= $value.id %}")'>Редактировать</button>
             </div>
        {%/each%}
        <input type="text" name="newStickerText"/><button onclick="createSticker()">Создать</button>
    </script>
    <script id="stickersUpTmpl" type="text/x-jquery-tmpl">
        {%each $data%}
             <div id="sticker_{%= $value.id%}">
                <div id="stickerText">
                    {%= $value.text %}
                </div>
                <button onclick='editSticker("{%= $value.id %}")'>Редактировать</button>
             </div>
        {%/each%}
        <input type="text" name="newStickerText"/><button onclick="createSticker()"></button>
    </script>
    <script id="stickersDelTmpl" type="text/x-jquery-tmpl">
        {%each $data%}
             <div id="sticker_{%= $value.id%}">
                <div id="stickerText">
                    {%= $value.text %}
                </div>
                <button onclick='deleteSticker("{%= $value.id %}")'>Редактировать</button>
             </div>
        {%/each%}
        <input type="text" name="newStickerText"/><button onclick="createSticker()"></button>
    </script>
    <script id="boardsButtonsTmpl" type="text/x-jquery-tmpl">
        {%each $data%}
             <button onclick='checkBoard("{%= $value.id %}")'>{%= $value.title %}</button><br>
        {%/each%}
    </script>
    <script id="membersGroupTmpl" type="text/x-jquery-tmpl">
        <div id="member_{%= $data.id %}">
            <span class="memberOfGroup">
                {%= $data.id %} {%= $data.firstName %} {%= $data.lastName %}
            </span>
            <img src="<c:url value="/resources/img/deleteMember.png" />" onClick='deleteMemberFromSelectedGroup("{%= $data.id %}")'>
        </div>
        <br>
    </script>
<head>
    <title>Main</title>
</head>
<body>
    <%@include  file="header.jsp"%>
    <div id="main">
        <input type="text"  hidden name="currentBoard"/>
        <button onclick="showStickers()">Stickers</button>
        <button onclick="showBoards()">Boards</button>
        <div id="frame_manageBoards">
            <h3>Manage</h3>
            <div id="boards"></div>
        </div>
        <div id="frame_stickersInBoards">
            <h3>Stickers</h3>
            <table border="1">
                <tr>
                    <td>
                        <div id="boardsButtons"></div>
                    </td>
                    <td>
                        <div id="stickers"></div>
                    </td>
                </tr>
            </table>
        </div>
        <div id="frame_editBoard">
            <div id="idBoard"></div>
            <input id="editTitleBoard" type="text"><button id="buttonSaveTitleBoard">Сохранить</button><br>
            Доступы:<br>
            <label><input onchange="changeExposedBoard()" id="exposedBoard" type="checkbox"/>Открыт для всех по сслыке</label><br>
            Выбери группу:<select size="5" id="groupsSelect" onchange="changeGroup()" ></select>
            <br>
            <button onclick="deleteGroup()">Удалить выбранную</button>
            <br>
            <button onclick="deleteAllMembersFromSelectedGroup()">Очистить выбранную</button>
            <br>
            <input id="newGroup" type="text"><button onclick="addGroup()">Добавить группу</button><br>
            Операции для выбранной группы:
            <div id="operationsCheckBox"></div>
            <div>Члены группы:</div>
            <br><div id="membersGroup"></div>
            <br>
            <select size="10" id="users" ondblclick="addUserInSelectedGroup()"></select>

        </div>
    </div>

</body>
</html>
