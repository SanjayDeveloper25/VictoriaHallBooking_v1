package com.gcc.communityHall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gcc.communityHall.entity.GccBlockingHistory;

@Repository
public interface GccBlockingHistoryRepository extends JpaRepository<GccBlockingHistory,Integer>{

}
