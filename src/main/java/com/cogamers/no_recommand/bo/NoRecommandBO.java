package com.cogamers.no_recommand.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cogamers.no_recommand.mapper.NoRecommandMapper;
import com.cogamers.recommand.mapper.RecommandMapper;

@Service
public class NoRecommandBO {

	@Autowired
	private NoRecommandMapper noRecommandMapper;
	
	
	// input:postId, userId     output: X
		public void giveNoRecommand(int postId, int userId) {
				noRecommandMapper.insertNoRecommand(postId, userId);
		}
		
		public int getNoRecommandCountByPostId(int postId) {
			return noRecommandMapper.selectNoRecommandCountByPostId(postId);
		}
}
