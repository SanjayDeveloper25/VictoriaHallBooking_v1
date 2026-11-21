package com.gcc.communityHall.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.HallDetails;
import com.gcc.communityHall.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>{
	
	@Query(value = "SELECT * FROM notification WHERE is_active = 1 AND is_delete = 0", nativeQuery = true)
	List<Notification> findActivenotifications();
}
