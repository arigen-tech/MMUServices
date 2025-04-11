package com.mmu.services.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "mas_equipment_checklist")
@SequenceGenerator(name="MAS_MMU_EQUIPMENT_CHECKLIST_GENERATOR", sequenceName="mas_equipment_checklist_seq", allocationSize=1)
public class MasEquipmentChecklist implements Serializable {

    private static final long serialVersionUID = 984336528265133843L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator ="MAS_MMU_EQUIPMENT_CHECKLIST_GENERATOR")
    @Column(name = "checklist_id")
    private Long checklistId;

    @Column(name = "instrument_code")
    private String instrumentCode;

    @Column(name = "instrument_name")
    private String instrumentName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status")
    private String status;
    
    public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	@Column(name = "sequence_no")
    private Integer sequenceNo;

    @Column(name = "penalty_id")
    private Long penaltyId;

    public Long getPenaltyId() {
        return penaltyId;
    }

    public void setPenaltyId(Long penaltyId) {
        this.penaltyId = penaltyId;
    }

    public Long getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }

    public String getInstrumentCode() {
        return instrumentCode;
    }

    public void setInstrumentCode(String instrumentCode) {
        this.instrumentCode = instrumentCode;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
