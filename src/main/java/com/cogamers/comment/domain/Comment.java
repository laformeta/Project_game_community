package com.cogamers.comment.domain;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Comment {

	private int id;
	private int lolPostId;
	private int userId;
	private String content;
	private Date createdAt;
	private Date updatedAt;
}
