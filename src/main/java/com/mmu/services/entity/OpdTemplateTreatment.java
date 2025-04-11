package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


/**
 * The persistent class for the OPD_TEMPLATE_TREATMENT database table.
 * 
 */
@Entity
@Table(name="OPD_TEMPLATE_TREATMENT")
@NamedQuery(name="OpdTemplateTreatment.findAll", query="SELECT o FROM OpdTemplateTreatment o")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="OPD_TEMPLATE_TREATMENT_SEQ", sequenceName="OPD_TEMPLATE_TREATMENT_SEQ", allocationSize=1)
public class OpdTemplateTreatment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3148274099944845605L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="OPD_TEMPLATE_TREATMENT_SEQ")
	@Column(name="TREATMENT_TEMPLATE_ID")
	private Long treatmentTemplateId;

	private String dosage;

	@Column(name="FREQUENCY_ID")
	private Long frequencyId;

	

	private String instruction;

	@Column(name="ITEM_ID")
	private Long itemId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	
	private Long noofdays;

	private String status;

	@Column(name="TEMPLATE_ID")
	private Long templateId;

	private Long total;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "TEMPLATE_ID",nullable=false,insertable=false,updatable=false)
	private OpdTemplate opdTemplate;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "ITEM_ID",nullable=false,insertable=false,updatable=false)
	private MasStoreItem masStoreItem;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "FREQUENCY_ID",nullable=false,insertable=false,updatable=false)
	private MasFrequency masFrequency;
	
	
	public Long getTreatmentTemplateId() {
		return treatmentTemplateId;
	}

	public void setTreatmentTemplateId(Long treatmentTemplateId) {
		this.treatmentTemplateId = treatmentTemplateId;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public Long getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Long frequencyId) {
		this.frequencyId = frequencyId;
	}

	

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	
	public Long getNoofdays() {
		return noofdays;
	}

	public void setNoofdays(Long noofdays) {
		this.noofdays = noofdays;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public OpdTemplate getOpdTemplate() {
		return opdTemplate;
	}

	public void setOpdTemplate(OpdTemplate opdTemplate) {
		this.opdTemplate = opdTemplate;
	}

	public MasStoreItem getMasStoreItem() {
		return masStoreItem;
	}

	public void setMasStoreItem(MasStoreItem masStoreItem) {
		this.masStoreItem = masStoreItem;
	}

	public MasFrequency getMasFrequency() {
		return masFrequency;
	}

	public void setMasFrequency(MasFrequency masFrequency) {
		this.masFrequency = masFrequency;
	}

	

}