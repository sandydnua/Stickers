
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
        $('#membersGroupTmpl').tmpl(data).appendTo('#membersGroup');
    });
}
function deleteAllMembersFromSelectedGroup() {
    var groupId = $('#groupsSelect option:checked').attr('name');
    $.post("deleteAllMembersFromGroup", {groupId : groupId}).done(function (data) {
        $('#membersGroup').empty();
    });
}

function deleteMemberFromSelectedGroup(memberIdInGroup) {
    $.post("deleteMemberFromGroup", {memberIdInGroup : memberIdInGroup}).done(function (data) {
        $('#member_' + memberIdInGroup).empty();
    });
};
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
function addUserInSelectedGroup() {

        // TODO надо атрибусты name переделать...
    //TODO надо сделать проверку на наличие этого юзера уже в группе
        var groupId = $('#groupsSelect option:checked').attr('name');
        var userId = $("#users option:checked").attr('name');
        $.post("addUserInSelectedGroup", {userId : userId, groupId : groupId}).done(function () {
            loadMembersGroup();
        });
};

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
                    $('#operationsCheckBox').append('<label><input onchange="setOperationStatusForCurrentGroup(\'' + data[i]['id'] + '\')" type="checkbox" checked name="operation_' + data[i]['id'] + '" value="' + data[i]['id'] + '"/>' + data[i]["name"] + '</label><br>');
                } else {
                    $('#operationsCheckBox').append('<label><input onchange="setOperationStatusForCurrentGroup(\'' + data[i]['id'] + '\')" type="checkbox" name="operation_' + data[i]['id'] + '" value="' + data[i]['id'] + '"/>' + data[i]["name"] + '</label><br>');
                }
            }
        });
    });
    loadUsers();
    loadMembersGroup();

}
function setOperationStatusForCurrentGroup(operationId) {
    var groupId = $('#groupsSelect option:checked').attr('name');
    var checked = $('input[name="operation_' + operationId + '"]').is(":checked");

    $.post("setOperationStatus", {groupId : groupId, operationId : operationId, checked : checked}).done(function () {
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