package com.cogamers.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomepageController {

	@GetMapping("/homepage/homepage-view")
	public String homepageView(Model model) {
		
		model.addAttribute("viewName", "homepage/homepage");
		//
		return "template/layout";
	}
}
