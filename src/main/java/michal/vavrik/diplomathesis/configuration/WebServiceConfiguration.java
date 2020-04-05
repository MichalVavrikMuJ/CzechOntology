package michal.vavrik.diplomathesis.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;

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
@EnableWebMvc
@Configuration
public class WebServiceConfiguration extends WsConfigurerAdapter {
	
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
