package com.gcc.communityHall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.EbDetails;

@Repository
public interface EbDetailsRepository extends JpaRepository<EbDetails,Integer> {
}
