package michal.vavrik.diplomathesis.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import michal.vavrik.diplomathesis.rest.model.OntologyEntryDTO;

@Service
public class MatcherService {
	
	@Autowired
	Word2VecService word2VecService;
	
	@Autowired
	DeriNetService deriNetService;
	
	public OntologyEntryDTO matchKeyWordWithOntology(String keyword) {
		return null;
	}
	
	public Map<double[], double[]> getTrainigSetForWord(String keyWord) {
		List<String> derivedWords = deriNetService.getWordsDerived(keyWord);
		List<String> wordsSimilarInCharacter = word2VecService.getWordsSimilarInCharacter(keyWord, Word2VecService.DEFAULT_SIMILARITY_ACCURACY);
		List<double[]> positiveSamples = word2VecService.getWordVectors(derivedWords);
		wordsSimilarInCharacter.removeAll(derivedWords);
		List<double[]> negativeSamples = word2VecService.getWordVectors(wordsSimilarInCharacter);
		return null;
	}

}
