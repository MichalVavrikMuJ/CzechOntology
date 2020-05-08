package michal.vavrik.diplomathesis.database.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import michal.vavrik.diplomathesis.database.entity.DeriNetRow;

/**
 * Spring data {@link org.springframework.data.repository.Repository} for {@link DeriNetRow} entity.
 */
@Repository
public interface DeriNetRepository extends JpaRepository<DeriNetRow, Double> {

	public static final String ROOT = "Type=Root";
	
	/**
	 * default search similar to LIKE SQL clause
	 * 
	 * @param title {@link String}
	 * @return {@link List<DeriNetRow>} list of rows whose lemma contains given {@link String}
	 */
	List<DeriNetRow> findByLemmaContaining(String lemma);
	
	List<DeriNetRow> findByLemma(String lemma);
	
	List<DeriNetRow> findByMainParentId(Double id);
	
	List<DeriNetRow> findByIdBetween(Double id, Double idPlusOne);
	
	List<DeriNetRow> findByMainParentId(Double id, Pageable pageable);
	
	@Query(value = "SELECT d FROM DeriNetRow d WHERE d.id NOT LIKE '%.%' ORDER BY d.id ASC",
			countQuery = "SELECT COUNT(*) FROM DeriNetRow d WHERE d.id NOT LIKE '%.%' ORDER BY d.id ASC")
	List<DeriNetRow> searchByIdWithoutDecimalPoint(Pageable pageable);
	
	/**
	 * search {@link DeriNetRow#getMorphologicalSegmentation()} similarly to SQL LIKE clause
	 * 
	 * @param contain {@link String}
	 * @return {@link List<DeriNetRow>} list of rows with known roots
	 */
	List<DeriNetRow> findByMorphologicalSegmentationContaining(String contain);
	
	/**
	 * Retrieves list with words whose root were manually (by linguists) determined
	 * 
	 * @return {@link List<DeriNetRow>} list of rows with known roots
	 */
	default List<DeriNetRow> listWordsWithKnownRoot() {
		return findByMorphologicalSegmentationContaining(ROOT);
	}
}
