package com.cogamers.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cogamers.comment.bo.CommentBO;
import com.cogamers.comment.domain.Comment;
import com.cogamers.comment.domain.CommentView;
import com.cogamers.post.bo.LolPostBO;
import com.cogamers.post.domain.LolPost;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/post")
public class PostController {
	
	@Autowired
	private LolPostBO lolPostBO;
	
	@Autowired
	private CommentBO commentBO;

	@GetMapping("/post-list-view")
	public String postListView(
			@RequestParam(name = "category", required = false, defaultValue = "") String category,
			@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
			Model model) {
	    List<LolPost> postList= lolPostBO.getAllLolPost();
	    
	    
	    if (!keyword.isEmpty()) {
	        // 키워드가 존재하는 경우, 검색 기능 사용
	        postList = lolPostBO.getLolPostsByKeyword(keyword);
	    } else {
	        // 키워드가 없는 경우, 카테고리에 따라 게시글 가져오기
	        if (category.isEmpty() || category.equals("전체")) {
	            // 카테고리가 선택되지 않거나 '전체'인 경우, 전체 카테고리의 게시글을 가져옴
	            postList = lolPostBO.getAllLolPost();
	        } else {
	            // 선택된 카테고리에 해당하는 게시글을 가져옴
	            postList = lolPostBO.getAllLolPostByCategory(category);
	        }
	    }
	   

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
		
		// 추천 개수 불러오기
		int recommandCount = lolPostBO.getRecommandCount(postId);
		lolPost.setRecommandCount(recommandCount);
		
		// 비추 개수 불러오기
		int noRecommandCount =lolPostBO.getNoRecommandCount(postId);
		lolPost.setNoRecommandCount(noRecommandCount);
		
		// 댓글들
		List<CommentView> commentList = commentBO.generateCommentViewListByPostId(postId);
		lolPost.setCommentList(commentList);
		
		model.addAttribute("lolPost", lolPost);
		model.addAttribute("viewName", "post/postDetail");
		
		
		
		// 현재 로그인한 사용자의 ID를 세션에서 가져와서 모델에 추가
        Integer userId = (Integer) session.getAttribute("userId");
        model.addAttribute("loggedInUserId", userId);
		
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
