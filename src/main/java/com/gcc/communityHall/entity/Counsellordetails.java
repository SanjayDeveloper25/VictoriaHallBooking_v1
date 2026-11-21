package com.gcc.communityHall.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="counsellor_details")
public class Counsellordetails {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "cid")
	    private int cId;
	 
	    @Column(name = "name")
	    private String name;
	    @Column(name = "zone")
	    private int zone;
	    
	    @Column(name = "divid")
	    private int divid;
	    
	    @Column(name = "mobile1")
	    private long mobile1;
	    
	    @Column(name = "mobile2")
	    private long mobile2;
	    
	    @Column(name = "emailid")
	    private String emailid;
		public Counsellordetails(int cId, String name, int zone, int divid, long mobile1, long mobile2,
				String emailid) {
			super();
			this.cId = cId;
			this.name = name;
			this.zone = zone;
			this.divid = divid;
			this.mobile1 = mobile1;
			this.mobile2 = mobile2;
			this.emailid = emailid;
		}
		
		
		public Counsellordetails() {
			super();
		}


		public int getcId() {
			return cId;
		}
		public void setcId(int cId) {
			this.cId = cId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getZone() {
			return zone;
		}
		public void setZone(int zone) {
			this.zone = zone;
		}
		public int getDivid() {
			return divid;
		}
		public void setDivid(int divid) {
			this.divid = divid;
		}
		public long getMobile1() {
			return mobile1;
		}
		public void setMobile1(long mobile1) {
			this.mobile1 = mobile1;
		}
		public long getMobile2() {
			return mobile2;
		}
		public void setMobile2(long mobile2) {
			this.mobile2 = mobile2;
		}
		public String getEmailid() {
			return emailid;
		}
		public void setEmailid(String emailid) {
			this.emailid = emailid;
		}
	    
	    


}
