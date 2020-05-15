package michal.vavrik.diplomathesis.configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.nd4j.linalg.primitives.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import michal.vavrik.diplomathesis.database.entity.DeriNetRow;
import michal.vavrik.diplomathesis.rest.model.DeriNetRowDTO;
import michal.vavrik.diplomathesis.services.MatcherService;

/**
 * Serves for configuring web service.
 *
 */
@Slf4j
@EnableWebMvc
@Configuration
public class WebServiceConfiguration extends WsConfigurerAdapter {
	
	@Value("${fastText.word2vecModel}")
	private String word2vecModelPattern;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private MatcherService matcherService;
	
	@Value("${neuralNetworkModel.trainingSetModel}")
	private String neuralNetworkModelFilePath;
	
	@Bean
	public MapperFacade mapperFacade() {
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		mapperFactory.getConverterFactory().registerConverter("createEntity", new CustomConverter<String, DeriNetRow>() {
			@Override
			public DeriNetRow convert(String source, Type<? extends DeriNetRow> destinationType,
					MappingContext mappingContext) {
				return StringUtils.isEmpty(source) ? null : DeriNetRow.builder().id(Double.parseDouble(source)).build();
			}
		});
		mapperFactory
				.classMap(DeriNetRowDTO.class, DeriNetRow.class)
				.fieldMap("mainParentID", "mainParent").converter("createEntity").add()
				.byDefault()
				.register();
		return mapperFactory.getMapperFacade();
	}
	
	@Bean
	public Word2Vec word2Vec() throws IOException {
		try {
		Resource[] resources = context.getResources(word2vecModelPattern);

		if (resources.length < 1) {
			log.error("You must download word2vec model first, as it is not part of git repository. Download it to /src/resources/fasttext from https://fasttext.cc/docs/en/crawl-vectors.html#models");
		}
		
		log.info("Starting process of loading word2vec model, wait until it is loaded");
		return WordVectorSerializer.readWord2VecModel(resources[0].getFile());
		} finally {
			log.info("Successfully loaded word2vec model");
		}
	}
	
	@Bean
	public MultiLayerConfiguration multiLayerConfiguration(Word2Vec word2Vec) {
		log.info("Started preparing multi layer configuration.");
		int numOfInputs = 600; // AKA 2 * word2Vec.getWordVector("ano").length;
		int numOfHiddenNodes = numOfInputs + 50;
		int numOfOutputs = 2;

		return new NeuralNetConfiguration.Builder()
			    .seed(65353)
			    .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
			    .updater(new Adam(5e-3))
			    .list()
			    .layer(0, new DenseLayer.Builder().nIn(numOfInputs).nOut(numOfHiddenNodes)
			            .weightInit(WeightInit.XAVIER)
			            .activation(Activation.RELU)
			            .build())
			    .layer(1, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
			            .weightInit(WeightInit.XAVIER)
			            .activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER)
			            .nIn(numOfHiddenNodes).nOut(numOfOutputs).build())
			    .build();
		
		// current version
//		LSTM layer0 = new LSTM.Builder().nIn(numOfInputs).nOut(numOfHiddenNodes).activation(Activation.RELU).build();
//		LSTM layer1 = new LSTM.Builder().nIn(numOfHiddenNodes).nOut(numOfHiddenNodes).activation(Activation.RELU).build();
//		LSTM layer2 = new LSTM.Builder().nIn(numOfHiddenNodes).nOut(numOfHiddenNodes).activation(Activation.RELU).build();
//		RnnOutputLayer layer3 = new RnnOutputLayer.Builder().nIn(numOfHiddenNodes).nOut(numOfOutputs).activation(Activation.SOFTMAX).lossFunction(LossFunction.MCXENT).build();
//
//		
		
//		return new NeuralNetConfiguration.Builder()
//				.seed(500)
//				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
//				.weightInit(WeightInit.XAVIER)
//				.updater(new Adam(5e-3))
//				.l2(1e-5)
//				.list()
//					.layer(0, layer0)
//					.layer(1, layer1)
//					.layer(2, layer2)
//					.layer(3, layer3)
//				.backpropType(BackpropType.Standard)
////				.backpropType(BackpropType.TruncatedBPTT)
////				.tBPTTBackwardLength(2)
////				.tBPTTForwardLength(2)
//				.build();
		
		// second version
		//		return new NeuralNetConfiguration.Builder()
//			    .seed(65432)
//			    .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
//			    .updater(new Adam(0.1))
//			    .list()
//			    .layer(0, new DenseLayer.Builder().nIn(numOfInputs).nOut(numOfInputs)
////                      .weightInit(WeightInit.DISTRIBUTION).dist(new UniformDistribution(0,1))
//                      .activation(Activation.SIGMOID)
//                      .build())
//              .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
////                      .weightInit(WeightInit.DISTRIBUTION).dist(new UniformDistribution(0,1))
//                      .activation(Activation.SIGMOID)
//                      .nIn(numOfInputs).nOut(2).build())
//              .build();
		
		// first version
//		return new NeuralNetConfiguration.Builder()
//			    .seed(65432)
//			    .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
//			    .updater(new Adam(0.1))
//			    .list()
//			    .layer(0, new DenseLayer.Builder().nIn(numOfInputs).nOut(numOfInputs)
////                      .weightInit(WeightInit.DISTRIBUTION).dist(new UniformDistribution(0,1))
//                      .activation(Activation.SIGMOID)
//                      .build())
//			    .layer(1, new DenseLayer.Builder().nIn(numOfInputs).nOut(numOfInputs / 2)
////                      .weightInit(WeightInit.DISTRIBUTION).dist(new UniformDistribution(0,1))
//                      .activation(Activation.SIGMOID)
//                      .build())
//                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
//                      .weightInit(new UniformDistribution(0,1))
//                      .activation(Activation.SOFTMAX)
//                      .nOut(2).build())
//                .build();
	}
	
	@Bean
	List<Pair<double[],double[]>> trainingSet() {
		return matcherService.getTrainigSet(0, 1000);
	}
	
	@Bean(name = "loadedNeuralNetworkModel")
	MultiLayerNetwork loadedNeuralNetworkModel(File trainingSetModelFile) throws IOException{
		log.info("Started loading MultiLayerNetwork.");
		return MultiLayerNetwork.load(trainingSetModelFile, true);
	}
	
	@Bean(name = "createdNeuralNetworkModel")
	MultiLayerNetwork createdNeuralNetworkModel(MultiLayerConfiguration conf) throws IOException{
		log.info("Started creating MultiLayerNetwork.");
		MultiLayerNetwork multiLayerNetwork = new MultiLayerNetwork(conf);
		multiLayerNetwork.init();
		return multiLayerNetwork;
	}
	
	@Bean
	File trainingSetModelFile() throws IOException {
		return context.getResources(neuralNetworkModelFilePath)[0].getFile();
	}
	
//	@Bean
//    public SpringTemplateEngine templateEngine() {
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
//        return templateEngine;
//    }
// 
//    @Bean
//    public SpringResourceTemplateResolver thymeleafTemplateResolver() {
//        SpringResourceTemplateResolver templateResolver 
//          = new SpringResourceTemplateResolver();
////        templateResolver.setPrefix("*\\templates\\view");
//        templateResolver.setSuffix(".html");
//        templateResolver.setTemplateMode("HTML5");
//        return templateResolver;
//    }
//    
//    @Bean
//    public ThymeleafViewResolver thymeleafViewResolver() {
//        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//        viewResolver.setTemplateEngine(templateEngine());
//        return viewResolver;
//    }
	
}
