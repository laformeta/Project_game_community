package com.cogamers.post;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cogamers.post.service.LolPostService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@RestController
public class LolPostRestController {

	@Autowired
	private LolPostService lolPostService;
	
	@PostMapping("/create")
	public Map<String, Object> create(
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam("category") String category,
			@RequestParam("nickname") String nickname,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session) {
		
		// 글쓴이 번호 - session에 있는 userId를 꺼낸다.]
		int userId = (int)session.getAttribute("userId");
		String userLogindId = (String)session.getAttribute("userLoginId");
		String userNickname = (String) session.getAttribute("userNickname");
		
		// DB  INSERT
		lolPostBO.addLolPost(userId, userLogindId, category, subject, content, userNickname, file);
		
		
		// RESPONSE VALUE
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("result", "성공");
		return result;
	}
}
