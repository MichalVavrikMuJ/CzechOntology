package michal.vavrik.diplomathesis.rest.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import michal.vavrik.diplomathesis.rest.model.DeriNetRowDTO;
import michal.vavrik.diplomathesis.rest.model.WikiArticleDTO;
import michal.vavrik.diplomathesis.services.ExtractArticlesFromWikiJsonService;
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

	@Autowired
	private ExtractArticlesFromWikiJsonService extractArticlesService;
	
	@Autowired
	private WikiArticlesService wikiArticlesService;
	
	@Autowired
	private TsvParserService tsvService;
	
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
	
	@GetMapping("/derinet")
	public String parseAndStoreDerinet(Model model) throws IOException {
		log.info("Started parsing and storing Derinet.");
		
		try {
			List<DeriNetRowDTO> derinetRows = tsvService.getRows(context.getResources(derinetFilePath)[0].getFile());
			model.addAttribute("derinetRows", derinetRows);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return "init/derinet";
	}

}
