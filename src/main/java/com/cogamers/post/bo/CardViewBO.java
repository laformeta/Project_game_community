package com.cogamers.post.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cogamers.comment.bo.CommentBO;
import com.cogamers.comment.domain.CommentView;
import com.cogamers.no_recommand.bo.NoRecommandBO;
import com.cogamers.post.domain.CardView;
import com.cogamers.post.domain.LolPost;
import com.cogamers.recommand.bo.RecommandBO;
import com.cogamers.user.bo.UserBO;
import com.cogamers.user.entity.UserEntity;

@Service
public class CardViewBO {

	@Autowired
	private LolPostBO lolPostBO;
	
	@Autowired
	private UserBO userBO;
	
	@Autowired
	private CommentBO commentBO;
	
	@Autowired
	private RecommandBO recommandBO;
	
	@Autowired
	private NoRecommandBO noRecommandBO;
	
	// input:userId(비로그인:null , 로그인:userId)       output: List<CardView>
		public List<CardView> generateCardViewList(Integer userId){
			List<CardView> cardViewList = new ArrayList<>();
			
			// 글 목록을 가져온다. List<LolPost> PostEntity>>LolPost
			List<LolPost> postList = lolPostBO.getAllLolPost();
			
			// 글 목록 반복문 순회
			// post => cardView     => cardViewList에 넣기
			
			for (LolPost post : postList) {
				// post 하나에 대응되는 하나의 카드를 만든다.
				CardView cardView = new CardView();
				
				// 글 1개
				cardView.setPost(post);	
				
				// 글쓴이 정보
				UserEntity user = userBO.getUserEntityById(post.getUserId());
				cardView.setUser(user);
				
				// 댓글들
				List<CommentView> commentList = commentBO.generateCommentViewListByPostId(post.getId());
				cardView.setCommentList(commentList);
				
				// 추천 개수
				int recommandCount = recommandBO.getRecommandCountByPostId(post.getId());
				int noRecommandCount = noRecommandBO.getNoRecommandCountByPostId(post.getId());
				
				// ★★★★★ 마지막에 cardView를 list에 넣는다.
				cardViewList.add(cardView);
			}
			
			return cardViewList;
		}
}
