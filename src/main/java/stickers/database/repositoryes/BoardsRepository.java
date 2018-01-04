package stickers.database.repositoryes;

import org.springframework.data.repository.CrudRepository;
import stickers.database.entity.Accaunt;
import stickers.database.entity.Boards;

import java.util.List;

public interface BoardsRepository extends CrudRepository<Boards, Integer> {
    List<Boards> findAllByCreator(Accaunt creator);
    Boards findByIdAndCreator(int boardId, Accaunt creator);
    Boards findByIdAndExposedTrue(int boardId);

}
