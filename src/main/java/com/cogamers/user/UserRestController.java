package com.cogamers.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cogamers.common.EncryptSHA256;
import com.cogamers.recaptcha.RecaptchaService;
import com.cogamers.user.bo.UserBO;
import com.cogamers.user.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserRestController {
	
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
		Integer userId = userBO.addUser(loginId, hashedPassword, name, nickname, email);

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
			session.setAttribute("userName", user.getName());
			
			result.put("code", 200);
			result.put("result", "성공");
			
		} else { // 로그인 불가
			result.put("code", 300);
			result.put("error_message", "존재하지 않는 사용자입니다.");
		}
		return result;
		
	}
}
