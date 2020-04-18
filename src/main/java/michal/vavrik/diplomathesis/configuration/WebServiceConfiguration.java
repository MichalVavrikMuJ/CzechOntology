package michal.vavrik.diplomathesis.configuration;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
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
