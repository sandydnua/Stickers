package stickers.database.repositoryes;

import org.springframework.data.repository.CrudRepository;
import stickers.database.entity.Operation;

import java.util.List;

public interface OperationRepository extends CrudRepository<Operation, Integer> {
    List<Operation> findByName(String name);
}

