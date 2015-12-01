package koodi.repository;

import java.util.List;
import koodi.domain.User;

public interface UserRepository extends BaseRepository<User> {

    public User findByUsername(String name);
    public List<User> findByRemovedIsFalse();
}
