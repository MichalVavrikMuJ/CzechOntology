package michal.vavrik.diplomathesis.rest.controller;

import java.util.List;

import org.nd4j.linalg.primitives.Pair;
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
	
	@Autowired
	List<Pair<double[],double[]>> trainingSet;
	
	@GetMapping("/train/{pageIndex}/{pageSize}")
	public String trainModel(Model model, @PathVariable(name = "pageIndex") int pageIndex, @PathVariable(name = "pageSize") int pageSize) {
		neuralNetworkService.trainNeuralNetworkModel(pageIndex, pageSize);
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("pageSize", pageSize);
		return "neural-network/trainNeuralNetworkModel";
	}
	
	@GetMapping("/train/")
	public String trainModel(Model model) {
		neuralNetworkService.trainNeuralNetworkModel(trainingSet);
		model.addAttribute("pageIndex", 0);
		model.addAttribute("pageSize", 1000);
		return "neural-network/trainNeuralNetworkModel";
	}
	
	@GetMapping("/evaluate/")
	public String evaluateModel(Model model) {
		neuralNetworkService.evaluateNeuralNetworkModel(trainingSet);
		model.addAttribute("pageIndex", 0);
		model.addAttribute("pageSize", 1000);
		return "neural-network/trainNeuralNetworkModel";
	}
	
	@GetMapping("/evaluate/{pageIndex}/{pageSize}")
	public String evaluateModel(Model model, @PathVariable(name = "pageIndex") int pageIndex, @PathVariable(name = "pageSize") int pageSize) {
		neuralNetworkService.evaluateNeuralNetworkModel(pageIndex, pageSize);
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("pageSize", pageSize);
		return "neural-network/trainNeuralNetworkModel";
	}
	
	@GetMapping("/decideWhetherDerivedFrom/{baseWord}/{derivedOrNotDerivedWord}")
	public String decideWhetherDerivedFrom(Model model, @PathVariable(name = "baseWord") String baseWord, @PathVariable(name = "derivedOrNotDerivedWord") String derivedOrNotDerivedWord) {
		model.addAttribute("derviedOrNot", neuralNetworkService.decideWhetherDerivedFrom(baseWord, derivedOrNotDerivedWord));
		return "neural-network/derivedOrNot";
	}

}
