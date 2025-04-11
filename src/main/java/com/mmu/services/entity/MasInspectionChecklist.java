package com.mmu.services.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "mas_inspection_checklist")
@SequenceGenerator(name="MAS_MMU_INSPECTION_CHECKLIST_GENERATOR", sequenceName="mas_inspection_checklist_seq", allocationSize=1)
public class MasInspectionChecklist implements Serializable {

    private static final long serialVersionUID = 984571528265133843L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator ="MAS_MMU_INSPECTION_CHECKLIST_GENERATOR")
    @Column(name = "checklist_id")
    private Long checklistId;

    @Column(name = "checklist_name")
    private String checklistName;

    @Column(name = "type_of_input")
    private String typeOfInput;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @Column(name = "subsequence")
    private Integer subsequence;

    @Column(name = "penalty_id")
    private Long penaltyId;

    @Column(name = "status")
    private String status;

    public Long getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    public String getTypeOfInput() {
        return typeOfInput;
    }

    public void setTypeOfInput(String typeOfInput) {
        this.typeOfInput = typeOfInput;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSubsequence() {
        return subsequence;
    }

    public void setSubsequence(Integer subsequence) {
        this.subsequence = subsequence;
    }

    public Long getPenaltyId() {
        return penaltyId;
    }

    public void setPenaltyId(Long penaltyId) {
        this.penaltyId = penaltyId;
    }
}
