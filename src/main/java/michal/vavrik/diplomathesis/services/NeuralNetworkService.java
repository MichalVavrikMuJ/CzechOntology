 package michal.vavrik.diplomathesis.services;

import java.util.List;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class NeuralNetworkService {
	
	@Autowired
	private Word2VecService word2VecService;
	
	@Autowired
	private MultiLayerNetwork network;
	
	/**
	 * Large linguistic resources contain over millions lexemes connected by thousands of thousands links that correspond to derivational relationship.
	 * And yet, these resources cover only part of the language and are heavily depended on the experts manual work (or let say, correction of generated content).
	 * 
	 * Let's try to train neural network, feed it with words similar in characters and find out, whether it can derive words relationship correctly.
	 * 
	 * @param baseWord {@link String}
	 * @return {@link List<String>} 
	 */
	public List<String> modelDerivationalRelations(String baseWord) {
		List<String> wordsSimilarInCharacter = word2VecService.getWordsSimilarInCharacter(baseWord, Word2VecService.DEFAULT_SIMILARITY_ACCURACY);
		List<INDArray> wordVectorMatrixes = word2VecService.getWordVectorMatrixes(wordsSimilarInCharacter);
		INDArray output = network.output(wordVectorMatrixes.get(0));
		
		log.info(output.getRow(0).toString());
		
		// return list of results
		return null;
	}
	
}
