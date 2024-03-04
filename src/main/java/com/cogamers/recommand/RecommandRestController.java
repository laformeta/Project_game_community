package com.cogamers.recommand;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cogamers.recommand.bo.RecommandBO;

import jakarta.servlet.http.HttpSession;

@RestController
public class RecommandRestController {

	@Autowired
	private RecommandBO recommandBO;
	
	@RequestMapping("/recommand/{postId}")
	public Map<String, Object> recommand(
			@PathVariable(name = "postId") int postId,
			HttpSession session){
		
		// 로그인 여부 확인
		Integer userId = (Integer)session.getAttribute("userId");
		
		Map<String, Object> result = new HashMap<>();
		
		if (userId == null) {
			result.put("code", 300);
			result.put("error_message", "로그인 필요");
			return result;
		}
		
		
		// BO 호출 >> giveRecommand
		recommandBO.giveRecommand(postId, userId);
		
		// 응답값
		result.put("code", 200);
		result.put("result", "성공");
		
		return result;
	}
}
