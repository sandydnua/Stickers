package stickers.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stickers.database.MemberOfGroup;
import stickers.database.entity.*;
import stickers.services.DbServices;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
public class RestService {

    @Autowired
    private DbServices dbServices;

    @GetMapping("absetEmail")
    public boolean existsEmail(@RequestParam(name = "email") String email) {
        return dbServices.exsistsAccauntWithEamil(email);
    }

    @GetMapping("loadBoards")
    public List<Boards> getBoardsForCreator(HttpSession session){

        Accaunt creator = (Accaunt)session.getAttribute("accaunt");
        List<Boards> boards = dbServices.getBoardsByCreator(creator);

        return boards;
    }
    @PostMapping("deleteBoard")
    public void deleteBoard(@RequestParam(name = "id") int boardId){
        dbServices.deleteBoard(boardId);
    }

    @PostMapping("deleteGroup")
    public void deleteGroup(@RequestParam(name = "groupId") int groupId){
        dbServices.deleteGroup(groupId);
    }

    @PostMapping("insertBoard")
    public void insertBoard(@RequestParam(name = "title") String title){

        dbServices.createBoard(title);
    }

    @GetMapping("loadStickers")
    public Map<String, Object> getStickers(@RequestParam(name = "boardId") int boardId, HttpSession session){
        Set<String> allPermittedOperations = dbServices.getAllAccessesToBoardForCurrentAccaunt(boardId);
        Map<String, Object> stickersAndPermittedOperations = new HashMap<>();
        stickersAndPermittedOperations.put("allPermittedOperations", allPermittedOperations);
        stickersAndPermittedOperations.put("stickers", dbServices.getStickersForBoardId(boardId));
        return stickersAndPermittedOperations;
    }
    @GetMapping("getAllInfForBoard")
    public Map<String, Object> getAllInfForBoard(@RequestParam(name = "boardId") int boardId, HttpSession session){
        Boards board = dbServices.getBoardById(boardId);
        Map<String, Object> allInfForBoard = new HashMap<>();
        allInfForBoard.put("board", board);
        allInfForBoard.put("groups", dbServices.getGroupsForBoardId(boardId));
        return allInfForBoard;
    }

    @GetMapping("getAllOperations")
    public List<Operation> getAllOperations(){
        return dbServices.getAllOperations();
    }

    @GetMapping("getOperationsForGroup")
    public List<OperationsForGroups> getOperationsForGroup(@RequestParam(name = "groupId") int groupId){
        return dbServices.getOperationsForGroupId(groupId);
    }

    @PostMapping("setExposedBoard")
    public void setExposedBoard(@RequestParam(name = "boardId") int boardId, HttpSession session) {
        dbServices.setExposedBoard(boardId);
    }
    @PostMapping("unsetExposedBoard")
    public void unsetExposedBoard(@RequestParam(name = "boardId") int boardId, HttpSession session) {
        dbServices.unsetExposedBoard(boardId);
    }

    @PostMapping("saveTitleBoard")
    public void saveTitleBoard(@RequestParam(name = "boardId") int boardId,
                               @RequestParam(name = "newTitle") String newTitle) {
        dbServices.saveTitleBoard(boardId, newTitle);
    }
    @PostMapping("addGroup")
    public void addGroup(@RequestParam(name = "boardId") int boardId,
                         @RequestParam(name = "newGroupName") String newGroupName) {
        dbServices.createGroup(boardId, newGroupName);
    }

    @GetMapping("loadUsers")
    public List<Accaunt> loadUsers() {

        return dbServices.getAllAccaunts();
    }
    @GetMapping("loadMembersGroup")
    public List<MemberOfGroup> loadMembersGroup(@RequestParam(name = "groupId") int groupId) {

        return dbServices.getMembersGroup(groupId);
    }
    @PostMapping("createSticker")
    public void createSticker(@RequestParam(name = "boardId") int boardId,
                              @RequestParam(name = "textSticker") String textSticker) {
         dbServices.createSticker(textSticker, boardId);
    }
    @PostMapping("deleteSticker")
    public void deleteSticker(@RequestParam(name = "stickerId") int stickerId) {
         dbServices.deleteSticker(stickerId);
    }

    @PostMapping("deleteMemberFromGroup")
    public void deleteMemberFromGroup(@RequestParam(name = "memberIdInGroup") int memberIdInGroup){
        dbServices.deleteMemberFromGroup(memberIdInGroup);
    }
    @PostMapping("deleteAllMembersFromGroup")
    public void deleteAllMembersFromGroup(@RequestParam(name = "groupId") int groupId){
        dbServices.deleteAllMembersFromGroup(groupId);
    }
    @PostMapping("addUserInSelectedGroup")
    public void addUserInSelectedGroup(@RequestParam(name = "groupId") int groupId,
                                       @RequestParam(name = "userId") int userId){
        dbServices.addUserInGroup(groupId, userId);
    }
    @PostMapping("setOperationStatus")
    public void setOperationStatus(@RequestParam(name = "groupId") int groupId,
                                   @RequestParam(name = "operationId") int operationId,
                                   @RequestParam(name = "checked") boolean checked){
        dbServices.setOperationStatus(groupId, operationId, checked);
    }

}
