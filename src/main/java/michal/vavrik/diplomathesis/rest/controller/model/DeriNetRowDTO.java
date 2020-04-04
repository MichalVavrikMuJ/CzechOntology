package michal.vavrik.diplomathesis.rest.controller.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

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
		
		String POS;
		
		PartOfSpeech(String partOfSpeech) {
			this.POS = partOfSpeech;
		}
	}
	
	
	
	private String id;
	
	private String languageSpecificID;
	
	private String Lemma;
	
	private PartOfSpeech POS;
	
	private String MorphologicalFeatures;
	
	private String MorphologicalSegmentation;
	
	private String MainParentID;
	
	/**
	 * AKA RELTYPE
	 */
	private String ParentRelation;
	
	/**
	 * Relations that did not fit into the main PARENTID / RELTYPE
	 */
	private String OtherRelations;
	
	/**
	 * e.g. annotation not fitting elsewhere, debugging labels, authorship informations etc.
	 */
	private String JSONGeneralData;
	
	public String getRoot() {
		return getSegmentationMap().get("root");
	}
	
	private Map<String, String> getSegmentationMap() {
		// Morph = x ---> x is root name
		// Type = root ---> signaling there is a root identified
		// Start = a ---> 0 inclusive
		// End = b ---> 3 exclusive
		// e.g. lemma = Janička
		// Morph = Jan, then Jan is root, nička is rest

		if (!StringUtils.isEmpty(this.getMorphologicalSegmentation())) {
			return Arrays.stream(this.getMorphologicalSegmentation().split("&")).map(pair -> {
				String[] split = pair.split("=");
				return Map.entry(split[0], split[1]);
			}).collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
		} else {
			return Collections.emptyMap();
		}
	}

}
