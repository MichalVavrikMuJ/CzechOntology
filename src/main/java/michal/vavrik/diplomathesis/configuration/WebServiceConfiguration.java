package michal.vavrik.diplomathesis.configuration;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.deeplearning4j.datasets.iterator.DoublesDataSetIterator;
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
	MatcherService matcherService;
	
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
		int numOfInputs = word2Vec.getWordVector("ano").length;
		return new NeuralNetConfiguration.Builder()
			    .seed(65432)
			    .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
			    .updater(new Adam(0.1))
			    .list()
			    .layer(0, new DenseLayer.Builder().nIn(numOfInputs).nOut(numOfInputs)
			            .weightInit(WeightInit.XAVIER)
			            .activation(Activation.RELU)
			            .build())
			    .layer(1, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
			            .weightInit(WeightInit.XAVIER)
			            .activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER)
			            .nIn(numOfInputs).nOut(1).build())
			    .build();
	}
	
	@Bean MultiLayerNetwork multiLayerNetwork(MultiLayerConfiguration conf) {
		log.info("Started creating MultiLayerNetwork.");
		MultiLayerNetwork multiLayerNetwork = new MultiLayerNetwork(conf);
		multiLayerNetwork.init();
		DoublesDataSetIterator iterator = new DoublesDataSetIterator(matcherService.getTrainigSet(), 1);
		// TODO: decide on number of training set iterations!!! 1000?
		for(int i = 0; i < 1; i++) {
			log.info("Started fitting network, iteration n. {}", i);
			multiLayerNetwork.fit(iterator);
		}
		return multiLayerNetwork;
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
