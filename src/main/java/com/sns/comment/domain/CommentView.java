package com.sns.comment.domain;

import com.cogamers.comment.domain.Comment;
import com.cogamers.user.entity.UserEntity;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class CommentView {
	
	// 댓글 1개
	private Comment comment;
	
	// 댓글쓴이
	private UserEntity user;
}