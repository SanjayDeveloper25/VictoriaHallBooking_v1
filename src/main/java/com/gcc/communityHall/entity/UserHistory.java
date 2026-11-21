package com.gcc.communityHall.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class UserHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userHisId;

    private Date cdate ;

    @Column(name="user_status", columnDefinition = "VARCHAR(20) DEFAULT 'APPROVED'")
    @Enumerated(EnumType.STRING)
    private Users.userStatus userstatus = Users.userStatus.APPROVED ;

    private String comments;
    private  String updatedBy;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private Users users;

    public int getUserHisId() {
        return userHisId;
    }

    public void setUserHisId(int userHisId) {
        this.userHisId = userHisId;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public Users.userStatus getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(Users.userStatus userstatus) {
        this.userstatus = userstatus;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
