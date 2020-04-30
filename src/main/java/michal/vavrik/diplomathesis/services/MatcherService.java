package michal.vavrik.diplomathesis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.nd4j.linalg.primitives.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import michal.vavrik.diplomathesis.rest.model.OntologyEntryDTO;

@Slf4j
@Service
public class MatcherService {
	
	@Autowired
	Word2VecService word2VecService;
	
	@Autowired
	DeriNetService deriNetService;
	
	public OntologyEntryDTO matchKeyWordWithOntology(String keyword) {
		return null;
	}
	
	@Bean
	public List<Pair<double[],double[]>> getTrainigSet() {
		List<Pair<double[],double[]>> trainingSet = new ArrayList<>();
		deriNetService.getBaseWords().stream().map(this::getTrainigSetForWord).forEach(y -> trainingSet.addAll(y));
		return trainingSet;
	}
	
	@Bean
	public List<Pair<double[],double[]>> getTrainigSetForWord(String keyWord) {
		List<String> derivedWords = deriNetService.getWordsDerived(keyWord);
		List<String> wordsSimilarInCharacter = word2VecService.getWordsSimilarInCharacter(keyWord, Word2VecService.DEFAULT_SIMILARITY_ACCURACY);
		List<double[]> positiveSamples = word2VecService.getWordVectors(derivedWords);
		log.info("List of words similar in character has {} items before removing derived.", wordsSimilarInCharacter.size());
		wordsSimilarInCharacter.removeAll(derivedWords);
		log.info("List of words similar in character has {} items after removing derived words.", wordsSimilarInCharacter.size());
		List<double[]> negativeSamples = word2VecService.getWordVectors(wordsSimilarInCharacter);
		
		//instantiating training set
		double[] positivePattern = {1};
		double[] negativePattern = {0};
		List<Pair<double[], double[]>> trainingSet = positiveSamples.stream().map(x -> Pair.makePair(x, positivePattern)).collect(Collectors.toList());
		negativeSamples.stream().map(x -> Pair.makePair(x, negativePattern)).forEach(y -> trainingSet.add(y));
		return trainingSet;
	}

}
