package com.gcc.communityHall.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.CounterRefundList;

@Repository
public interface CounterRefundListRepository extends JpaRepository<CounterRefundList, Integer> {

	@Query(value = "SELECT cr.* FROM counter_refund_list cr JOIN booking_details b ON cr.counterdetails_counter_id = b.counterdetails_counter_id JOIN hall_details h ON b.hall_details_hall_id = h.hall_id WHERE cr.initialisation_status='INITIATED' and h.zone =:zone",nativeQuery = true)
	List<CounterRefundList> findCounterRefundList(String zone);
}