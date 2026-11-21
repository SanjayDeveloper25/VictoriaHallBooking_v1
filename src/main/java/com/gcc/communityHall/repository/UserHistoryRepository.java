package com.gcc.communityHall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.UserHistory;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory,Integer> {
}
