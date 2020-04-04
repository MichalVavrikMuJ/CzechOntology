package michal.vavrik.diplomathesis.rest.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
import michal.vavrik.diplomathesis.rest.controller.model.WikiArticleDTO;
import michal.vavrik.diplomathesis.services.ExtractArticlesFromWikiJsonService;
import michal.vavrik.diplomathesis.services.WikiArticlesService;

/**
 * Controller allows to operate whole application.
 * 
 * @author Michal Vavrik
 */
@Slf4j
@Controller
//@RequestMapping(AppController.ROOT)
public class AppController {
	
	@Autowired
	private ExtractArticlesFromWikiJsonService extractArticlesService;
	
	@Autowired
	private WikiArticlesService wikiArticlesService;
	
	public static final String ROOT = "/wiki";
	
	
	@GetMapping("/")
	public String viewWikiArticles(Model model) throws IOException {
		log.info("Processing request with model {}", model);
		
		List<WikiArticleDTO> articles = extractArticlesService.getArticles();
		wikiArticlesService.saveArticles(articles);
		
		model.addAttribute("wikiArticle", articles);
		
		return "all";
	}

}
