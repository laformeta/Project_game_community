package com.cogamers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cogamers.aop.TimeTrace;
import com.cogamers.recaptcha.RecaptchaService;
import com.cogamers.user.bo.UserBO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	
	 @Autowired
	    private RecaptchaService recaptchaService;

	    @Autowired
	    private UserBO userBO;
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
	  @PostMapping("/sign-up")
	    public String signUp(
	            @RequestParam("loginId") String loginId,
	            @RequestParam("password") String password,
	            @RequestParam("name") String name,
	            @RequestParam("nickname") String nickname,
	            @RequestParam("email") String email,
	            @RequestParam("g-recaptcha-response") String recaptchaResponse,
	            Model model) {

	        // reCAPTCHA 검증
	        if (!recaptchaService.verifyRecaptcha(recaptchaResponse)) {
	            model.addAttribute("recaptchaError", "reCAPTCHA 검증에 실패했습니다.");
	            return "user/sign-up-view"; // 검증 실패 시 다시 회원가입 페이지로 이동
	        }

	        // reCAPTCHA 검증 성공 시 회원 가입 로직 수행
	        Integer userId = userBO.addUser(loginId, password, name, nickname, email);

	        if (userId != null) {
	            // 회원 가입 성공
	            return "redirect:/user/sign-in-view"; // 로그인 페이지로 이동
	        } else {
	            // 회원 가입 실패 로직 (예: 에러 메시지 설정)
	            model.addAttribute("signupError", "회원 가입에 실패했습니다.");
	            return "user/sign-up-view"; // 실패 시 다시 회원가입 페이지로 이동
	        }
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
	
	@RequestMapping("/sign-out")
	public String signOut(HttpSession session) {
		// 세션을 모두 비운다.
		session.removeAttribute("userId");
		session.removeAttribute("userLoginId");
		session.removeAttribute("userNickname");
		
		// 로그인 화면으로 이동
		return "redirect:/user/sign-in-view";
	}
	
	@GetMapping("/withdrawal-view")
	public String withdrawal(Model model) {
		model.addAttribute("viewName", "user/withdrawal");
		return "template/layout";
	}
	

	
}
