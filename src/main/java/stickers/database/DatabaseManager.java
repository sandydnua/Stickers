package stickers.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import stickers.database.entity.Accaunt;
import stickers.database.entity.Boards;
import stickers.database.entity.Group;
import stickers.database.entity.Operation;
import stickers.database.repositoryes.BoardsRepository;
import stickers.database.repositoryes.GroupRepository;
import stickers.database.repositoryes.OperationRepository;
import stickers.database.repositoryes.OperationsForGroupsRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface DatabaseManager {
//    Accaunt getAccaunt(String email);

    void close();
    boolean isConnect();

    /*Accaunt getAccaunt(String email, String password);

    Accaunt getAccaunt(Integer id);

    GroupRepository getGroupRepository();

    OperationRepository getOperationRepository();

    OperationsForGroupsRepository getOperationForGroupsRepository();

    Boards getBoardExposed(int boardId);

    Accaunt createAccaunt(Accaunt accaunt);

    void confirmAccaunt(Integer id);

    boolean insertBoard(Boards board);

    BoardsRepository getBoardsRepository();

    void changeExposedBoard(int boardId, boolean newStatusExposed, HttpSession session);

    void createSticker(int boardId, String textSticker, HttpSession session);*/

    SessionFactory getSessionFactory();

    public Session getSession();

    void updateQuery(String query);

    public List<String> executeQuery(String query);
}
