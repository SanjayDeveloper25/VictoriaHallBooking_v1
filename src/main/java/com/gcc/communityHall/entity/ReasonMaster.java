package com.gcc.communityHall.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class ReasonMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reasonId;
    private String reason;
    private String reasoncode;

    private LocalDate cdate = LocalDate.now();
    private String updatedBy;
    private int maxdays;

    private boolean isActive;
    private boolean isDelete;

    @OneToMany(mappedBy = "reasonMaster", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReasonDocumentMaster> reasonDocumentMasterList;

    public int getReasonId() {
        return reasonId;
    }

    public void setReasonId(int reasonId) {
        this.reasonId = reasonId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getCdate() {
        return cdate;
    }

    public void setCdate(LocalDate cdate) {
        this.cdate = cdate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<ReasonDocumentMaster> getReasonDocumentMasterList() {
        return reasonDocumentMasterList;
    }

    public void setReasonDocumentMasterList(List<ReasonDocumentMaster> reasonDocumentMasterList) {
        this.reasonDocumentMasterList = reasonDocumentMasterList;
    }

    public int getMaxdays() {
        return maxdays;
    }

    public void setMaxdays(int maxdays) {
        this.maxdays = maxdays;
    }

    public String getReasoncode() {
        return reasoncode;
    }

    public void setReasoncode(String reasoncode) {
        this.reasoncode = reasoncode;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getActive() {
        return isActive;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public boolean getDelete() {
        return isDelete;
    }

}
