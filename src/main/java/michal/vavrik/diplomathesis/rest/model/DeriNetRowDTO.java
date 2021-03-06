package michal.vavrik.diplomathesis.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Michal Vavrik
 *
 *	refers to Derinet 2.x format
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
public class DeriNetRowDTO {
	
	public enum PartOfSpeech {
		A("Adjective"), D("Adverb"), N("Noun"), V("Verb");
		
		@Getter
		String POS;
		
		PartOfSpeech(String partOfSpeech) {
			this.POS = partOfSpeech;
		}
	}
	
	private String id;
	
	private String languageSpecificID;
	
	private String lemma;
	
	private PartOfSpeech POS;
	
	private String morphologicalFeatures;
	
	private String morphologicalSegmentation;
	
	private String mainParentID;
	
	/**
	 * AKA RELTYPE
	 */
	private String parentRelation;
	
	/**
	 * Relations that did not fit into the main PARENTID / RELTYPE
	 */
	private String otherRelations;
	
	/**
	 * e.g. annotation not fitting elsewhere, debugging labels, authorship informations etc.
	 */
	private String jsonGeneralData;
	
}
