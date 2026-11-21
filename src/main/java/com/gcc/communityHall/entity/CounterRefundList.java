package com.gcc.communityHall.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

@Entity
public class CounterRefundList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Transient
    private MultipartFile letterdocs;
    @Column(name = "letter_docs_url")
    private String letterdocument;
    public enum InitialisationStatus{
        APPROVED,INITIATED,REJECTED;
    }
    @Column(name="initialisation_status")
    @Enumerated(EnumType.STRING)
    private InitialisationStatus initialisationStatus;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "counterdetails_counter_id")
    @JsonIgnore
    private CounterDetails counterDetails;
    
    @Column(name="reasonforCancelling")
    private String reasonforCancelling;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MultipartFile getLetterdocs() {
        return letterdocs;
    }

    public void setLetterdocs(MultipartFile letterdocs) {
        this.letterdocs = letterdocs;
    }

    public String getLetterdocument() {
        return letterdocument;
    }

    public void setLetterdocument(String letterdocument) {
        this.letterdocument = letterdocument;
    }

    public CounterDetails getCounterDetails() {
        return counterDetails;
    }

    public void setCounterDetails(CounterDetails counterDetails) {
        this.counterDetails = counterDetails;
    }

    public InitialisationStatus getInitialisationStatus() {
        return initialisationStatus;
    }

    public void setInitialisationStatus(InitialisationStatus initialisationStatus) {
        this.initialisationStatus = initialisationStatus;
    }

	public String getReasonforCancelling() {
		return reasonforCancelling;
	}

	public void setReasonforCancelling(String reasonforCancelling) {
		this.reasonforCancelling = reasonforCancelling;
	}

	public CounterRefundList() {
		super();
	}
    
    
}
