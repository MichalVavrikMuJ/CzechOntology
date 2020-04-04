package michal.vavrik.diplomathesis.rest.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import michal.vavrik.diplomathesis.rest.controller.model.DeriNetRowDTO;
import michal.vavrik.diplomathesis.rest.controller.model.WikiArticleDTO;
import michal.vavrik.diplomathesis.services.ExtractArticlesFromWikiJsonService;
import michal.vavrik.diplomathesis.services.TsvParserService;
import michal.vavrik.diplomathesis.services.WikiArticlesService;


/**
 * Controller allows to operate whole application.
 * 
 * @author Michal Vavrik
 */
@Slf4j
@Controller
@RequestMapping(AppController.ROOT)
public class AppController {

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
		log.info("Started parsing and storing Derinet.");
		
		List<WikiArticleDTO> articles = extractArticlesService.getArticles();
		wikiArticlesService.saveArticles(articles);
		
		model.addAttribute("wikiArticle", articles);
		
		return "all";
	}
	
	@GetMapping("/derinet")
	public String parseAndStoreDerinet(Model model) throws IOException {
		log.info("Started parsing and storing Derinet.");
		
		try {
			List<DeriNetRowDTO> tsvRows = tsvService.getRows(context.getResources("file:*src/main/resources/vavrik/derinet-2-0")[0].getFile());
			tsvRows.forEach(row -> log.info("one row: {} and root {}", row, row.getRoot()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		model.addAttribute("wikiArticle", null);
		
		return "all";
	}

}
