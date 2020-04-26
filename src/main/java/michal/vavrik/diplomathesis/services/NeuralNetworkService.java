 package michal.vavrik.diplomathesis.services;

import java.util.List;

import org.deeplearning4j.datasets.iterator.DoublesDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
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
		word2VecService.getWordsSimilarInCharacter(baseWord, Word2VecService.DEFAULT_SIMILARITY_ACCURACY);
		
		INDArray inputLayerData = Nd4j.zeros(2, 2);
		
		// return list of results
		return null;
	}

	public INDArray neuralNetwork(INDArray data) {
		return network.output(data , false);
	}
	
	public void trainNeuralNetwork(DoublesDataSetIterator iterator) {
		network.init();
		network.fit(iterator);
	}
	
}
