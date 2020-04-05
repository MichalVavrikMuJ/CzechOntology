package michal.vavrik.diplomathesis.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import michal.vavrik.diplomathesis.database.entity.DeriNetRow;

/**
 * Spring data {@link org.springframework.data.repository.Repository} for {@link DeriNetRow} entity.
 */
@Repository
public interface DeriNetRepository extends JpaRepository<DeriNetRow, Double> {

	/**
	 * default search similar to LIKE SQL clause
	 * 
	 * @param title {@link String}
	 * @return {@link List<DeriNetRow>} list of rows whose lemma contains given {@link String}
	 */
	List<DeriNetRow> findByLemmaContaining(String lemma);
	
}
