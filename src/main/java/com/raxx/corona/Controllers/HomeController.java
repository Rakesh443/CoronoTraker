package com.raxx.corona.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.raxx.corona.models.Location;
import com.raxx.corona.services.DataRetriveService;

@Controller
public class HomeController {
	@Autowired
	DataRetriveService dataRetriveService;
	
	@GetMapping("/")
	public String home(Model model) {
		List<Location> allstats = dataRetriveService.getLoactions();
		int totalCases = allstats.stream().mapToInt(stats -> stats.getTotalCases()).sum();
//		int totalCases = 0;
		model.addAttribute("test",allstats );
		model.addAttribute("totalCases",totalCases );
		return "home";
	}

}
