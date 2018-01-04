package stickers.database.repositoryes;

import org.springframework.data.repository.CrudRepository;
import stickers.database.entity.Accaunt;

import java.util.List;

public interface AccauntRepository extends CrudRepository<Accaunt, Integer> {

    Accaunt findAccauntByEmailAndPassword(String login, String password);
    Accaunt findAccauntByEmail(String email);

    @Override
    Accaunt save(Accaunt accaunt);
    Accaunt findAccauntById(Integer id);
}
