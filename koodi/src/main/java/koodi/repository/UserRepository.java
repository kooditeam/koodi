package koodi.repository;

import java.util.List;
import koodi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String name);
    public List<User> findByRemovedIsFalse();
}
