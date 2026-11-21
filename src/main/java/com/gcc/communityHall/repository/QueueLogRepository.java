package com.gcc.communityHall.repository;

import com.gcc.communityHall.entity.QueueLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueLogRepository extends JpaRepository<QueueLog,Integer> {
}
