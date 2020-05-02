package michal.vavrik.diplomathesis.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import michal.vavrik.diplomathesis.services.NeuralNetworkService;

@Slf4j
@Controller
@RequestMapping(NeuralNetworkController.ROOT)
public class NeuralNetworkController {
	
	public static final String ROOT = "/neuralNetwork";
	
	@Autowired
	private NeuralNetworkService neuralNetworkService;
	
	@GetMapping("/{baseWord}")
	public String derivedWordsFrom(Model model, @PathVariable(name = "baseWord") String baseWord) {
		model.addAttribute("baseWord", baseWord);
		model.addAttribute("listOfDerived", neuralNetworkService.modelDerivationalRelations(baseWord));
		return "neural-network/derivedWords";
	}

}
