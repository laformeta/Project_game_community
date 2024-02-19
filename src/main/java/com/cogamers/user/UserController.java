package com.cogamers.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cogamers.aop.TimeTrace;

@RequestMapping("/user")
@Controller
public class UserController {

	/*
	 * 회원가입 화면(veiw)
	 * @param model
	 * @return
	 * */
	@TimeTrace
	@GetMapping("/sign-up-view")
	public String signUpView(Model model) {
		
		model.addAttribute("viewName", "user/signUp");
		
		return "template/layout";
	}
	
	/**
	 * 로그인 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/sign-in-view")
	public String signInView(Model model) {
		model.addAttribute("viewName", "user/signIn");
		return "template/layout";
	}
	
	@GetMapping("/withdrawal-view")
	public String withdrawal(Model model) {
		model.addAttribute("viewName", "user/withdrawal");
		return "template/layout";
	}
	
}
