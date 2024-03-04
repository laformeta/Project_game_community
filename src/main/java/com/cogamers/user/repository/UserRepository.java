package com.cogamers.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cogamers.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer>{

	public UserEntity findByLoginId(String loginId);
	
	public UserEntity findByLoginIdAndPassword(String loginId, String password);
	public UserEntity findByName(String name);
	public UserEntity findByLoginIdAndNameAndOauth(String loginId, String name, String oauth);
}
