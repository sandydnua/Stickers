<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <script src="<c:url value="/resources/js/jquery.js" />"></script>
    <script src="<c:url value="/resources/js/jquery.tmpl.js" />"></script>
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
        function hideAll() {
            $('[id ^=frame]').hide();
        }
        function editBoard(id) {
            $("input[name='currentBoard']").val(id);
            hideAll();
            $.get("getAllInfForBoard", {boardId:id}).done(function (data) {
                var boardInfo = JSON.parse(JSON.stringify(data));
                var title = boardInfo['board']["title"];
                var id = boardInfo['board']["id"];
                var groups = boardInfo['groups'];

                if (boardInfo['board']["exposed"]) {
                    $('#exposedBoard').prop('checked', true);
                }
                $("#editTitleBoard").val(title);
                $("#idBoard").empty();
                $("#newGroup").val("");
                $("#idBoard").append("Id:" + id );
                $("#buttonSaveTitleBoard").bind("click",{id:id}, saveTitleBoard);
                $('#groupsSelect').empty();
                for (var i=0; i < groups.length; i++) {
                     $('#groupsSelect').append('<option name="' + groups[i]['id'] + '">' + groups[i]['name'] + "</option>");
                }
                changeGroup();

            });

            $('#frame_editBoard').show();
        };

        function loadUsers() {
            $.get("loadUsers").done(function (data) {
                $('#users').empty();
                data.forEach(function (user) {
                    $('#users').append('<option name="' + user['id'] + '">' + user['firstName'] + " " + user['lastName'] + "</option>");
                });

            });
        }
        function deleteGroup() {
            var groupId = $('#groupsSelect option:checked').attr('name');
            if (groupId != null) {
                $.post("deleteGroup", {groupId : groupId}).done(function () {
                    editBoard($("input[name='currentBoard']").val());
                });
            }
        }
        function loadMembersGroup() {
            var groupId = $('#groupsSelect option:checked').attr('name');
            $.get("loadMembersGroup", {groupId : groupId}).done(function (data) {
                $('#membersGroup').empty();
                data.forEach(function (members) {
                    $('#membersGroup').append('<option name="' + members['accaunt']['id'] + '">' + members['accaunt']['firstName'] + " " + members['accaunt']['lastName'] + "</option>");
                });
            });
        }
        function saveMembersGroup() {
            var groupId = $('#groupsSelect option:checked').attr('name');

            var membersId = new Array();
            $('#membersGroup option').each(function (index, element) {
                membersId[index] = $(element).attr('name');
            });

            $.post( 'saveMembersGroup',
                {groupId : groupId, membersId:membersId}
            ).done(function () {
                changeGroup();
            });
        }

        function deleteMembersGroup() {
            $('#membersGroup option:checked').each(function (index, element) {
                $(element).remove();
            });
        }
        function addUser() {

            $("#users option:checked").each(function (index, item) {
                var name = $(item).val();
                var id = $(item).attr('name');
                if ( id != null && name != null) {
                    if ($('#membersGroup [name=' + id + ']').length == 0) {
                        $('#membersGroup').append('<option name="' + id + '">' + name + "</option>");
                    } else {
                        alert("Такой уже есть: " + $('#membersGroup [name=' + id + ']').length);
                    }
                } else {
                    //TODO
                    alert("Error addUser");
                }
            });

        }

        function changeGroup() {
            var groupId = $('#groupsSelect option:checked').attr('name');
                if (groupId ==null) {
                    $("#groupsSelect :first").attr("selected", "selected");
                    groupId = $('#groupsSelect option:checked').attr('name');
                }
                    $.get("getOperationsForGroup", {groupId: groupId}).done(function (data) {
                        operations = new Array();
                        for (var j = 0; j < data.length; j++) {
                            operations[j] = data[j]['operation']['name'];
                        }
                        $.get("getAllOperations", {boardId: groupId}).done(function (data) {
                            $('#operationsCheckBox').empty();
                            for (var i = 0; i < data.length; i++) {
                                if (operations.indexOf(data[i]['name']) != -1) {
                                    // есть в данной группе операция data[i]['name']
                                    $('#operationsCheckBox').append('<label><input type="checkbox" checked name="operation_' + data[i]['id'] + '" value="' + data[i]['id'] + '"/>' + data[i]["name"] + '</label><br>');
                                } else {
                                    $('#operationsCheckBox').append('<label><input type="checkbox" name="operation_' + data[i]['id'] + '" value="' + data[i]['id'] + '"/>' + data[i]["name"] + '</label><br>');
                                }
                            }
                        });
                    });
                    loadUsers();
                    loadMembersGroup();

        }
        function saveOperationsForGroup() {
            var groupId = $('#groupsSelect option:checked').attr('name');

            var operationsId = new Array();
            $('input[name^="operation_"]:checked').each(function (index, element) {
                operationsId[index] = $(element).val();
            });

            $.post( 'saveOperationsForGroup',
                    {groupId : groupId, operationsId:operationsId}
                  ).done(function () {
                      changeGroup();
                   });
        }
        function saveTitleBoard(eventObject) {
            $.post('saveTitleBoard',{boardId : eventObject.data.id, newTitle : $("#editTitleBoard").val()}).done(function () {
                editBoard(eventObject.data.id);
            });
        }
        function loadBoardsForEdit() {
            $.get("loadBoards").done(function (data) {
                $("#boards").empty();
                var boards = new Array();
                boards[0] = data;
                $('#boardsTmpl').tmpl(boards).appendTo('#boards');
            });
        };
        function insertBoard() {
            var title = $("input[name='titleNewBoard']").val();
            $.post("insertBoard", {title : title}).done(function () {
                loadBoardsForEdit();
                loadBoardForViewStickers();
            });
        }
        function insertSticker() {
            var text = $("input[name='textNewSticker']").val();
            var board = $("input[name='currentBoard']").val();
            $.post("insertSticker", {text : text, boardId : board}).done(function () {
                checkBoard(board);
            });
        }
        function deleteBoard(id) {
            $.post("deleteBoard", {id : id}).done(function () {
                loadBoardsForEdit();
                loadBoardForViewStickers();
            });
        }
        function loadBoardForViewStickers() {
            $.get("loadBoards").done(function (data) {
                $("#boardsButtons").empty();
                var boards = new Array();
                boards[0] = data;
                $('#boardsButtonsTmpl').tmpl(boards).appendTo('#boardsButtons');
            });
        };
        function showStickers() {
            loadBoardForViewStickers();
            hideAll();
            $('#frame_stickersInBoards').show();
        };
        function showBoards() {
            loadBoardsForEdit();
            hideAll();
            $('#frame_manageBoards').show();
        };
        function addGroup() {
            var newGroupName = $("#newGroup").val();
            var currentBoard = $("input[name='currentBoard']").val();

            if (newGroupName !="") {
                $.post("addGroup", {newGroupName:newGroupName, boardId:currentBoard}).done(function () {
                    editBoard(currentBoard);
                });
            } else {
                alert("пустое имя надопустимо");
            }
        }
        function changeExposedBoard() {
            var boardId = $("input[name='currentBoard']").val();
            if($('#exposedBoard').prop('checked')) {
                $.post('setExposedBoard', {boardId : boardId});
            } else {
                $.post('unsetExposedBoard', {boardId : boardId});
            }
            editBoard(boardId);
        }
        function checkBoard(board) {
            $("input[name='currentBoard']").val( board);

            $.get("loadStickers", {boardId : board}).done(function (data) {
                $("#stickers").empty();
                var stickers = new Array();
                var allPermittedOperations = data["allPermittedOperations"];
                if(allPermittedOperations.indexOf("read") !=-1) {

                    stickers[0] = data["stickers"];
                    if (allPermittedOperations.indexOf("update") !=-1 && allPermittedOperations.indexOf("delete") !=-1) {
                        $('#stickersUpDelTmpl').tmpl(stickers).appendTo('#stickers');
                        // можно удалять и редактировать стикеры
                    } else if (allPermittedOperations.indexOf("update") !=-1 && allPermittedOperations.indexOf("delete") ==-1) {
                        $('#stickersUpTmpl').tmpl(stickers).appendTo('#stickers');
                        // можно редактировать  и нельзя удалять
                    } else if (allPermittedOperations.indexOf("update") ==-1 && allPermittedOperations.indexOf("delete") !=-1) {
                        $('#stickersDelTmpl').tmpl(stickers).appendTo('#stickers');
                        // нельзя редактировать, но можно удалять
                    }
                }
            });
        }
        function createSticker() {
            var boardId = $("input[name='currentBoard']").val();
            var textSticker = $("input[name='newStickerText']").val();
            $.post("createSticker", { boardId: boardId, textSticker: textSticker}).done(function () {
                checkBoard(boardId);
            });
        };

        function  deleteSticker(stickerId) {
            var boardId = $("input[name='currentBoard']").val();
            $.post("deleteSticker", { stickerId: stickerId}).done(function () {
                checkBoard(boardId);
            });
        }
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
            <%--<input type="text"  hidden name="currentBoard"/>--%>
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
            <button onclick="deleteGroup()">Удалить выбранную</button>
            <br>
            <input id="newGroup" type="text"><button onclick="addGroup()">Добавить группу</button><br>
            Операции для выбранной группы:
            <div id="operationsCheckBox"></div>
            <button onclick="saveOperationsForGroup()">Сохранить операции</button><br>
            Члены группы:<br><select multiple size="10" id="membersGroup"></select>
            <button onclick="addUser()"> << </button>
            <select multiple size="10" id="users"></select>
            <br>
            <button onclick="deleteMembersGroup()">Удалить выбранных</button><br>
            <button onclick="saveMembersGroup()">Сохранить членов</button><br>


        </div>
    </div>

</body>
</html>
