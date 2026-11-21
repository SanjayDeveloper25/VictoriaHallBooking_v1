package com.gcc.communityHall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.BookingHistory;
import com.gcc.communityHall.entity.Counsellordetails;

@Repository
public interface CounsellorDetailsRepository extends JpaRepository<Counsellordetails,Long>{

    @Query(value="SELECT * FROM counsellor_details WHERE mobile1 = :mobileno OR mobile2 = :mobileno", nativeQuery = true)
    Counsellordetails findByMobile(@Param("mobileno") long mobileno);
    
    @Query(value="SELECT * FROM counsellor_details WHERE mobile1 = :mobileno AND mobile2 = :mobileno", nativeQuery = true)
    Counsellordetails findByMobileno(@Param("mobileno") long mobileno);
}
