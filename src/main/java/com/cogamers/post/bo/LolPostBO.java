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

	// 페이징 필드
	private static final int POST_MAX_SIZE = 3;

	// input: userId(로그인 된 사람) output:List<Post>
	public List<LolPost> getLolPostListByUserId(int userId, Integer prevId, Integer nextId) {
		// 게시글 번호 10 9 8 | 7 6 5 | 4 3 2 | 1
		// 만약 4 3 2 페이지에 있을 때
		// 1) 다음: 2보다 작은 3개 DESC
		// 2) 이전: 4보다 큰 3개 ASC(5 6 7) => List reverse(7 6 5)
		// 3) 페이징 정보 없음: 최신순 3개 DESC

		Integer standardId = null; // 기준이 되는 postId
		String direction = null; // 방향
		if (prevId != null) { // 2) 이전
			standardId = prevId;
			direction = "prev";

			List<LolPost> postList = lolPostMapper.selectLolPostListByUserId(userId, standardId, direction,
					POST_MAX_SIZE);

			// reverse list 5 6 7 => 7 6 5
			Collections.reverse(postList); // 뒤집고 저장

			return postList;
		} else if (nextId != null) { // 1) 다음
			standardId = nextId;
			direction = "next";
		}

		// 3) 페이징 정보 없음, 1) 다음
		return lolPostMapper.selectLolPostListByUserId(userId, standardId, direction, POST_MAX_SIZE);
	}

	// 이전 페이지의 마지막인가?
	public boolean isPrevLastPageByUserId(int userId, int prevId) {
		int postId = lolPostMapper.selectLolPostIdByUserIdSort(userId, "DESC");
		return postId == prevId; // 같으면 마지막이다.
	}

	// 다음 페이지의 마지막인가?
	public boolean isNextLastPageByUserId(int userId, int nextId) {
		int postId = lolPostMapper.selectLolPostIdByUserIdSort(userId, "ASC");
		return postId == nextId; // 같으면 마지막이다.
	}

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