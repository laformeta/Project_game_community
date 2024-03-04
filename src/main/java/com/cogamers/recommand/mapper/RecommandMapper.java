package com.cogamers.recommand.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RecommandMapper {

	public void insertRecommand(
			@Param("postId") int postId,
			@Param("userId") int userId);
	
	public int selectRecommandCountByPostId(int postId);
	
	public void deleteRecommandByPostId(int postId);
}
