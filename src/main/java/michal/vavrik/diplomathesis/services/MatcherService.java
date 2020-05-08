package michal.vavrik.diplomathesis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.primitives.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import michal.vavrik.diplomathesis.database.entity.DeriNetRow;
import michal.vavrik.diplomathesis.rest.model.OntologyEntryDTO;

@Slf4j
@Service
public class MatcherService {
	
	@Autowired
	Word2VecService word2VecService;
	
	@Autowired
	DeriNetService deriNetService;
	
	public static final double[] positivePattern = {1};
	public static final double[] negativePattern = {0};
	
	public OntologyEntryDTO matchKeyWordWithOntology(String keyword) {
		return null;
	}
	
	public INDArray getNeuralNetworkInput(String baseWord, String allegedlyDerivedWord) {
		List<INDArray> wordVectorMatrixes = word2VecService.getWordVectorMatrixes(List.of(baseWord, allegedlyDerivedWord));
		if (wordVectorMatrixes == null || wordVectorMatrixes.size() != 2) {
			return null;
		} else {
			return Nd4j.concat(1, wordVectorMatrixes.get(0), wordVectorMatrixes.get(1));
		}
	}
	
	public List<Pair<double[],double[]>> getTrainigSet(int betweenStart, int betweenEnd) {
		List<Pair<double[],double[]>> trainingSet = new ArrayList<>();
		deriNetService.getBaseWords(betweenStart, betweenEnd).stream().map(this::getTrainigSetForWord).filter(x -> x != null).forEach(y -> trainingSet.addAll(y));
		log.info("Returning training set of size {}", trainingSet.size());
		return trainingSet;
	}
	
	public double[] merge2Arrays(double[] array1, double[] array2) {
		return ArrayUtils.addAll(array1, array2);
	}
	
	public List<Pair<double[],double[]>> getTrainigSetForWord(DeriNetRow keyWord) {
		log.info("Getting training set for word {}.", keyWord.getLemma());
		double[] baseWordVector = word2VecService.getWordVector(keyWord.getLemma());
		if (baseWordVector == null) { return null; }
		List<String> derivedWords = deriNetService.getWordsDerived(keyWord);
		if (derivedWords == null || derivedWords.size() < 4) { return null; }
		List<double[]> positiveSamples = word2VecService.getWordVectors(derivedWords);
		if (positiveSamples == null || positiveSamples.size() < 4 ) { return null; }
		List<String> wordsSimilarInCharacter = word2VecService.getWordsSimilarInCharacter(keyWord.getLemma(), Word2VecService.DEFAULT_SIMILARITY_ACCURACY);
		log.info("List of words similar in character has {} items before removing derived.", wordsSimilarInCharacter.size());
		wordsSimilarInCharacter.removeAll(derivedWords);
		log.info("List of words similar in character has {} items after removing derived words.", wordsSimilarInCharacter.size());
		List<double[]> negativeSamples = word2VecService.getWordVectors(wordsSimilarInCharacter);
		if ( negativeSamples == null || negativeSamples.isEmpty() ) { return null; }

		//instantiating training set
		List<Pair<double[], double[]>> trainingSet = positiveSamples.stream().map(x -> Pair.makePair(merge2Arrays(baseWordVector, x), positivePattern)).collect(Collectors.toList());
		negativeSamples.stream().map(x -> Pair.makePair(merge2Arrays(baseWordVector, x), negativePattern)).forEach(y -> trainingSet.add(y));
		return trainingSet;
	}

}
