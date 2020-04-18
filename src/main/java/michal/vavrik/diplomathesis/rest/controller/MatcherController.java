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
	
	@GetMapping("/word2vec/{keyWord}")
	public String word2vec(Model model, @PathVariable(name = "keyWord") String keyWord) throws IOException {
		model.addAttribute("word2vec", linkerService.getWord2Vec(keyWord));
		model.addAttribute("keyWord", keyWord);
		return "matcher/word2vec";
	}
	
	@GetMapping("/word2vecSimilarity/{keyWord1}/{keyWord2}")
	public String word2vecSimilarity(Model model, @PathVariable(name = "keyWord1") String keyWord1, @PathVariable(name = "keyWord2") String keyWord2) throws IOException {
		model.addAttribute("similarity", linkerService.getWordsSimilarity(keyWord1, keyWord2));
		model.addAttribute("keyWord1", keyWord1);
		model.addAttribute("keyWord2", keyWord2);
		return "matcher/word2vec2Similarity";
	}

}
