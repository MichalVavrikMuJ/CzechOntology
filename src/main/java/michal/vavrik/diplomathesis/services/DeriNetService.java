package michal.vavrik.diplomathesis.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import michal.vavrik.diplomathesis.database.entity.DeriNetRow;
import michal.vavrik.diplomathesis.database.repository.DeriNetRepository;
import michal.vavrik.diplomathesis.rest.model.DeriNetRowDTO;

@Service
@Slf4j
public class DeriNetService {
	
	@Autowired
	private MapperFacade mapper;
	
	@Autowired
	private DeriNetRepository derinetRepository;
	
	public void saveDeriNetRows(List<DeriNetRowDTO> derinetRows) {
		log.info("Maping {} DTO to DeriNetRow entities and storing them.", derinetRows.size());
		List<DeriNetRow> entities = derinetRows.stream().map(row -> mapper.map(row, DeriNetRow.class)).collect(Collectors.toList());
		log.info("{} DeriNetRow entities stored.", derinetRepository.saveAll(entities).size());
	}
	
	public List<String> getWordsDerived(String from) {
		log.info("quering db for words derived from {}.", from);
		List<DeriNetRow> derivedWords = this.derinetRepository.findByMainParentId(derinetRepository.findByLemma(from).getId());
		log.info("{} results retrieved.", derivedWords.size());
		return derivedWords.stream().map(DeriNetRow::getLemma).collect(Collectors.toList());
	}
	
	public String getRoot(DeriNetRow derinetRow) {
		return this.getSegmentationMap(derinetRow).get("Morph");
	}
	
	public Map<String, String> getSegmentationMap(DeriNetRow derinetRow) {
		// Morph = x ---> x is root name
		// Type = root ---> signaling there is a root identified
		// Start = a ---> 0 inclusive
		// End = b ---> 3 exclusive
		// e.g. lemma = Janička
		// Morph = Jan, then Jan is root, nička is rest

		if (!StringUtils.isEmpty(derinetRow.getMorphologicalSegmentation())) {
			return Arrays.stream(derinetRow.getMorphologicalSegmentation().split("&")).map(pair -> {
				String[] split = pair.split("=");
				return Map.entry(split[0], split[1]);
			}).collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
		} else {
			return Collections.emptyMap();
		}
	}
	
	public Map<String, DistanceToRoot> getMapOfWordsWithKnownRoot() {
		Map<String, DistanceToRoot> rootDistances = new HashMap<>();
		derinetRepository.listWordsWithKnownRoot().stream()
		// FIXME: change limit(100) to something reasonable
		.limit(100)
		.forEach(
				word -> {
					String root = this.getRoot(word);
					rootDistances.merge(
							root,
							DistanceToRoot.builder().rootWord(root).derivedWordWithDistance(word.getLemma(), 0.0).build(),
							(old, newOne) -> {
								Map<String, Double> derivedWordsWithDistance = new HashMap<>();
								derivedWordsWithDistance.putAll(old.getDerivedWordsWithDistance());
								derivedWordsWithDistance.put(word.getLemma(), newOne.getDerivedWordsWithDistance().get(word.getLemma()));
								old.setDerivedWordsWithDistance(derivedWordsWithDistance);
								return old;
									});
				});
		return rootDistances;
	}
	
	@Builder @Getter @Setter
	public static class DistanceToRoot {
		private String rootWord;
		@Singular(value = "derivedWordWithDistance")
		private Map<String, Double> derivedWordsWithDistance;
	}
}
