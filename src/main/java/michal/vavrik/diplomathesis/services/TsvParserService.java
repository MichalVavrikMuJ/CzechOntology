package michal.vavrik.diplomathesis.services;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.google.common.base.Enums;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.ObjectRowProcessor;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import lombok.extern.slf4j.Slf4j;
import michal.vavrik.diplomathesis.rest.model.DeriNetRowDTO;

@Slf4j
@Service
public class TsvParserService {
	
	public List<DeriNetRowDTO> getRows(Resource resource) {
		try {
			List<DeriNetRowDTO> result = new ArrayList<>();
			TsvParserSettings settings = new TsvParserSettings();
			settings.setProcessor(new ObjectRowProcessor() {
			    @Override
			    public void rowProcessed(Object[] row, ParsingContext context) {
			    	result.add(mapRowToDTO(row));
			    }
			});
			(new TsvParser(settings)).parse(new FileReader(resource.getFile()));
			return result;
		} catch (IOException e) {
			log.error("Could not read file named {}", resource.getFilename());
			return Collections.emptyList();
		}
	}
	
	public DeriNetRowDTO mapRowToDTO(Object[] objectRow) {
		List<String> row = Arrays.stream(objectRow).map(x -> x != null ? x.toString() : "").collect(Collectors.toList());
		log.info("Mapping row to DTO - lemma: {}", row.get(2));
		return DeriNetRowDTO
			.builder()
			.id(row.get(0))
			.languageSpecificID(row.get(1))
			.lemma(row.get(2))
			.POS(Enums.getIfPresent(DeriNetRowDTO.PartOfSpeech.class, row.get(3)).orNull())
			.morphologicalFeatures(row.get(4))
			.morphologicalSegmentation(row.get(5))
			.mainParentID(row.get(6))
			.parentRelation(row.get(7))
			.otherRelations(row.get(8))
			.jsonGeneralData(row.get(9))
			.build();
	}

}
