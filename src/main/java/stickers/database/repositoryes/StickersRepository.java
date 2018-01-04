package stickers.database.repositoryes;

import org.springframework.data.repository.CrudRepository;
import stickers.database.entity.Boards;
import stickers.database.entity.Stickers;

import java.util.List;

public interface StickersRepository extends CrudRepository<Stickers, Integer> {
    List<Stickers> findAllByBoardId(int boardId);
}
