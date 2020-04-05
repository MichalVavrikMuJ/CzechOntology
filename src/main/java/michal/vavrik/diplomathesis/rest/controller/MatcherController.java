package michal.vavrik.diplomathesis.rest.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import michal.vavrik.diplomathesis.services.WikiDerinetLinkerService;

@Slf4j
@Controller
@RequestMapping(MatcherController.ROOT)
public class MatcherController {

	
	public static final String ROOT = "/matcher";
	
	@Autowired
	private WikiDerinetLinkerService linkerService;
	
	@GetMapping("/wiki/{keyWord}")
	public String matchWikiArticlesWithKeyWord(Model model, @PathVariable(name = "keyWord") String keyWord) throws IOException {
		log.info("Showing article for key word: {}.", keyWord);
		
		model.addAttribute("wikiArticles", linkerService.linkKeyWordWithWiki(keyWord));
		model.addAttribute("keyWord", keyWord);
		
		return "matcher/wiki";
	}
	
	@GetMapping("/derinet/{keyWord}")
	public String matchDeriNetRowWithKeyWord(Model model, @PathVariable(name = "keyWord") String keyWord) throws IOException {
		log.info("Showing DeriNetRow entities for key word: {}.", keyWord);
		
		model.addAttribute("deriNetRows", linkerService.linkKeyWordWithDeriNetRow(keyWord));
		model.addAttribute("keyWord", keyWord);
		
		return "matcher/derinet";
	}

}
