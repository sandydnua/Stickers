package stickers.database.repositoryes;

import org.springframework.data.repository.CrudRepository;
import stickers.database.entity.Group;
import stickers.database.entity.Operation;
import stickers.database.entity.OperationsForGroups;

import java.util.List;

public interface OperationsForGroupsRepository extends CrudRepository<OperationsForGroups, Integer> {
    List<OperationsForGroups> findAllByGroupId(int groupId);
    void deleteByGroup_Id(int groupId);
    void deleteByGroupAndOperation(Group group, Operation operation);
    OperationsForGroups findByGroup_IdAndOperation_Id(int groupId, int operationId);

}
