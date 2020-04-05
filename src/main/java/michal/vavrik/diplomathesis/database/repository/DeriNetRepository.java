package michal.vavrik.diplomathesis.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import michal.vavrik.diplomathesis.database.entity.DeriNetRow;

/**
 * Spring data {@link org.springframework.data.repository.Repository} for {@link DeriNetRow} entity.
 */
@Repository
public interface DeriNetRepository extends JpaRepository<DeriNetRow, Double> {

}
