package com.cogamers.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cogamers.post.bo.LolPostBO;
import com.cogamers.post.domain.LolPost;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/post")
public class PostController {
	
	@Autowired
	private LolPostBO lolPostBO;

	@GetMapping("/post-list-view")
	public String postListView(Model model) {
		
		List<LolPost> postList = lolPostBO.getAllLolPost();
		
		model.addAttribute("postList", postList);
		model.addAttribute("viewName", "post/postList");
		
		return "template/layout";
	}

	
	@GetMapping("/post-create-view")
	public String postCreateView(Model model) {
		
		model.addAttribute("viewName", "post/postCreate");
		
		return "template/layout";
	}
	
	@GetMapping("/post-detail-view")
	public String postDetailView(
			@RequestParam("postId") int postId,
			Model model,
			HttpSession session) {
		
		// DB 조회 - postId + userId
		LolPost lolPost = lolPostBO.getLolPostByPostId(postId);
		
		model.addAttribute("lolPost", lolPost);
		model.addAttribute("viewName", "post/postDetail");
		return "template/layout";
	}
	
	@GetMapping("/post-edit-view/{postId}")
	public String postEditView(@PathVariable("postId") int postId, Model model, HttpSession session) {
	    // 세션에서 로그인 ID를 가져옴
	    int userId = (int) session.getAttribute("userId");

	    // 글 정보를 가져오는 메서드 호출
	    LolPost lolPost = lolPostBO.getLolPostByPostIdUserId(postId, userId);

	    // 해당 글 정보를 모델에 추가
	    model.addAttribute("lolPost", lolPost);
	    model.addAttribute("viewName", "post/postEdit");

	    return "template/layout";
	}
}
