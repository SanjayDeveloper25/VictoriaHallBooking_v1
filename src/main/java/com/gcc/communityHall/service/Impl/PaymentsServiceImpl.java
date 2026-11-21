package com.gcc.communityHall.service.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.gcc.communityHall.dto.BookingDetailsDTO;
import com.gcc.communityHall.dto.PaymentsDTO;
import com.gcc.communityHall.dto.UsersDTO;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.Payments;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.repository.PaymentsRepository;
import com.gcc.communityHall.service.PaymentsService;
import org.springframework.transaction.annotation.Transactional;

@Service 
public class PaymentsServiceImpl implements PaymentsService{
	
	 private PaymentsRepository paymentsRepository;

	 @Autowired
	 private JdbcTemplate jdbcTemplate;

	@Autowired
	    public PaymentsServiceImpl(PaymentsRepository paymentsRepository) {
	        this.paymentsRepository = paymentsRepository;
	    }

	    @Override
	    public List<Payments> getPaymentsByZone(String zone) {
	        return paymentsRepository.getPaymentListByzone(zone);
	    }

	    @Override
	    public List<PaymentsDTO> getAllPaymentDetails() {
	       // List<Payments> payments = paymentsRepository.findAllWithBookingDetailsAndUsers();
	        return null;
	    }

	    private PaymentsDTO convertToDto(Payments payment) {
	        PaymentsDTO paymentsDTO = new PaymentsDTO();
	        paymentsDTO.setPaymentId(payment.getPaymentId());
	        paymentsDTO.setAmountReceived(payment.getAmountReceived());
	        paymentsDTO.setTransactionId(payment.getTransactionId());
	        paymentsDTO.setPaymentType(payment.getPaymentType());
	        paymentsDTO.setPaymentStatus(payment.getPaymentStatus());
	        paymentsDTO.setCdate(payment.getCdate());

	        BookingDetails bookingDetails = payment.getBookingDetails();
	        BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO();
	        bookingDetailsDTO.setBookingId(bookingDetails.getBookingId());
	        bookingDetailsDTO.setNoOfDays(bookingDetails.getNoOfDays());
	        bookingDetailsDTO.setStartDate(bookingDetails.getStartDate());
	        bookingDetailsDTO.setEndDate(bookingDetails.getEndDate());
	        bookingDetailsDTO.setStartingSlot(bookingDetails.getStartingSlot());
	        bookingDetailsDTO.setEndingSlot(bookingDetails.getEndingSlot());
	        bookingDetailsDTO.setBookingDate(bookingDetails.getBookingDate());
	        bookingDetailsDTO.setBookingStatus(bookingDetails.getBookingStatus().name());
	        bookingDetailsDTO.setDepositAmount(bookingDetails.getDepositAmount());
	        bookingDetailsDTO.setHallAmount(bookingDetails.getHallAmount());
	        bookingDetailsDTO.setAmountReceived(bookingDetails.getAmountReceived());
	        bookingDetailsDTO.setGst(bookingDetails.getGst());
	        bookingDetailsDTO.setApprovedBy(bookingDetails.getApprovedBy());
	        bookingDetailsDTO.setRefundStatus(bookingDetails.getRefundStatus().name());
	        bookingDetailsDTO.setUpdatedDate(bookingDetails.getUpdatedDate());
	        bookingDetailsDTO.setReason(bookingDetails.getReason());

	        Users user = bookingDetails.getUsers();
	        UsersDTO usersDTO = new UsersDTO();
	        usersDTO.setUserId(user.getUserId());
	        usersDTO.setUserStatus(user.getUserstatus().name());
	        usersDTO.setUserType(user.getUsertype().name());
	        usersDTO.setName(user.getName());
	        usersDTO.setMobileNo(user.getMobileNo());
	        usersDTO.setEmail(user.getEmail());
	        usersDTO.setDoorNo(user.getDoorNo());
	        usersDTO.setStreet(user.getStreet());
	        usersDTO.setArea(user.getArea());
	        usersDTO.setLocation(user.getLocation());
	        usersDTO.setPinCode(user.getPinCode());
	        usersDTO.setProfilephoto(user.getProfilephoto());
	        usersDTO.setGovernmentId(user.getGovernmentId());
	        usersDTO.setActive(user.isActive());
	        usersDTO.setCdate(user.getCdate());

	        bookingDetailsDTO.setUsers(usersDTO);
	        paymentsDTO.setBookingDetails(bookingDetailsDTO);
	        return paymentsDTO;
	    }



	@Transactional
	public int saveorderLog(

			String order_id,
			String order_info,
			String bookingId
			) {

		int lastInsertId = 0;

		String sqlQuery = "INSERT INTO hall_order_log(order_id, order_info, booking_details_booking_id) "
				+ "VALUES (?,?,?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		try {
			int affectedRows = jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[] { "id" });
					ps.setString(1, order_id);
					ps.setString(2, order_info);
					ps.setString(3, bookingId);
					return ps;
				}
			}, keyHolder);

			if (affectedRows > 0) {
				Number generatedId = keyHolder.getKey();
				lastInsertId = (generatedId != null) ? generatedId.intValue() : 0;
				return lastInsertId;
			} else {
				System.out.println("Error on temp save!");
			}
		} catch (DataAccessException e) {
			System.out.println("Data Access Exception:");
			Throwable rootCause = e.getMostSpecificCause();
			if (rootCause instanceof SQLException) {
				SQLException sqlException = (SQLException) rootCause;
				System.out.println("SQL State: " + sqlException.getSQLState());
				System.out.println("Error Code: " + sqlException.getErrorCode());
				System.out.println("Message: " + sqlException.getMessage());
			} else {
				System.out.println("Message: " + rootCause.getMessage());
			}
		}
		return 0;
	}


	@Transactional
	public int updateOrderId(String orderId, int bookingId) {

		String sql = " UPDATE transaction_response SET order_id = ? " +
					" WHERE booking_id = ? " ;

		return jdbcTemplate.update(sql, orderId, bookingId);
	}

	public String getTransactionIdByOrderId(String orderId) {

		String sql = """
        SELECT txansaction_id
        FROM transaction_response
        WHERE order_id = ?
        ORDER BY id DESC
        LIMIT 1
        """;

		return jdbcTemplate.queryForObject(
				sql,
				String.class,
				orderId
		);
	}


	@Transactional
	public int updateRazorpayPaymentId(String txnId, String paymentId) {

		String sql = " UPDATE transaction_response SET payment_id = ? WHERE txansaction_id = ? " ;
		return jdbcTemplate.update(sql, paymentId, txnId);

	}


	public Map<String, Object> getTransactionPaymentDetailsByBookingId(int bookingId) {

		String sql = " select txansaction_id, payment_id, order_id  from transaction_response where booking_id = ? order by id desc limit 1 ";


		List<Map<String, Object>> rows =
				jdbcTemplate.queryForList(sql, bookingId);

		return rows.isEmpty() ? Collections.emptyMap() : rows.get(0);
	}



	public Map<String, Object> getRazorpayPaymentIdByBookingId(int bookingId) {

		String sql = "SELECT tr.payment_id, tr.order_id   " +
				" FROM transaction_response tr   " +
				" JOIN payments py ON py.booking_details_booking_id = tr.booking_id   " +
				" WHERE tr.status_code =0300   " +
				" AND tr.booking_id = ? " ;

		List<Map<String, Object>> result =
				jdbcTemplate.queryForList(sql, bookingId);

		if (result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}


}
