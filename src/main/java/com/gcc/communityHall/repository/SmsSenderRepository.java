package com.gcc.communityHall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.SmsSender;

@Repository
public interface SmsSenderRepository extends JpaRepository<SmsSender,Integer> {
}
