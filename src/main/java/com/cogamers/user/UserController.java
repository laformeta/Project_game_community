package com.cogamers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.cogamers.aop.TimeTrace;
import com.cogamers.common.KakaoProfile;
import com.cogamers.common.OAuthToken;
import com.cogamers.recaptcha.RecaptchaService;
import com.cogamers.user.bo.UserBO;
import com.cogamers.user.entity.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

	@Value("${cos.key}")
	private String cosKey;

	@Autowired
	private RecaptchaService recaptchaService;

	@Autowired
	private UserBO userBO;

	/*
	 * 회원가입 화면(veiw)
	 * 
	 * @param model
	 * 
	 * @return
	 */
	@TimeTrace
	@GetMapping("/sign-up-view")
	public String signUpView(Model model) {

		model.addAttribute("viewName", "user/signUp");

		return "template/layout";
	}

	@PostMapping("/sign-up")
	public String signUp(@RequestParam("loginId") String loginId, @RequestParam("password") String password,
			@RequestParam("name") String name, @RequestParam("nickname") String nickname,
			@RequestParam("email") String email, @RequestParam("oauth") String oauth,
			@RequestParam("g-recaptcha-response") String recaptchaResponse, Model model) {

		// reCAPTCHA 검증
		if (!recaptchaService.verifyRecaptcha(recaptchaResponse)) {
			model.addAttribute("recaptchaError", "reCAPTCHA 검증에 실패했습니다.");
			return "user/sign-up-view"; // 검증 실패 시 다시 회원가입 페이지로 이동
		}

		// reCAPTCHA 검증 성공 시 회원 가입 로직 수행
		Integer userId = userBO.addUser(loginId, password, name, nickname, email, oauth);

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
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/sign-in-view")
	public String signInView(Model model, HttpSession session) {

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

	@GetMapping("/auth/kakao/callback")
//	    public ResponseEntity<String> kakaoCallback(
	public String kakaoCallback(@RequestParam("code") String code, Model model,
			HttpSession session) {// 데이터를 리턴해주는 컨트롤러 함수

		// post 방식 key=value 데이터를 요청(카카오쪽으로)

		//
		RestTemplate rt = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HTTPBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "9b585a3db65d56c3f2ee027143053192");
		params.add("redirect_uri", "http://localhost/user/auth/kakao/callback");
		params.add("code", code);

		// httpheader와 httpbody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

		// http요청하기 - post방식 - response 변수 응답받음
		ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST,
				kakaoTokenRequest, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;

		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());

		RestTemplate rt2 = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());

		// httpheader와 httpbody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

		// http요청하기 - post방식 - response 변수 응답받음
		ResponseEntity<String> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
				kakaoProfileRequest, String.class);

		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;

		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.info("####내 사이트 아이디 : {}#####", kakaoProfile.getKakao_account().getEmail());
		log.info("####내 사이트 비밀번호 : {}#####", cosKey);
		log.info("####내 사이트 이름 : {}#####", kakaoProfile.getProperties().getNickname());
		log.info("####내 사이트 닉네임 : {}#####", kakaoProfile.getKakao_account().getProfile().getNickname());
		log.info("####내 사이트 이메일 : {}#####", kakaoProfile.getKakao_account().getEmail());

		
		  // 예시: 가져온 사용자 정보
	    String loginId = kakaoProfile.getKakao_account().getEmail();
	    String nickname = kakaoProfile.getProperties().getNickname();
		String email = kakaoProfile.getKakao_account().getEmail();
		String name= kakaoProfile.getProperties().getNickname();
		String oauth = "kakao";
	    
		UserEntity user = userBO.getUserEntityByLoginIdNameOauth(loginId, name, oauth);

		if (user == null) {// loginId / pw / name /nickname/email/oauth
			userBO.addUser(loginId, cosKey,
					name,
					kakaoProfile.getKakao_account().getProfile().getNickname(),
					email, oauth);
			return "redirect:/post/post-list-view";
		} 
		
		 // 로그인 처리
	    session.setAttribute("userId", user.getId());
	    session.setAttribute("userLoginId", user.getLoginId());
	    session.setAttribute("userNickname", user.getNickname());
	    
	    log.info("####유저 아이디 : {}#####", user.getId());
		return "redirect:/post/post-list-view";
	}

}
