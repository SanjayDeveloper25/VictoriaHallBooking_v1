package com.gcc.communityHall.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.HallDetails;
import com.gcc.communityHall.entity.HallImages;

@Repository
public interface HallImagesRepository extends JpaRepository<HallImages,Integer> {
	List<HallImages> findByHallDetails(HallDetails hallDetails);

	List<HallImages> findByHallDetails_HallId(Long hallId);
}
