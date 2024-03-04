package com.cogamers.no_recommand.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NoRecommandMapper {

	public void insertNoRecommand(
			@Param("postId") int postId,
			@Param("userId") int userId);
	
	public int selectNoRecommandCountByPostId(int postId);
}
