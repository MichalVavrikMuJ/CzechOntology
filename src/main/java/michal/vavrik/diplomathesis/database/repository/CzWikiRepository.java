package michal.vavrik.diplomathesis.database.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import michal.vavrik.diplomathesis.database.entity.Czwiki;


/**
 * Spring data {@link org.springframework.data.repository.Repository} for {@link PageMap} entity.
 */
@Repository
public interface CzWikiRepository extends JpaRepository<Czwiki, BigDecimal> {

	// It does it's job even if it is empty.

}