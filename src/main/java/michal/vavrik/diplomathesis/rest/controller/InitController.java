package michal.vavrik.diplomathesis.rest.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;
import michal.vavrik.diplomathesis.rest.model.WikiArticleDTO;
import michal.vavrik.diplomathesis.services.DeriNetService;
import michal.vavrik.diplomathesis.services.ExtractArticlesFromWikiJsonService;
import michal.vavrik.diplomathesis.services.MatcherService;
import michal.vavrik.diplomathesis.services.TsvParserService;
import michal.vavrik.diplomathesis.services.WikiArticlesService;


/**
 * Controller allows to init whole application. I did not employ migrations, so db has got to be initialized manually.
 * 
 * @author Michal Vavrik
 */
@Slf4j
@Controller
@RequestMapping(InitController.ROOT)
public class InitController {
	
	@Value("${ufal.derinet}")
	private String derinetFilePath;
	
	@Value("${ufal.testExampleDerinet}")
	private String derinetSmallTestExample;

	@Autowired
	private ExtractArticlesFromWikiJsonService extractArticlesService;
	
	@Autowired
	private WikiArticlesService wikiArticlesService;
	
	@Autowired
	private TsvParserService tsvService;
	
	@Autowired
	private DeriNetService deriNetService;
	
	@Autowired
	private ApplicationContext context;
	
	public static final String ROOT = "/init";
	
	
	@GetMapping("/wiki")
	public String parseAndStoreWiki(Model model) throws IOException {
		log.info("Started parsing and storing Wikipedia dump.");
		
		List<WikiArticleDTO> articles = extractArticlesService.getArticles();
		wikiArticlesService.saveArticles(articles);
		
		model.addAttribute("wikiArticle", articles);
		
		return "init/wiki";
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping("/derinet")
	public String parseAndStoreDerinet(Model model) throws IOException {
		log.info("Started parsing and storing Derinet.");
		
		try {
			Arrays.stream(context.getResources(derinetFilePath)).map(tsvService::getRows).forEach(deriNetService::saveDeriNetRows);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return "Process of parsing/storing has begun. See console more informations.";
	}
	
	@GetMapping("/derivedWords")
	public String extractAndStoreDerivedWordsInDb(MatcherService matcherService) throws IOException {
		log.info("Started extracting and storing derived words in db.");

		// FIXME: finish
		
		return "";
	}

}
