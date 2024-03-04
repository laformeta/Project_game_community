package com.cogamers.comment.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cogamers.comment.domain.Comment;
import com.cogamers.comment.mapper.CommentMapper;
import com.cogamers.user.bo.UserBO;
import com.cogamers.user.entity.UserEntity;
import com.sns.comment.domain.CommentView;

@Service
public class CommentBO {

	@Autowired
	private CommentMapper commentMapper;
	
	@Autowired
	private UserBO userBO;
	
	public void addComment(int lolPostId, int userId, String content) {
		commentMapper.insertComment(lolPostId, userId, content);
	}
	
	public List<Comment> getCommentList() {
		return commentMapper.selectCommentList();
	}
	
	public List<CommentView> generateCommentViewListByPostId(int postId) {
		// 결과 리스트 만들기
		List<CommentView> commentViewList = new ArrayList<>(); // []
		
		// 글에 해당하는 댓글 목록 가져오기 List<Comment>
		List<Comment> commentList = commentMapper.selectCommentListByPostId(postId);
		
		// 반복문 순회 List<Comment> -> List<CommentView>  => 리스트에 넣기
		for (Comment comment : commentList) {
			CommentView commentView = new CommentView();
			
			// 댓글 1개
			commentView.setComment(comment);
			
			// 댓글쓴이 
			UserEntity user = userBO.getUserEntityById(comment.getUserId());
			commentView.setUser(user);
			
			// 결과 리스트에 담기
			commentViewList.add(commentView);
		}
		
		// 결과 리스트 리턴
		return commentViewList;
	}
	
	public void deleteCommentById(int id) {
		commentMapper.deleteCommentById(id);
	}
	
	public void deleteCommentByLolPostId(int lolPostId) {
		commentMapper.deleteCommentByLolPostId(lolPostId);
	}
}