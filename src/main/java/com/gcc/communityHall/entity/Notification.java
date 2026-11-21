package com.gcc.communityHall.entity;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Notification {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
	    private String notification_description;
	    private LocalDate cdate;
	    private String createdBY;
	    private String updatedDate;
	    private String updatedBy;
	    private boolean isActive;
	    private boolean isDelete;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getNotification_description() {
			return notification_description;
		}
		public void setNotification_description(String notification_description) {
			this.notification_description = notification_description;
		}
		
		public LocalDate getCdate() {
			return cdate;
		}
		public void setCdate(LocalDate cdate) {
			this.cdate = cdate;
		}
		public String getCreatedBY() {
			return createdBY;
		}
		public void setCreatedBY(String createdBY) {
			this.createdBY = createdBY;
		}
		public String getUpdatedDate() {
			return updatedDate;
		}
		public void setUpdatedDate(String updatedDate) {
			this.updatedDate = updatedDate;
		}
		public String getUpdatedBy() {
			return updatedBy;
		}
		public void setUpdatedBy(String updatedBy) {
			this.updatedBy = updatedBy;
		}
		public boolean isActive() {
			return isActive;
		}
		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}
		public boolean isDelete() {
			return isDelete;
		}
		public void setDelete(boolean isDelete) {
			this.isDelete = isDelete;
		} 
	    }
