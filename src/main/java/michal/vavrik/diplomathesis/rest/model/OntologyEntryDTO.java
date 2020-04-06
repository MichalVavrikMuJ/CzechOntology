package michal.vavrik.diplomathesis.rest.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import michal.vavrik.diplomathesis.database.entity.DeriNetRow;

/**
 * @author Michal Vavrik
 *
 * Each {@link OntologyEntryDTO} ideally correspond with single {@link DeriNetRow},
 * where {@link OntologyEntryDTO#keyWord} equals {@link DeriNetRow#getLemma()}.
 * 
 * 
 * TODO: ALSO should have 0..N Czech Morfflex entries (inflexions) of the key word
 * TODO: ALSO should have 0..N synsets (meaning, example, name and hypernyms, hyponyms, ...) from Czech WordNet
 * TODO: DEPENDING ON MEANING, EACH {@link DeriNetRow#getLemma()} should have 0..N wiki articles
 */
@ToString @Builder @Getter @Setter
public class OntologyEntryDTO {

	String keyWord;
	
	
	
}
