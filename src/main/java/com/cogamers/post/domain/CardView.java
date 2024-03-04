package com.cogamers.post.domain;

//View용 객체
//글 1개와 매핑됨
@ToString
@Data
public class CardView {
	// 글 1개
	private LolPostEntity post;
	
	// 글쓴이 정보
	private UserEntity user;
	
	// 댓글들
	private List<CommentView> commentList;
	
	// 좋아요 개수
	private int likeCount;
	
	// 로그인 된 사람이 좋아요를 누른 여부
	private boolean filledLike;
}