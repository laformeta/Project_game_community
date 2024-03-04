package com.cogamers.no_recommand.domain;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class NoRecommand {

	private int postId;
	private int userId;
	private Date createdAt;
}
