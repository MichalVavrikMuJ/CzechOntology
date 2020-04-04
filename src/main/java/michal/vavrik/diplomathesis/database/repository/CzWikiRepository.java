package michal.vavrik.diplomathesis.database.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import michal.vavrik.diplomathesis.database.entity.Czwiki;


/**
 * Spring data {@link org.springframework.data.repository.Repository} for {@link PageMap} entity.
 */
@Repository
public interface CzWikiRepository extends JpaRepository<Czwiki, BigDecimal> {

	/**
	 * default search similar to LIKE SQL clause
	 * 
	 * @param title {@link String}
	 * @return {@link List<Czwiki>} list of rows whose title contains given {@link String}
	 */
	List<Czwiki> findByTitleContaining(String title);

}