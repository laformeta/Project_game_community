package com.cogamers.comment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cogamers.comment.domain.Comment;

@Mapper
public interface CommentMapper {

	public void insertComment(
			@Param("lolPostId") int lolPostId, 
			@Param("userId") int userId, 
			@Param("content") String content);
	
	public List<Comment> selectCommentList();
	
	public List<Comment> selectCommentListByPostId(int lolPostId);
	
	public void deleteCommentById(int id);
	
	public void deleteCommentByLolPostId(int lolPostId);
}
