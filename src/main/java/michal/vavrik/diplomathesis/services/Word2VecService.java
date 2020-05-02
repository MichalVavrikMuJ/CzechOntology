package michal.vavrik.diplomathesis.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import michal.vavrik.diplomathesis.services.DeriNetService.DistanceToRoot;

@Service @Slf4j
public class Word2VecService {

	public static final double DEFAULT_SIMILARITY_ACCURACY = 0.75;
	
	@Autowired
	private Word2Vec word2Vec;
	
	@Autowired
	private DeriNetService deriNetService;
	
	public Word2VecWord getWord2Vec(String keyWord) {
		log.info("Started loading word2vec model.");
		return Word2VecWord.builder().keyWord(keyWord).wordVector(word2Vec.getWordVector(keyWord)).wordsNearest(word2Vec.wordsNearest(keyWord, 100)).build();
	}
	
	public String getWordsSimilarity(String keyWord1, String keyWord2) {
		return Double.toString(word2Vec.similarity(keyWord1, keyWord2));
	}
	
	public List<String> getWordsSimilarInCharacter(String word, double accuracy) {
		log.info("Getting words similar in character to word {}.", word);
		return word2Vec.similarWordsInVocabTo(word, accuracy);
	}
	
	public List<double[]> getWordVectors(List<String> words) {
		log.info("Getting word vectors for {} words.", words.size());
		return words.stream().map(word2Vec::getWordVector).filter(x -> x != null).collect(Collectors.toList());
	}
	
//	public List<DerivedSamples> getWordVectorsFromRow(List<DeriNetRow> words) {
//		log.info("Getting word vectors for {} words.", words.size());
//		return words.stream().map(x -> {
//			double[] wordVector = word2Vec.getWordVector(x.getLemma());
//			return DerivedSamples
//				.builder()
//				.word(x.getLemma())
//				.id(x.getId())
//				.positiveOrNegative(BigDecimal.ONE)
////				.wordVector(Array)
//				.build();
//		}).filter(x -> x != null).collect(Collectors.toList());
//		// FIXME: finish implementation0
//	}
	
	public List<INDArray> getWordVectorMatrixes(List<String> words) {
		log.info("Getting word vector matrixes for {} words.", words.size());
		return words.stream().map(word2Vec::getWordVectorMatrix).filter(x -> x != null).collect(Collectors.toList());
	}
	
	/**
	 * For similarity of root with words derived from the root.
	 * Returns only these words, whose roots have been determined by experts manually.
	 * 
	 * @return {@link Map<String, DistanceToRoot>} list
	 */
	public Map<String, DistanceToRoot> getKnowRootDistances() {
		Map<String, DistanceToRoot> mapOfWordsWithKnownRoot = deriNetService.getMapOfWordsWithKnownRoot();
		mapOfWordsWithKnownRoot.forEach((rootWord, instance) -> {
			instance.setDerivedWordsWithDistance(
					instance
						.getDerivedWordsWithDistance()
						.keySet()
						.stream()
						.map(derivedWord -> Map.entry(
								derivedWord, 
								word2Vec.similarity(rootWord, derivedWord)))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
		});
		return mapOfWordsWithKnownRoot;
	}
	
}
