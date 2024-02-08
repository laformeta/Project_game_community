package com.cogamers.user.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cogamers.user.entity.UserEntity;
import com.cogamers.user.repository.UserRepository;

@Service
public class UserBO {

	@Autowired
	private UserRepository userRepository;
	
	public UserEntity getUserEntityByLoginId(String loginId ) {
		return userRepository.findByLoginId(loginId);
	}
}
