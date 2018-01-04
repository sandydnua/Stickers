package stickers.database.repositoryes;

import org.springframework.data.repository.CrudRepository;
import stickers.database.entity.Accaunt;
import stickers.database.entity.Group;

import java.util.List;

public interface GroupRepository extends CrudRepository<Group, Integer> {
    List<Group> findAllByBoardId(int boardId);
    List<Group> findByIdAndBoard_Creator(int groupId, Accaunt creator);
}
