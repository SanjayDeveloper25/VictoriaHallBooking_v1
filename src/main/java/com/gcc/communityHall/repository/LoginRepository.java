package com.gcc.communityHall.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {

	@Query(value = "SELECT * FROM login where mobile_number=:mobileNo and password=:pin",nativeQuery = true)
    Login findByMobileNoAndPin(long mobileNo, String pin);

	@Query(value = "SELECT a FROM Login a WHERE a.mobileNumber = ?1")
	Login checkUserbyMobile(long mobileNo);

	@Query(value = "SELECT * FROM login where name=:username and password=:pass LIMIT 1",nativeQuery = true)
	Login findByUsernameAndPass(String username, String pass);
}
