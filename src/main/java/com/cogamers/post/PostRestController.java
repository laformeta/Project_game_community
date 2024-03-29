package com.cogamers.post;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cogamers.post.bo.LolPostBO;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@RestController
public class PostRestController {

	@Autowired
	private LolPostBO lolPostBO;
	
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
	
	@PutMapping("/update")
	public Map<String, Object> update(
			@RequestParam("postId") int postId,
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam("category") String category,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session) {
		
		// 글쓴이 번호 - session에 있는 userId를 꺼낸다.]
		int userId = (int)session.getAttribute("userId");
		String userLoginId = (String)session.getAttribute("userLoginId");

		// DB update
		lolPostBO.updateLolPostById(userId, userLoginId, category, postId, subject, content, file);
		
		// 응답
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("result", "성공");
		return result;
	}
	
	
	@DeleteMapping("/delete")
	public Map<String, Object> delete(
			@RequestParam("postId") int postId,
			HttpSession session) {
		
		int userId = (int)session.getAttribute("userId");
		
		// DB delete
		lolPostBO.deletePostByPostIdUserId(postId, userId);
		
		
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("result", "성공");
		return result;
	}
	
	
	
}
