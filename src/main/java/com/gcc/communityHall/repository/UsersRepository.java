package com.gcc.communityHall.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gcc.communityHall.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {

    List<Users> findByUserstatus(Users.userStatus userStatus);
    List<Users> findByUsertype(Users.userType userType);

    @Query(value = "SELECT * FROM users WHERE user_status in ('PENDING') and usertype in ('FAMILYPENSIONER','PENSIONER') ",nativeQuery = true)
	List<Users> getPensionerPendingList();
	
    @Query(value = "SELECT * FROM users WHERE user_status in ('APPROVED') and usertype in ('FAMILYPENSIONER','PENSIONER') ",nativeQuery = true)
	List<Users> getPensionerApprovedList();
    
    @Query(value = "SELECT * FROM users WHERE user_status in ('REJECTED') and usertype in ('FAMILYPENSIONER','PENSIONER') ",nativeQuery = true)
	List<Users> getPensionerRejectedList();
    
    //COUNSELLOR
    @Query(value = "SELECT * FROM users WHERE user_status in ('PENDING') and usertype in ('COUNCILLOR') ",nativeQuery = true)
	List<Users> getCounsellorPendingList();
    
    @Query(value = "SELECT * FROM users WHERE user_status in ('APPROVED') and usertype in ('COUNCILLOR') ",nativeQuery = true)
	List<Users> getCounsellorApprovedList();
    
    @Query(value = "SELECT * FROM users WHERE user_status in ('REJECTED') and usertype in ('COUNCILLOR') ",nativeQuery = true)
	List<Users> getCounsellorRejectedList();
    
    //GCCSTAFF
    @Query(value = "SELECT * FROM users WHERE user_status in ('PENDING') and usertype in ('GCC_EMPLOYEE') ",nativeQuery = true)
	List<Users> getGccStaffpendingList();
    
    @Query(value = "SELECT * FROM users WHERE user_status in ('APPROVED') and usertype in ('GCC_EMPLOYEE') ",nativeQuery = true)
	List<Users> getGccStaffApprovedList();
    
    @Query(value = "SELECT * FROM users WHERE user_status in ('REJECTED') and usertype in ('GCC_EMPLOYEE') ",nativeQuery = true)
	List<Users> getGccStaffRejectedList();
    
    @Query(value = "SELECT * FROM users WHERE user_status in ('PENDING') ",nativeQuery = true)
	List<Users> getPendingList();

    @Transactional
    @Modifying
    @Query("update Users u set u.userstatus = 'APPROVED' where u.userId = ?1 and u.userstatus = 'PENDING'")
    void approveUser(int userId);

    @Query(value = "SELECT * FROM users WHERE user_status in ('APPROVED') ",nativeQuery = true)
	List<Users> getUserApproved();

    @Query(value = "SELECT * FROM users WHERE user_status in ('REJECTED') ",nativeQuery = true)
	List<Users> getUserReject();
	Users findByMobileNo(long mobileNo);
	
	//yoga
	@Query(value = "SELECT * FROM users WHERE DATE(cdate) >= :fromDate AND DATE(cdate) <= :toDate", nativeQuery = true)
	 List<Users> getAllUsers(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
	 
	 @Query(value = "SELECT * FROM users WHERE DATE(cdate) >= :fromDate AND DATE(cdate) <= :toDate " +
            "AND (:userStatus IS NULL OR user_status = :userStatus) " +
            "AND (:userType IS NULL OR usertype = :userType)", nativeQuery = true)
	 List<Users> getAllUsersWithFilters(@Param("fromDate") String fromDate, 
                                @Param("toDate") String toDate,
                                @Param("userStatus") String userStatus,
                                @Param("userType") String userType);
	 
	 @Query(value = "SELECT * FROM users WHERE DATE(cdate) >= :fromDate AND DATE(cdate) <= :toDate " +
            "AND (:userStatus IS NULL OR user_status = :userStatus) ", nativeQuery = true)
	 List<Users> getAllUsersWithStatusFilters(@Param("fromDate") String fromDate, 
                                @Param("toDate") String toDate,
                                @Param("userStatus") String userStatus);
	 
	 
	 @Query(value = "SELECT * FROM users WHERE DATE(cdate) >= :fromDate AND DATE(cdate) <= :toDate " +
            "AND (:userType IS NULL OR usertype = :userType)", nativeQuery = true)
	 List<Users> getAllUsersWithTypeFilters(@Param("fromDate") String fromDate, 
                                @Param("toDate") String toDate,
                                @Param("userType") String userType);
	@Query(value = "SELECT SUM(CASE WHEN user_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_users_count,\r\n"
    		+ "SUM(CASE WHEN user_status = 'PENDING' THEN 1 ELSE 0 END) AS pending_users_count,\r\n"
    		+ "SUM(CASE WHEN user_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_users_count\r\n"
    		+ "FROM users;",nativeQuery = true)
    List<Map<String, Object>> toatlUserCount();
	
	@Query(value="SELECT  SUM(CASE WHEN user_status = 'PENDING' THEN 1 ELSE 0 END) AS pending_count, SUM(CASE WHEN user_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count, SUM(CASE WHEN bd.user_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count FROM users bd where  bd.usertype IN ('PENSIONER', 'FAMILYPENSIONER')",nativeQuery = true)
	List<Map<String, Object>> totalPensioneruserCount();
	
	@Query(value="SELECT  SUM(CASE WHEN user_status = 'PENDING' THEN 1 ELSE 0 END) AS pending_count, SUM(CASE WHEN user_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count, SUM(CASE WHEN bd.user_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count FROM users bd where  bd.usertype IN ('COUNCILLOR')",nativeQuery = true)
	List<Map<String, Object>> totalCounselloruserCount();
	
	@Query(value="SELECT  SUM(CASE WHEN user_status = 'PENDING' THEN 1 ELSE 0 END) AS pending_count, SUM(CASE WHEN user_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count, SUM(CASE WHEN bd.user_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count FROM users bd where  bd.usertype IN ('GCC_EMPLOYEE')",nativeQuery = true)
	List<Map<String, Object>> totalEmployeeuserCount();
	
	  @Query(value="SELECT  CONCAT(YEAR(bd.cdate), '-', LPAD(MONTH(bd.cdate), 2, '0')) AS months, "
		  		+ "SUM(CASE WHEN user_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count, "
		  		+ "SUM(CASE WHEN bd.user_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count,"
		  		+ " SUM(CASE WHEN bd.user_status = 'PENDING' THEN 1 ELSE 0 END) AS pending_count "
		  		+ "FROM users bd where bd.usertype IN('PENSIONER','FAMILYPENSIONER') and bd.cdate >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) "
		  		+ "AND bd.cdate <= DATE_ADD(CURDATE(), INTERVAL 6 MONTH) and bd.usertype IN('PENSIONER','FAMILYPENSIONER') group by months order by months ASC",nativeQuery=true)
		  List<Map<String, Object>> getpensionermonthCount();
	  
	  @Query(value="SELECT  CONCAT(YEAR(bd.cdate), '-', LPAD(MONTH(bd.cdate), 2, '0')) AS months, "
		  		+ "SUM(CASE WHEN user_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count, "
		  		+ "SUM(CASE WHEN bd.user_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count,"
		  		+ " SUM(CASE WHEN bd.user_status = 'PENDING' THEN 1 ELSE 0 END) AS pending_count "
		  		+ "FROM users bd where bd.usertype IN('PENSIONER','FAMILYPENSIONER') and bd.cdate >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) "
		  		+ "AND bd.cdate <= DATE_ADD(CURDATE(), INTERVAL 6 MONTH) and bd.usertype IN('COUNCILLOR') group by months order by months ASC",nativeQuery=true)
		  List<Map<String, Object>> getcounsellormonthCount();
	  
	  @Query(value="SELECT  CONCAT(YEAR(bd.cdate), '-', LPAD(MONTH(bd.cdate), 2, '0')) AS months, "
		  		+ "SUM(CASE WHEN user_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count, "
		  		+ "SUM(CASE WHEN bd.user_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count,"
		  		+ " SUM(CASE WHEN bd.user_status = 'PENDING' THEN 1 ELSE 0 END) AS pending_count "
		  		+ "FROM users bd where bd.usertype IN('PENSIONER','FAMILYPENSIONER') and bd.cdate >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) "
		  		+ "AND bd.cdate <= DATE_ADD(CURDATE(), INTERVAL 6 MONTH) and bd.usertype IN('GCC_EMPLOYEE') group by months order by months ASC",nativeQuery=true)
		  List<Map<String, Object>> getemployeemonthCount();
	  
	  @Query(value = "SELECT * FROM users WHERE name= :username",nativeQuery = true)
	  Users findByUsername(String username);
	  
	  @Query(value = "SELECT * FROM users WHERE DATE(cdate) >= :fromDate AND DATE(cdate) <= :toDate AND is_active = 0", nativeQuery = true)
	  List<Users> getDeactivatedList(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
	  
	    boolean existsByGovernmentId(@Param("governmentId") String governmentId);
	    @Query(value = "SELECT tr.booking_id, u.name, u.mobile_no, " +
                "CONCAT(u.doorno, ', ', u.street, ', ', u.area, ', ', u.location, ', ', u.pin_code) AS full_address, " +
                "tr.status_code, tr.date, tr.amount, tr.txansaction_id, bd.gst, bd.deposit_amount, bd.hall_amount " +
                "FROM transaction_response tr " +
                "JOIN booking_details bd ON tr.booking_id = bd.booking_id " +
                "JOIN users u ON bd.users_user_id = u.user_id " +
                "WHERE bd.booking_date >= :fromDate AND bd.booking_date < :toDate AND tr.status_code=0300", 
        nativeQuery = true)
 List<Object[]> findUserDetailsByBookingDate(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
 
 @Query(value = "SELECT p.booking_details_booking_id, u.name, u.mobile_no, p.transaction_id, "
         + "p.amount_received, p.cdate, bd.booking_status, bd.gst, bd.deposit_amount, "
         + "bd.hall_amount, st.damage_amount, st.eb_amount, st.refunded_amount, "
         + "bd.hall_details_hall_id, r.transaction_id AS refund_transaction_id, "
         + "r.amount_refunded, r.cdate AS refund_date "
         + "FROM payments p "
         + "JOIN booking_details bd ON p.booking_details_booking_id = bd.booking_id "
         + "JOIN users u ON bd.users_user_id = u.user_id "
         + "LEFT JOIN settlement st ON bd.booking_id = st.booking_details_booking_id "
         + "LEFT JOIN refund_transaction r ON r.booking_details_booking_id = bd.booking_id "
         + "WHERE bd.booking_date >= :startDate AND bd.booking_date < :endDate AND p.payment_status = 'Success'", nativeQuery = true)
 List<Object[]> findPaymentsWithRefunds(@Param("startDate") String startDate, @Param("endDate") String endDate);

 @Query(value = "SELECT p.booking_details_booking_id, u.name, u.mobile_no, p.transaction_id, "
         + "p.amount_received, p.cdate, bd.gst, bd.deposit_amount, bd.hall_amount, "
         + "st.damage_amount, st.eb_amount, st.refunded_amount, bd.hall_details_hall_id "
         + "FROM payments p "
         + "JOIN booking_details bd ON p.booking_details_booking_id = bd.booking_id "
         + "JOIN users u ON bd.users_user_id = u.user_id "
         + "LEFT JOIN settlement st ON bd.booking_id = st.booking_details_booking_id "
         + "WHERE bd.booking_date >= :startDate AND bd.booking_date < :endDate AND p.payment_status = 'Success'", nativeQuery = true)
 List<Object[]> findPaymentsWithoutRefunds(@Param("startDate") String startDate, @Param("endDate") String endDate);

 @Query(value = "SELECT p.booking_details_booking_id, u.name, u.mobile_no, bd.gst, "
         + "bd.deposit_amount, bd.hall_amount, bd.hall_details_hall_id, r.transaction_id, "
         + "r.amount_refunded, r.cdate "
         + "FROM payments p "
         + "JOIN booking_details bd ON p.booking_details_booking_id = bd.booking_id "
         + "JOIN users u ON bd.users_user_id = u.user_id "
         + "LEFT JOIN settlement st ON bd.booking_id = st.booking_details_booking_id "
         + "LEFT JOIN refund_transaction r ON r.booking_details_booking_id = bd.booking_id "
         + "WHERE bd.booking_date >= :startDate AND bd.booking_date < :endDate AND p.payment_status = 'Success'", nativeQuery = true)
 List<Object[]> findPaymentsWithRefundsOnly(@Param("startDate") String startDate, @Param("endDate") String endDate);
 boolean existsByMobileNo(long mobileNo);

}