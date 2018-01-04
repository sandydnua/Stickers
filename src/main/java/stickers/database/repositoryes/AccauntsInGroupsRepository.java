package stickers.database.repositoryes;

import org.springframework.data.repository.CrudRepository;
import stickers.database.entity.Accaunt;
import stickers.database.entity.AccauntsInGroups;

import java.util.List;

public interface AccauntsInGroupsRepository extends CrudRepository<AccauntsInGroups, Integer>{
    List<AccauntsInGroups> findAllByGroupId(int groupId);
    void deleteByGroup_Id(int groupId);
    AccauntsInGroups findByGroup_IdAndAccaunt_Id(int groupId, int accauntId);
}

