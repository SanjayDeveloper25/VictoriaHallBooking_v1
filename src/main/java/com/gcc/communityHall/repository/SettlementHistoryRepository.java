package com.gcc.communityHall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gcc.communityHall.entity.SettlementHistory;

@Repository
public interface SettlementHistoryRepository extends JpaRepository<SettlementHistory, Integer> {
}
