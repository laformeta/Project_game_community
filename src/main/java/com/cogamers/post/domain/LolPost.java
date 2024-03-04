package com.cogamers.post.domain;

import java.util.Date;
import java.util.List;

import com.cogamers.comment.domain.CommentView;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class LolPost {
	private int id;
	private int userId;
	private String nickname;
	private String subject;
	private String content;
	private String category;
	private String imagePath;
	private Date createdAt;
	private Date updatedAt;
	private int recommandCount;
	private int noRecommandCount;
	// 댓글들
	private List<CommentView> commentList;
}
