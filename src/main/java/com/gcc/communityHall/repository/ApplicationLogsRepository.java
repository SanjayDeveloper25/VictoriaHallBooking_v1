package com.gcc.communityHall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.ApplicationLogs;

@Repository
public interface ApplicationLogsRepository extends JpaRepository<ApplicationLogs,Long> {
}
