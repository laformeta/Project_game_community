package com.cogamers.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class KakaoUserEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	private String kakaoUserId;
	
	private String name;
	
	private String email;
	
	@Builder
    public KakaoUserEntity(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
