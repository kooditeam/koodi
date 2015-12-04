
package koodi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Long> {
    List<T> findByRemovedFalse();
    List<T> findByRemovedTrue();
    T findByIdAndRemovedFalse(Long id);
}
