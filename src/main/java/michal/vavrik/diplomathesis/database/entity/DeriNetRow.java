package michal.vavrik.diplomathesis.database.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
@Entity
public class DeriNetRow {
	
	public enum PartOfSpeech {
		A("Adjective"), D("Adverb"), N("Noun"), V("Verb");
		
		@Getter
		String POS;
		
		PartOfSpeech(String partOfSpeech) {
			this.POS = partOfSpeech;
		}
	}
	
	@Id
	private Double id;
	
	@Column(name = "LANGUAGE_SPECIFIC_ID")
	private String languageSpecificID;
	
	private String lemma;
	
	private PartOfSpeech POS;
	
	private String morphologicalFeatures;
	
	private String morphologicalSegmentation;
	
	@ManyToOne
	@JoinColumn(name="MAIN_PARENT_ID", nullable=true)
	private DeriNetRow mainParent;
	
	@OneToMany(mappedBy = "mainParent", cascade = CascadeType.ALL)
	private Set<DeriNetRow> descendantRows;

	
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
	@Column(name = "JSON_GENERAL_DATA")
	private String JSONGeneralData;

}
