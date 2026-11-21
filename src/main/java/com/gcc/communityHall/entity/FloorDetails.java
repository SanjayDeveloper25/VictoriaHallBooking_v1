package com.gcc.communityHall.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class FloorDetails {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int floorId;

	    @Column(name = "floor")
	    private String floor;
	    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	    @JsonIgnore
	    private HallDetails hallDetails;
	    
	    @Column(name="area")
	    private double hallarea;
	   

	    public HallDetails getHallDetails() {
	        return hallDetails;
	    }

	    public void setHallDetails(HallDetails hallDetails) {
	        this.hallDetails = hallDetails;
	    }

		public int getFloorId() {
			return floorId;
		}

		public void setFloorId(int floorId) {
			this.floorId = floorId;
		}

		public String getFloor() {
			return floor;
		}

		public void setFloor(String floor) {
			this.floor = floor;
		}

		public double getHallarea() {
			return hallarea;
		}

		public void setHallarea(double hallarea) {
			this.hallarea = hallarea;
		}

		public FloorDetails(int floorId, String floor, HallDetails hallDetails, double hallarea) {
			super();
			this.floorId = floorId;
			this.floor = floor;
			this.hallDetails = hallDetails;
			this.hallarea = hallarea;
		}

		public FloorDetails() {
			super();
		}
	    
}
