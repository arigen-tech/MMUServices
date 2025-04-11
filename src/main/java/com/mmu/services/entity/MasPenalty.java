package com.mmu.services.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "mas_penalty")
@SequenceGenerator(name="MAS_MMU_PENALTY_GENERATOR", sequenceName="mas_penalty_seq", allocationSize=1)
public class MasPenalty implements Serializable {

    private static final long serialVersionUID = 984571528265139443L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator ="MAS_MMU_PENALTY_GENERATOR")
    @Column(name = "penalty_id")
    private Long penaltyId;

    @Column(name = "penalty_code")
    private String penaltyCode;

    @Column(name = "penalty_description")
    private String penaltyDescription;

    @Column(name = "penalty_amount")
    private Double penaltyAmount;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "status")
    private String status;

    public Long getPenaltyId() {
        return penaltyId;
    }

    public void setPenaltyId(Long penaltyId) {
        this.penaltyId = penaltyId;
    }

    public String getPenaltyCode() {
        return penaltyCode;
    }

    public void setPenaltyCode(String penaltyCode) {
        this.penaltyCode = penaltyCode;
    }

    public String getPenaltyDescription() {
        return penaltyDescription;
    }

    public void setPenaltyDescription(String penaltyDescription) {
        this.penaltyDescription = penaltyDescription;
    }

    public Double getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(Double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
