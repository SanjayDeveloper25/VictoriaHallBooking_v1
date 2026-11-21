package com.gcc.communityHall.entity;

import java.util.Date;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CounterDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int counterId;

	@Column(name="dd_number")
	private int ddNumber;

	@Column(name = "cdate", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	private Date cdate = new Date();

	@Column(name="bank_name")
	private String bankName;

	@Column(name="bank_acc_no")
	private int bankAccNo;

	@Column(name="amount_received")
	private double amountReceived;

	@Column(name = "dd_photo_url")
	private String ddphoto;

	@Transient
	private MultipartFile counterddphoto;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "booking_details_booking_id")
	@JsonIgnore
	private BookingDetails bookingDetails;


	public int getCounterId() {
		return counterId;
	}

	public void setCounterId(int counterId) {
		this.counterId = counterId;
	}

	public int getDdNumber() {
		return ddNumber;
	}

	public void setDdNumber(int ddNumber) {
		this.ddNumber = ddNumber;
	}

	public Date getCdate() {
		return cdate;
	}

	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public int getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(int bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	public double getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(double amountReceived) {
		this.amountReceived = amountReceived;
	}

	public String getDdphoto() {
		return ddphoto;
	}

	public void setDdphoto(String ddphoto) {
		this.ddphoto = ddphoto;
	}

	public MultipartFile getCounterddphoto() {
		return counterddphoto;
	}

	public void setCounterddphoto(MultipartFile counterddphoto) {
		this.counterddphoto = counterddphoto;
	}

	public BookingDetails getBookingDetails() {
		return bookingDetails;
	}

	public void setBookingDetails(BookingDetails bookingDetails) {
		this.bookingDetails = bookingDetails;
	}


}
