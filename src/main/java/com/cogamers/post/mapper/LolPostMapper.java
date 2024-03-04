package com.cogamers.post.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.annotations.Parent;

import com.cogamers.post.domain.LolPost;


@Mapper
public interface LolPostMapper {
	
	// input:X   output:List<Map>
	public List<Map<String, Object>> selectPostList();
	
	
	//수정완료
	public List<LolPost> selectLolPostListByUserId(
			@Param("userId") int userId,
			@Param("standardId") Integer standardId,
			@Param("direction") String direction,
			@Param("limit") int limit);
	
	public int selectLolPostIdByUserIdSort(
			@Param("userId") int userId,
			@Param("sort") String sort);
	
	public void insertLolPost(
			@Param("userId") int userId, 
			@Param("subject") String subject, 
			@Param("content") String content,
			@Param("category") String category,
			@Param("imagePath") String imagePath,
			@Param("nickname") String nickname);
	
	public List<LolPost> selectAllLolPost();
	
	public LolPost selectLolPostByPostIdUserId(
			@Param("postId") int postId, 
			@Param("userId") int userId);
	
	public List<LolPost> selectAllLolPostByCategory(
			@Param("category") String category);
	
	public LolPost selectLolPostByPostId(
			@Param("postId") int postid);
	
	public List<LolPost> selectLolPostsByKeyWord(
			@Param("keyword") String keyword);
	
	
	public void updateLolPostByPostId(
			@Param("postId") int postId, 
			@Param("subject") String subject, 
			@Param("content") String content, 
			@Param("category") String category,
			@Param("imagePath") String imagePath);
	
	public int deleteLolPostByPostId(int postId);
}