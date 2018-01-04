package stickers.services;

import stickers.database.entity.*;

import java.util.List;
import java.util.Set;

public interface DbServices {
    void confirmAccaunt(int accauntId);

    boolean exsistsAccauntWithEamil(String email);

    List<Boards> getBoardsByCreator(Accaunt creator);

    void deleteBoard(int boardId);

    void deleteGroup(int groupId);

    void setActivAccaunt(Accaunt accaunt);
    void clearActivAccaunt();

    void createBoard(String title);


    Set<String> getAllAccessesToBoardForCurrentAccaunt(Integer requestedBoardId);

    boolean containRead(Set<String> operations);

    boolean canCurrentUserCreateSticker(int boardId);

    boolean canUserDeleteBoard(int boardId, Accaunt accaunt);
    boolean canUserDeleteGroup(int groupId, Accaunt accaunt);

    void createSticker(String textSticker, int boardId);

    Accaunt getAccaunt(String email, String password);
    Accaunt getAccaunt(int id);
    Accaunt getAccaunt(String email);
    Accaunt getActivAccaunt();

    boolean canCurrentUserReadBoard(Integer requestedBoardId);

    Accaunt createAccaunt(Accaunt accaunt);

    Object getStickersForBoardId(int boardId);

    Boards getBoardById(int boardId);

    List<Group> getGroupsForBoardId(int boardId);

    List<Operation> getAllOperations();


    void setExposedBoard(int boardId);

    void unsetExposedBoard(int boardId);

    List<OperationsForGroups> getOperationsForGroupId(int groupId);

    void createGroup(int boardId, String newGroupName);

    void saveTitleBoard(int boardId, String newTitle);

    void saveMembersGroup(int groupId, int[] membersId);

    List<Accaunt> getAllAccaunts(); // на будущее надо от него избавиться

    List<AccauntsInGroups> getMembersGroup(int groupId);

    void saveOperationsForGroup(int groupId, int[] operationsId);

    void deleteSticker(int stickerId);

    boolean canCurrentUserDeleteStickerFromBoard(int boardId);
}
