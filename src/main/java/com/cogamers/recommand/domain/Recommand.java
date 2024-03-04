package com.cogamers.recommand.domain;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Recommand {

	private int postId;
	private int userId;
	private Date createdAt;
}
