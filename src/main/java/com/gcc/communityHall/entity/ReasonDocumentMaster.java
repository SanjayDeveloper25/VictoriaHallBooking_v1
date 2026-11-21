package com.gcc.communityHall.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ReasonDocumentMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reasonDocId;
    private String fieldName;
    private boolean addTextField;
    private String docFieldName;
    private boolean addDocumentField;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reason_id")
    @JsonIgnore
    private ReasonMaster reasonMaster;

    public int getReasonDocId() {
        return reasonDocId;
    }

    public void setReasonDocId(int reasonDocId) {
        this.reasonDocId = reasonDocId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isAddTextField() {
        return addTextField;
    }

    public void setAddTextField(boolean addTextField) {
        this.addTextField = addTextField;
    }

    public String getDocFieldName() {
        return docFieldName;
    }

    public void setDocFieldName(String docFieldName) {
        this.docFieldName = docFieldName;
    }

    public boolean isAddDocumentField() {
        return addDocumentField;
    }

    public void setAddDocumentField(boolean addDocumentField) {
        this.addDocumentField = addDocumentField;
    }

    public ReasonMaster getReasonMaster() {
        return reasonMaster;
    }

    public void setReasonMaster(ReasonMaster reasonMaster) {
        this.reasonMaster = reasonMaster;
    }
}
