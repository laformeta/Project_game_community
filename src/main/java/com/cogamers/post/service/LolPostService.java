package com.cogamers.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cogamers.comment.bo.CommentBO;
import com.cogamers.common.FileManagerService;
import com.cogamers.no_recommand.bo.NoRecommandBO;
import com.cogamers.post.entity.LolPostEntity;
import com.cogamers.post.repository.LolPostRepository;
import com.cogamers.recommand.bo.RecommandBO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LolPostService {

	@Autowired
	private LolPostRepository lolPostRepository;
	
	@Autowired
	private CommentBO commentBO;
	
	@Autowired
	private RecommandBO recommandBO;
	
	@Autowired
	private NoRecommandBO noRecommandBO;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	// input: X   output: List<PostEntity>
	public List<LolPostEntity> getPostList() {
		return postRepository.findAllByOrderByIdDesc();
	}
	
	public /*output*/LolPostEntity addPost(int userId, String userLoginId, String content, MultipartFile file) {
		
		String imagePath = null;
		
		// 업로드 할 이미지가 있으면 업로드
		if (file != null) {
			imagePath = fileManagerService.saveFile(userLoginId, file);
		}
		
		LolPostEntity postEntity = postRepository.save(
				LolPostEntity.builder()
				.userId(userId)
				.content(content)
				.imagePath(imagePath)
				.build()
				);
		
		return postEntity;
	}
	
	public void deletePostByPostIdUserId(int postId, int userId) {
		// 기존 글 가져오기
		LolPostEntity post = postRepository.findById(postId).orElse(null);
		if (post == null) {
			log.error("[delete post] postId:{}, userId:{}", postId, userId);
			return;
		}
		
		// 글 삭제
		postRepository.delete(post);
		
		// 이미지 있으면 삭제
		fileManagerService.deleteFile(post.getImagePath());
		
		// 댓글들 삭제
		commentBO.deleteCommentByPostId(postId);
		
		// 좋아요 삭제
		likeBO.deleteLikeByPostId(postId);
	}
}