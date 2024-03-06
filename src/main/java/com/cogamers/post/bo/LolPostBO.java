package com.cogamers.post.bo;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cogamers.common.FileManagerService;
import com.cogamers.no_recommand.bo.NoRecommandBO;
import com.cogamers.post.domain.LolPost;
import com.cogamers.post.mapper.LolPostMapper;
import com.cogamers.recommand.bo.RecommandBO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LolPostBO {
	// private Logger logger = LoggerFactory.getLogger(PostBO.class);
	// private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LolPostMapper lolPostMapper;
	
	@Autowired 
	private RecommandBO recommandBO;
	
	@Autowired 
	private NoRecommandBO noRecommandBO;

	@Autowired
	private FileManagerService fileManagerService;

	

	// input: params output: X
	public void addLolPost(int userId, String userLoginId, 
		 String category, String subject, String content, String nickname,
			MultipartFile file) {

		String imagePath = null;

		// 업로드할 이미지가 있을 때 업로드
		if (file != null) {
			imagePath = fileManagerService.saveFile(userLoginId, file);
		}

		lolPostMapper.insertLolPost(userId, subject, content, category, imagePath, nickname);
	}
	
	

	// input: 글번호, userId output: Post
	public LolPost getLolPostByPostIdUserId(int id, int userId) {
		return lolPostMapper.selectLolPostByPostIdUserId(id, userId);
	}
	
	public LolPost getLolPostByPostId(int postId) {
		return lolPostMapper.selectLolPostByPostId(postId);
	}

	public List<LolPost> getAllLolPostByCategory(String category) {
		return lolPostMapper.selectAllLolPostByCategory(category);
	}
	
	public List<LolPost> getLolPostsByKeyword(String keyword) {
		return lolPostMapper.selectLolPostsByKeyWord(keyword);
	}
	
	// input: 글번호, userId output: Post
	public List<LolPost> getAllLolPost() {
		return lolPostMapper.selectAllLolPost();
	}
	
	// 추천 개수
	public int getRecommandCount(int postId) {
		return recommandBO.getRecommandCountByPostId(postId);
	}
	
	// 비추천 개수
	public int getNoRecommandCount(int postId) {
		return noRecommandBO.getNoRecommandCountByPostId(postId);
	}
	


	// input:파라미터들 output:X
	public void updateLolPostById(int userId, String userLoginId, String category, int postId, String subject,
			String content, MultipartFile file) {

		// 기존글을 가져온다.(1. 이미지 교체시 삭제하기 위해 2. 업데이트 대상이 있는지 확인)
		 LolPost lolPost = lolPostMapper.selectLolPostByPostIdUserId(postId, userId);
		if (lolPost == null) {
			log.info("[글 수정] post is null. postId:{}, userId:{}", postId, userId);
			return;
		}

		// 파일이 있다면
		// 1) 새 이미지를 업로드 한다.
		// 2) 1번 단계가 성공하면 기존 이미지 제거(기존 이미지가 있다면)
		String imagePath = null;
		if (file != null) {
			// 업로드
			imagePath = fileManagerService.saveFile(userLoginId, file);

			// 업로드 성공 시 기존 이미지가 있으면 제거
			if (imagePath != null && lolPost.getImagePath() != null) {
				// 업로드 성공하고 기존 이미지 있으면 서버의 파일 제거
				fileManagerService.deleteFile(lolPost.getImagePath());
			}
		}

		// db 업데이트
		lolPostMapper.updateLolPostByPostId(postId, subject, content, category, imagePath);
	}

	// input:postId, userId output: X
	public void deletePostByPostIdUserId(int postId, int userId) {
		// 기존글이 있는지 확인
		LolPost lolPost = lolPostMapper.selectLolPostByPostIdUserId(postId, userId);
		if (lolPost == null) {
			log.info("[글 삭제] post is null. postId:{}, userId:{}", postId, userId);
			return;
		}

		// DB 삭제
		int deleteRowCount = lolPostMapper.deleteLolPostByPostId(postId);

		// 이미지가 존재하면 삭제 && DB 삭제도 성공
		if (deleteRowCount > 0 && lolPost.getImagePath() != null) {
			fileManagerService.deleteFile(lolPost.getImagePath());
		}
	}
}