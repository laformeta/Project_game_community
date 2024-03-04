package com.cogamers.post.domain;

import java.util.List;

import com.cogamers.comment.domain.CommentView;
import com.cogamers.user.entity.UserEntity;

import lombok.Data;
import lombok.ToString;


//View용 객체
//글 1개와 매핑됨
@ToString
@Data
public class CardView {
		// 글 1개
		private LolPost post;
		
		// 글쓴이 정보
		private UserEntity user;
		
		// 댓글들
		private List<CommentView> commentList;
		
		// 좋아요 개수
		private int recommandCount;
		private int unRecommandCount;
		
}
