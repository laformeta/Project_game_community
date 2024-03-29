package com.cogamers.user.bo;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cogamers.user.entity.UserEntity;
import com.cogamers.user.repository.UserRepository;

@Service
public class UserBO {

	@Autowired
	private UserRepository userRepository;
	
	public UserEntity getUserEntityByLoginId(String loginId ) {
		UserEntity userEntity = userRepository.findByLoginId(loginId);
		return userEntity;
	}
	
	// input:4개 파라미터 output:id(pk)
		public Integer addUser(String loginId, String password, String name, String nickname, String email, String ouath) {
			// UserEntity = save(UserEntity);
			UserEntity userEntity = userRepository
					.save(UserEntity.builder()
							.loginId(loginId)
							.password(password)
							.name(name)
							.nickname(nickname)
							.email(email)
							.oauth(ouath)
							.build());

			return userEntity == null ? null : userEntity.getId();
		}
		
		// input: loginId, password
		// output: UserEntity
		public UserEntity getUserEntityByLoginIdPassword(String loginId, String password) {
			return userRepository.findByLoginIdAndPassword(loginId, password);
		}

		// input:userId output:UserEntity
		public UserEntity getUserEntityById(int id) {
			return userRepository.findById(id).orElse(null);
		}

		public UserEntity getUserEntityByLoginIdNameOauth(String loginId, String name, String oauth) {
			return userRepository.findByLoginIdAndNameAndOauth(loginId, name, oauth);
		}
		
}
 