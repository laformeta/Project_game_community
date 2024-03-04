package com.cogamers.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cogamers.post.entity.LolPostEntity;

public interface LolPostRepository extends JpaRepository<LolPostEntity, Integer> {

	public List<LolPostEntity> findAllByOrderByIdDesc();

}