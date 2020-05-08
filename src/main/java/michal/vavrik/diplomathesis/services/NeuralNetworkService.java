 package michal.vavrik.diplomathesis.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.datavec.api.records.metadata.RecordMetaData;
import org.deeplearning4j.datasets.iterator.DoublesDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.evaluation.meta.Prediction;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.primitives.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class NeuralNetworkService {
	
	@Autowired
	private Word2VecService word2VecService;
	
	@Autowired
	private MultiLayerNetwork network;
	
	@Autowired
	private File trainingSetModelFile;

	@Autowired
	private MatcherService matcherService;
	
	@Autowired
	private MultiLayerNetwork multiLayerNetwork;
	
	/**
	 * Large linguistic resources contain over millions lexemes connected by thousands of thousands links that correspond to derivational relationship.
	 * And yet, these resources cover only part of the language and are heavily depended on the experts manual work (or let say, correction of generated content).
	 * 
	 * Let's try to train neural network, feed it with words similar in characters and find out, whether it can derive words relationship correctly.
	 * 
	 * @param baseWord {@link String}
	 * @return {@link List<String>} 
	 */
	public boolean decideWhetherDerivedFrom(String baseWord, String derivedOrNotDerivedWord) {
		INDArray neuralNetworkInput = matcherService.getNeuralNetworkInput(baseWord, derivedOrNotDerivedWord);
		if (neuralNetworkInput == null) { 
			log.error("Could not find word matrixes in word2vec model.");
			return false;
		}
		INDArray output = network.output(neuralNetworkInput);
		boolean derived = output.getRow(0).toString().equals("[1.0000]");
		log.info("Is {} derived from {}? {} ({})", baseWord, derivedOrNotDerivedWord, Boolean.toString(derived), output.getRow(0).toString());
		
		// return list of results
		return derived;
	}
	
	public void trainNeuralNetworkModel(int pageIndex, int pageSize) {
		List<Pair<double[],double[]>> trainingSet = matcherService.getTrainigSet(pageIndex, pageSize);
		if (trainingSet != null && trainingSet.size() > 0) {
			DoublesDataSetIterator iterator = new DoublesDataSetIterator(trainingSet, 1);
			for(int i = 0; i < 1000; i++) {
				log.info("Started fitting network, iteration n. {}", i);
				multiLayerNetwork.fit(iterator);
				try {
					multiLayerNetwork.save(trainingSetModelFile, true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			log.error("NEURAL NETWORK COULD NOT BE TRAINED - PROVIDED TRAINING SET IS EMPTY!");
		}
	}
	
	public void evaluateNeuralNetworkModel(int pageIndex, int pageSize) {
		List<Pair<double[],double[]>> trainingSet = matcherService.getTrainigSet(pageIndex, pageSize);
		if (trainingSet != null && trainingSet.size() > 0) {
		DoublesDataSetIterator iterator = new DoublesDataSetIterator(trainingSet, 1);
		Evaluation eval = multiLayerNetwork.evaluate(iterator);
        List<Prediction> predictionErrors = eval.getPredictionErrors();
        log.info("\n\n+++++ Prediction Errors +++++");
        if (predictionErrors != null) {
            for (Prediction p : predictionErrors) {
                log.info("Predicted class: {}, Actual class: {} {}", p.getPredictedClass(), p.getActualClass(), p.getRecordMetaData(RecordMetaData.class));
            }
        }
        //Print the evaluation statistics
        log.info(eval.stats());
		} else {
			log.error("NEURAL NETWORK COULD NOT BE EVALUATED - PROVIDED TRAINING SET IS EMPTY!");
		}
	}
	
}
