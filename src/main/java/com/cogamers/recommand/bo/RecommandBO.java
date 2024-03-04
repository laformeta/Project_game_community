package com.cogamers.recommand.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cogamers.recommand.mapper.RecommandMapper;

@Service
public class RecommandBO {

	@Autowired
	private RecommandMapper recommandMapper;
	
	
	// input:postId, userId     output: X
		public void giveRecommand(int postId, int userId) {
				recommandMapper.insertRecommand(postId, userId);
		}
		
		public int getRecommandCountByPostId(int postId) {
			return recommandMapper.selectRecommandCountByPostId(postId);
		}
		
		public void deleteRecommandByPostId(int postId) {
			recommandMapper.deleteRecommandByPostId(postId);
		}
}
