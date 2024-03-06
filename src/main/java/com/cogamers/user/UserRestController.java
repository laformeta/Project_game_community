package com.cogamers.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cogamers.common.EncryptSHA256;
import com.cogamers.common.KakaoProfile;
import com.cogamers.common.OAuthToken;
import com.cogamers.recaptcha.RecaptchaService;
import com.cogamers.user.bo.UserBO;
import com.cogamers.user.entity.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserRestController {
	
	@Value("${cos.key}")
	private String cosKey;
	
	@Autowired
	private UserBO userBO;
	
	@Autowired
	private  RecaptchaService recaptchaService;
	
	@Autowired
    public UserRestController(UserBO userBO) {
        this.userBO = userBO;
    }
	

	/**
	 * id 중복확인 (is-duplicated-id)
	 * 
	 * 
	 * @param loginId
	 * 
	 */
	@RequestMapping("/is-duplicated-id")
	public Map<String, Object> isDuplicatedId(
			@RequestParam("loginId") String loginId){
		Map<String, Object> result = new HashMap<>();
		result.put("is_duplicated_id", false);

		// select
		UserEntity userEntity = userBO.getUserEntityByLoginId(loginId);
		result.put("code", 200);

		if (userEntity != null) {
			result.put("is_duplicated_id", true);
		} else {
			result.put("code", 200);
			result.put("is_duplicated_id", false);
		}

		return result;
	}
	
	/**
	 * 회원가입 API
	 * 
	 * @param loginId
	 * @param password
	 * @param name
	 * @param nickname
	 * @param email
	 * @return
	 */
	@PostMapping("/api/sign-up")
	public Map<String, Object> signUp(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password, 
			@RequestParam("name") String name,
			@RequestParam("nickname") String nickname,
			@RequestParam("email") String email,
			@RequestParam("g-recaptcha-response") String recaptchaResponse) {

		 // reCAPTCHA 검증
        if (!recaptchaService.verifyRecaptcha(recaptchaResponse)) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 500);
            errorResult.put("error_message", "reCAPTCHA 검증에 실패했습니다.");
            return errorResult;
        }
		
		// 비밀번호 해싱
		String hashedPassword = EncryptSHA256.testSHA256(password);

		// db insert
		String oauth ="";
		Integer userId = userBO.addUser(loginId, hashedPassword, name, nickname, email, oauth);

		Map<String, Object> result = new HashMap<>();
		if (userId != null) {
			// 응답
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("error_message", "회원가입에 실패했습니다.");
		}

		return result;
	}
	
	@PostMapping("/sign-in")
	public Map<String, Object> signIn(
			@RequestParam ("loginId") String loginId,
			@RequestParam ("password") String password,
			HttpServletRequest request) {
		
		// 비밀번호 hashing - md5 알고리즘 >> 데이터베이스에 저장된 해싱된 비밀번호와 대조하기위해
		String hashedPassword = EncryptSHA256.testSHA256(password);
		
		// db 조회(loginId, 해싱된 비밀번호) > UserEntity
		UserEntity user = userBO.getUserEntityByLoginIdPassword(loginId, hashedPassword);
		
		// 응답
		Map<String, Object> result = new HashMap<>();
		if (user != null) { // 성공
			// 로그인 처리
			// 로그인 정보를 세션에 담는다.(사용자 마다)
			HttpSession session = request.getSession();
			session.setAttribute("userId", user.getId());
			session.setAttribute("userLoginId", user.getLoginId());
			session.setAttribute("userNickname", user.getNickname());
			
			result.put("code", 200);
			result.put("result", "성공");
			
		} else { // 로그인 불가
			result.put("code", 300);
			result.put("error_message", "존재하지 않는 사용자입니다.");
		}
		return result;
		
	}
	
//	 @GetMapping("/auth/kakao/callback")
//	    public String kakaoCallback(
//	            @RequestParam("code") String code,
//	            Model model) {//데이터를 리턴해주는 컨트롤러 함수
//		
//		//post 방식 key=value 데이터를 요청(카카오쪽으로)
//		
//		//
//		RestTemplate rt = new RestTemplate();
//		
//		//HttpHeader 오브젝트 생성
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//		
//		//HTTPBody 오브젝트 생성
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("grant_type", "authorization_code");
//		params.add("client_id", "9b585a3db65d56c3f2ee027143053192");
//		params.add("code", code);
//		
//		// httpheader와 httpbody를 하나의 오브젝트에 담기
//		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = 
//				new HttpEntity<>(params, headers);
//		
//		// http요청하기 - post방식 - response 변수 응답받음
//		ResponseEntity<String> response = rt.exchange(
//				"https://kauth.kakao.com/oauth/token",
//				HttpMethod.POST,
//				kakaoTokenRequest,
//				String.class);
//		
//		ObjectMapper objectMapper = new ObjectMapper();
//		OAuthToken oauthToken = null;
//		
//		try {
//			 oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());
//		
//		RestTemplate rt2 = new RestTemplate();
//		
//		//HttpHeader 오브젝트 생성
//		HttpHeaders headers2 = new HttpHeaders();
//		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
//		
//		
//		// httpheader와 httpbody를 하나의 오브젝트에 담기
//		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = 
//				new HttpEntity<>(headers2);
//		
//		// http요청하기 - post방식 - response 변수 응답받음
//		ResponseEntity<String> response2 = rt2.exchange(
//				"https://kapi.kakao.com/v2/user/me",
//				HttpMethod.POST,
//				kakaoProfileRequest,
//				String.class);
//		
//		
//		ObjectMapper objectMapper2 = new ObjectMapper();
//		KakaoProfile kakaoProfile = null;
//		
//		try {
//			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 
//
//		log.info("####내 사이트 아이디 : {}#####", kakaoProfile.getKakao_account().getEmail());
//		log.info("####내 사이트 비밀번호 : {}#####", cosKey);
//		log.info("####내 사이트 이름 : {}#####", kakaoProfile.getProperties().getNickname());
//		log.info("####내 사이트 닉네임 : {}#####", kakaoProfile.getKakao_account().getProfile().getNickname());
//		log.info("####내 사이트 이메일 : {}#####", kakaoProfile.getKakao_account().getEmail());
//		
//		UserEntity userEntity = userBO.getUserEntityByLoginId(kakaoProfile.getKakao_account().getEmail());
//		Map<String, Object> result = new HashMap<>();
//		
//		if(userEntity == null) {// loginId / pw / name /nickname/email/oauth
//			String oauth = "kakao";
//				userBO.addUser(kakaoProfile.getKakao_account().getEmail(),
//						cosKey, 
//						kakaoProfile.getProperties().getNickname(),
//						kakaoProfile.getKakao_account().getProfile().getNickname(), 
//						kakaoProfile.getKakao_account().getEmail(), 
//						oauth);
//				return "신규가입이 완료됐습니다. 로그인을 진행합니다";
//		} else {
//			userBO.getUserEntityByLoginIdPassword(kakaoProfile.getKakao_account().getEmail(), cosKey);
//			return "이미 가입돼 있어 로그인을 진행합니다.";
//		}
//		
//	}
}
