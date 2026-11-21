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
public class GccblockingDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int blockingdocumentId;

    @Column(name = "blocking_document_url")
    private String blockingDocument;
    @Column(name = "document_desc")
    private String Description;   
    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JsonIgnore
    private GccBlockingDate blockingDate;
	public int getBlockingdocumentId() {
		return blockingdocumentId;
	}
	public void setBlockingdocumentId(int blockingdocumentId) {
		this.blockingdocumentId = blockingdocumentId;
	}
	public String getBlockingDocument() {
		return blockingDocument;
	}
	public void setBlockingDocument(String blockingDocument) {
		this.blockingDocument = blockingDocument;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public GccBlockingDate getBlockingDate() {
		return blockingDate;
	}
	public void setBlockingDate(GccBlockingDate blockingDate) {
		this.blockingDate = blockingDate;
	}
}
