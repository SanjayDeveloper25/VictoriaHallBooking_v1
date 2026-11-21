package com.gcc.communityHall.repository;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.HallDetails;

@Repository
public interface HallDetailsRepository extends JpaRepository<HallDetails,Integer> {
	@Query(value= "select id from halldetails", nativeQuery=true)
	List<String> getHallAvailablity();
	@Query(value = "SELECT * FROM hall_details where hall_id NOT IN (:groupByHallIdToRemov) ",nativeQuery = true)
    List<HallDetails> removeHallById(Set<Integer> groupByHallIdToRemov);
	@Query("SELECT h FROM HallDetails h WHERE h.division = :division")
	List<HallDetails> findByDivision(@Param("division") String division);
	@Query("SELECT h FROM HallDetails h WHERE h.zone = :zone")
	List<HallDetails> findByZone(@Param("zone") String zone);
	@Query("SELECT h FROM HallDetails h WHERE h.id = :hallId")
    HallDetails findHallDetailsById(@Param("hallId") int hallId);
	
	@Query(value = "SELECT zone,division FROM hall_details ",nativeQuery = true )
	List<Map<String, Object>> getZoneAndDivision();
	
	@Query(value = "SELECT DISTINCT division FROM hall_details  where zone=:zone ",nativeQuery = true )
	List<Map<String, Object>> getDivision(String zone);
	
	@Query(value = "SELECT hall_id, hall_name FROM hall_details", nativeQuery = true)
	List<Map<String, Object>> getAllHalls();

	@Query(value = "SELECT hall_id, hall_name FROM hall_details WHERE zone = :zone", nativeQuery = true)
	List<Map<String, Object>> getHallsByZone(@Param("zone") String zone);
	
	@Query(value = "SELECT hall_id AS hallId, hall_name AS hallName FROM hall_details WHERE zone IN (:zones)", nativeQuery = true)
	List<Map<String, Object>> getHallsByZones(@Param("zones") List<String> zones);

	@Query(value = "SELECT * FROM hall_details WHERE is_active = 1 order by hall_name asc", nativeQuery = true)
	List<HallDetails> findActiveHalls();


	//admin dashboard
	
	@Query(nativeQuery = true, value ="SELECT SUM(CASE WHEN b.booking_status = 'APPROVED' THEN 1 ELSE 0 END) AS approvedCount,\r\n"
			+ "SUM(CASE WHEN b.booking_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejectedCount,\r\n"
			+ "SUM(CASE WHEN b.booking_status = 'COMPLETED' THEN 1 ELSE 0 END) AS completedCount, \r\n"
			+ "SUM(CASE WHEN b.booking_status = 'CANCELED' THEN 1 ELSE 0 END) AS canceledCount, \r\n"
			+ "SUM(CASE WHEN b.booking_status = 'PAYMENT_FAILURE' THEN 1 ELSE 0 END) AS paymentFailureCount, \r\n"
			+ "SUM(CASE WHEN b.booking_status = 'GCC_BLOCKED' THEN 1 ELSE 0 END) AS gccBlockedCount,\r\n"
			+ "COUNT(b.booking_status) AS totalBooking, SUM( CASE WHEN b.amount_received IS NULL THEN 0 ELSE b.amount_received END ) AS amountReceived \r\n"
			+ "FROM hall_details h LEFT JOIN booking_details b ON b.hall_details_hall_id = h.hall_id WHERE h.zone = :zone AND b.booking_status!='PENDING'")
	Map<String, Object> getTotalBookingsAndAmountReceivedByZone(@Param("zone") String zone);

	@Query(nativeQuery = true, value ="SELECT h.hall_id AS hall_id, h.zone AS zonename,h.division AS division, h.hall_name AS hallName, " +
	           "SUM(CASE WHEN b.booking_status = 'APPROVED' THEN 1 ELSE 0 END) AS approvedCount, " +
	           "SUM(CASE WHEN b.booking_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejectedCount, " +
	           "SUM(CASE WHEN b.booking_status = 'COMPLETED' THEN 1 ELSE 0 END) AS completedCount, " +
	           "SUM(CASE WHEN b.booking_status = 'CANCELED' THEN 1 ELSE 0 END) AS canceledCount, " +
	           "SUM(CASE WHEN b.booking_status = 'PAYMENT_FAILURE' THEN 1 ELSE 0 END) AS paymentFailureCount, " +
	           "SUM(CASE WHEN b.booking_status = 'GCC_BLOCKED' THEN 1 ELSE 0 END) AS gccBlockedCount, " +
	           "COUNT(b.booking_status) AS totalBookings, " +
	           "SUM(CASE WHEN b.amount_received IS NULL THEN 0 ELSE b.amount_received END) AS totalAmount " +
	           "FROM  hall_details h LEFT JOIN booking_details b ON b.hall_details_hall_id = h.hall_id  WHERE h.zone = :zone AND b.booking_status!='PENDING' "+
	           "GROUP BY h.hall_id,h.zone,h.division,h.hall_name")
	List<Map<String, Object>> getHallDetailsByZone(@Param("zone") String zone);

@Query(value = "SELECT zone,division,hall_id,hall_name FROM hall_details ",nativeQuery = true )
	List<Map<String, Object>> getZonesAndDivisions();


//officer dashboard

@Query(nativeQuery = true, value ="SELECT SUM(CASE WHEN b.booking_status = 'APPROVED' THEN 1 ELSE 0 END) AS approvedCount,\r\n"
		+ "SUM(CASE WHEN b.booking_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejectedCount,\r\n"
		+ "SUM(CASE WHEN b.booking_status = 'COMPLETED' THEN 1 ELSE 0 END) AS completedCount, \r\n"
		+ "SUM(CASE WHEN b.booking_status = 'CANCELED' THEN 1 ELSE 0 END) AS canceledCount, \r\n"
		+ "SUM(CASE WHEN b.booking_status = 'PAYMENT_FAILURE' THEN 1 ELSE 0 END) AS paymentFailureCount, \r\n"
		+ "SUM(CASE WHEN b.booking_status = 'GCC_BLOCKED' THEN 1 ELSE 0 END) AS gccBlockedCount,\r\n"
		+ "COUNT(b.booking_status) AS totalBooking, SUM( CASE WHEN b.amount_received IS NULL THEN 0 ELSE b.amount_received END ) AS amountReceived \r\n"
		+ "FROM hall_details h LEFT JOIN booking_details b ON b.hall_details_hall_id = h.hall_id WHERE h.zone = :zone AND b.booking_status!='PENDING'")
    Map<String, Object> getTotalCountsForOfficer(@Param("zone") String zone);

@Query(nativeQuery = true, value ="SELECT h.hall_id AS hall_id, h.zone AS zonename,h.division AS division, h.hall_name AS hallName, " +
        "SUM(CASE WHEN b.booking_status = 'APPROVED' THEN 1 ELSE 0 END) AS approvedCount, " +
        "SUM(CASE WHEN b.booking_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejectedCount, " +
        "SUM(CASE WHEN b.booking_status = 'COMPLETED' THEN 1 ELSE 0 END) AS completedCount, " +
        "SUM(CASE WHEN b.booking_status = 'CANCELED' THEN 1 ELSE 0 END) AS canceledCount, " +
        "SUM(CASE WHEN b.booking_status = 'PAYMENT_FAILURE' THEN 1 ELSE 0 END) AS paymentFailureCount, " +
        "SUM(CASE WHEN b.booking_status = 'GCC_BLOCKED' THEN 1 ELSE 0 END) AS gccBlockedCount, " +
        "COUNT(b.booking_status) AS totalBookings, " +
        "SUM(CASE WHEN b.amount_received IS NULL THEN 0 ELSE b.amount_received END) AS totalAmount " +
        "FROM  hall_details h LEFT JOIN booking_details b ON b.hall_details_hall_id = h.hall_id  WHERE h.zone = :zone AND b.booking_status!='PENDING' "+
        "GROUP BY h.hall_id,h.zone,h.division,h.hall_name")
    List<Map<String, Object>> getHallCountsForOfficer(@Param("zone") String zone);

	@Query(value = "SELECT hall_name FROM hall_details WHERE hall_id = :hall_id", nativeQuery = true)
	String findHallNameById(@Param("hall_id") int hallId);
	
    @Query("SELECT DISTINCT h.division FROM HallDetails h WHERE h.zone = :zone")
    List<String> findDistinctDivisionsByZone(@Param("zone") String zone);

    // Query to get hall names by division
    @Query("SELECT h.hallName FROM HallDetails h WHERE h.division = :division")
    List<String> findHallsByDivision(@Param("division") String division);
}
