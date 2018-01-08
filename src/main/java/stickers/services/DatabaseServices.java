package stickers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import stickers.database.DatabaseManager;
import stickers.database.entity.*;
import stickers.database.repositoryes.*;
import stickers.exceptions.DatabaseManagerExeption;

import java.lang.reflect.Array;
import java.util.*;


@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DatabaseServices implements DbServices {

    private Accaunt activAccaunt = null;

    @Autowired
    private AccauntsInGroupsRepository accauntsInGroupsRepository;

    @Autowired
    private OperationsForGroupsRepository operationsForGroupsRepository;

    @Autowired
    private StickersRepository stickersRepository;

    @Autowired
    private AccauntRepository accauntRepository;

    @Autowired
    private BoardsRepository boardsRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private OperationRepository operationRepository;

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    private DatabaseManager databaseManager;


    @Override
    public void confirmAccaunt(int accauntId) {
        Accaunt accaunt = accauntRepository.findOne(accauntId);
        if(accaunt != null) {
            accaunt.setConfirm(true);
            accauntRepository.save(accaunt);
        } else {
            //TODO такого аккаунта нет
            throw new DatabaseManagerExeption("Accaunt " + accauntId + " not found");
        }
    }

    @Override
    public boolean exsistsAccauntWithEamil(String email) {
        return (null == accauntRepository.findAccauntByEmail(email))? true : false;
    }

    @Override
    public List<Boards> getBoardsByCreator(Accaunt creator) {
        return boardsRepository.findAllByCreator(creator);
    }

    @Override
    public void deleteBoard(int boardId) {
        if ( canUserDeleteBoard(boardId, this.activAccaunt)) {
            boardsRepository.delete(boardId);
        }
    }

    @Override
    public void deleteGroup(int groupId) {
        if (canUserDeleteGroup(groupId, this.activAccaunt)) {
            groupRepository.delete(groupId);
        }
    }

    @Override
    public void setActivAccaunt(Accaunt accaunt) {
        this.activAccaunt = accaunt;
    }

    @Override
    public void clearActivAccaunt() {
        activAccaunt = null;
    }

    @Override
    public void createBoard(String title) {
        boardsRepository.save(new Boards(title, activAccaunt));
    }


    @Override
    public Accaunt getAccaunt(String email, String password) {
        return accauntRepository.findAccauntByEmailAndPassword(email, password);
    }

    @Override
    public Accaunt getAccaunt(int id) {
        return accauntRepository.findOne(id);
    }

    @Override
    public Accaunt getAccaunt(String email) {
        return accauntRepository.findAccauntByEmail(email);
    }

    @Override
    public Accaunt getActivAccaunt() {
        return this.activAccaunt;
    }

    @Override
    public boolean canCurrentUserReadBoard(Integer requestedBoardId) {
        Set<String> accesses = getAllAccessesToBoardForCurrentAccaunt(requestedBoardId);
        return ( null != accesses && accesses.contains("read"));
    }

    @Override
    public Accaunt createAccaunt(Accaunt accaunt) {
        // TODO проверки
        accauntRepository.save(accaunt);
        return accauntRepository.findAccauntByEmail(accaunt.getEmail());
    }

    @Override
    public List<Stickers> getStickersForBoardId(int boardId) {
        return  (canCurrentUserReadBoard(boardId))? stickersRepository.findAllByBoardId(boardId) : null;
    }

    @Override
    public Boards getBoardById(int boardId) {
        return canCurrentUserReadBoard(boardId)?  boardsRepository.findOne(boardId) : null;
    }

    @Override
    public List<Group> getGroupsForBoardId(int boardId) {
        return groupRepository.findAllByBoardId(boardId);
    }

    @Override
    public List<Operation> getAllOperations() {
        return (List<Operation>) operationRepository.findAll();
    }

    @Override
    public void setExposedBoard(int boardId) {
        changeExposedBoard(boardId, true, activAccaunt);
    }

    @Override
    public void unsetExposedBoard(int boardId) {
        changeExposedBoard(boardId, false, activAccaunt);
    }

    public void changeExposedBoard(int boardId, boolean newStatusExposed, Accaunt creator) {
        Boards board = boardsRepository.findByIdAndCreator(boardId, creator);
        if ( board != null) {
            board.setExposed(newStatusExposed);
            boardsRepository.save(board);
        }
    }

    @Override
    public List<OperationsForGroups> getOperationsForGroupId(int groupId) {
        Group group = groupRepository.findOne(groupId);
        int boardId = group.getBoard().getId();
        return canCurrentUserReadBoard(boardId)? operationsForGroupsRepository.findAllByGroupId(groupId) : null;
    }

    @Override
    public void createGroup(int boardId, String newGroupName) {
        if (isCurentUserCreatorBoard(boardId)) {
            groupRepository.save(new Group(newGroupName, boardsRepository.findOne(boardId)));
        }
    }

    @Override
    public void saveTitleBoard(int boardId, String newTitle) {
        if (isCurentUserCreatorBoard(boardId)) {
            Boards board = boardsRepository.findOne(boardId);
            board.setTitle(newTitle);
            boardsRepository.save(board);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void saveMembersGroup(int groupId, int[] membersId) {
         // TODO надо оптимизировать, слишком много обращений к базе из-за хибернейта
        Group group = groupRepository.findOne(groupId);
        if (isCurentUserCreatorBoard(group.getBoard().getId())) {
            accauntsInGroupsRepository.deleteByGroup_Id(groupId);
            if (null != membersId) {
                List<AccauntsInGroups> rows = new ArrayList<>();
                for (int id : membersId) {
                    if (accauntsInGroupsRepository.findByGroup_IdAndAccaunt_Id(groupId, id) == null) {
                        rows.add(new AccauntsInGroups(accauntRepository.findOne(id), group));
                    }
                }
                accauntsInGroupsRepository.save(rows);
            }
        }
    }

    @Override
    public List<Accaunt> getAllAccaunts() {// на будущее надо от него избавиться
        return (List<Accaunt>) accauntRepository.findAll();
    }

    @Override
    public List<AccauntsInGroups> getMembersGroup(int groupId) {
        // тут я выбираю всю строку из перекрестной таблицы. и в одном из ее полей есть нужные мне члены. а надо было толко мемберов.. надо перстроить
        // так запрос, чтобы в итоге сразу был List<Accaunts>
        return accauntsInGroupsRepository.findAllByGroupId(groupId);
//        databaseManager.executeQuery("SELECT a.id, a.firstname, a.lastname from accaunts_in_groups aig inner join accaunts a on a.id = aig.accaunt where aig.groupT =" + groupId);
    }

    @Override
    @Transactional(readOnly = false)
    public void saveOperationsForGroup(int groupId, int[] operationsId) {
        // TODO
        // этот метод похож на saveMembersGroup - может как-то сделать универсально... надо подумать
        Group group = groupRepository.findOne(groupId);
        if (isCurentUserCreatorBoard(group.getBoard().getId())) {
            operationsForGroupsRepository.deleteByGroup_Id(groupId);
            if ( null != operationsId) {
                List<OperationsForGroups> rows = new ArrayList<>();

                for (int id : operationsId) {
                    if ( operationsForGroupsRepository.findByGroup_IdAndOperation_Id(groupId, id) == null ) {
                        rows.add(new OperationsForGroups(groupRepository.findOne(groupId), operationRepository.findOne(id)));
                    }
                }
                operationsForGroupsRepository.save(rows);
            }
        }
    }

    @Override
    public void createSticker(String textSticker, int boardId) {
        if (canCurrentUserReadBoard(boardId)) {
            Boards board = boardsRepository.findOne(boardId);
            stickersRepository.save(new Stickers(textSticker, this.activAccaunt, board));
        }
    }

    @Override
    public void deleteSticker(int stickerId) {
        int boardId = stickersRepository.findOne(stickerId).getBoard().getId();
        if (canCurrentUserDeleteStickerFromBoard(boardId)) {
            stickersRepository.delete(stickerId);
        }
    }

    private boolean isCurentUserCreatorBoard(int boardId) {
        return boardsRepository.findByIdAndCreator(boardId, activAccaunt) != null;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getAllAccessesToBoardForCurrentAccaunt(Integer requestedBoardId) {
        Set<String> resultSetOperations = new HashSet<>();
        System.out.println("++++++++++++++++++");
        if (boardsRepository.findByIdAndCreator(requestedBoardId, activAccaunt) != null) {
            // запрошеная доска была создана этим аккаунтом         // значит ему доступны все операци
            for (Operation operation : operationRepository.findAll()) {
                resultSetOperations.add(operation.getName());
            }
        } else if (activAccaunt !=null) {
            // TODO надо сделать параметризированый запрос а не этот...
            List<String> operations = databaseManager.executeQuery("select operations.name from operations, " +
                    "((SELECT groups.id FROM `groups`  " +
                    "join `accaunts_in_groups` on  groups.id = accaunts_in_groups.groupT  " +
                    "where accaunts_in_groups.accaunt = " + activAccaunt.getId()+ " and groups.boards = " + requestedBoardId + ") gr  join operations_for_groups on operations_for_groups.groupT = gr.id)" +
                    "where operations.id = operations_for_groups.operation");
            for(String operation : operations) {
                resultSetOperations.add(operation);
            }
        }

        if (isBoardExposed(requestedBoardId)) {
            resultSetOperations.add("read");
        }

        return resultSetOperations;
    }

    private boolean isBoardExposed(Integer boardId) {
        return boardsRepository.findByIdAndExposedTrue(boardId) != null;
    }

    @Override
    public boolean containRead(Set<String> operations) {
        return operations.contains("read");
    }

    @Override
    public boolean canCurrentUserCreateSticker(int boardId) {
        Set<String> accesses = getAllAccessesToBoardForCurrentAccaunt(boardId);
        return ( null != accesses && accesses.contains("create"));
    }

    @Override
    public boolean canUserDeleteBoard(int boardId, Accaunt accaunt) {
        return boardsRepository.findByIdAndCreator(boardId, accaunt) != null;
    }

    @Override
    public boolean canUserDeleteGroup(int groupId, Accaunt accaunt) {
        return groupRepository.findByIdAndBoard_Creator(groupId, accaunt) != null;
    }

    @Override
    public boolean canCurrentUserDeleteStickerFromBoard(int boardId) {
        Set<String> accesses = getAllAccessesToBoardForCurrentAccaunt(boardId);
        return ( null != accesses && accesses.contains("delete"));
    }
}
