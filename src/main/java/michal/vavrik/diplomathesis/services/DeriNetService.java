package michal.vavrik.diplomathesis.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
	
	public String getRoot(DeriNetRow derinetRow) {
		return this.getSegmentationMap(derinetRow).get("root");
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
	
}
