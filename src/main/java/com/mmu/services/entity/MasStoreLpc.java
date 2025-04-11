package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the MAS_STORE_LPC database table.
 * 
 */
@Entity
@Table(name="MAS_STORE_LPC")
@NamedQuery(name="MasStoreLpc.findAll", query="SELECT m FROM MasStoreLpc m")
@SequenceGenerator(name="MAS_STORE_LPC_SEQ", sequenceName="MAS_STORE_LPC_SEQ", allocationSize=1)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MasStoreLpc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_STORE_LPC_SEQ")
	@Column(name="LPC_ID")
	private Long lpcId;

	@Temporal(TemporalType.DATE)
	@Column(name="FROM_DATE")
	private Date fromDate;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="LETTER_NO")
	private String letterNo;

	@Column(name="MEMBER1_ID")
	private Long member1Id;

	public MasHospital getMasHospital() {
		return masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	@Column(name="MEMBER2_ID")
	private Long member2Id;

	@Column(name="PRESIDENT_ID")
	private Long presidentId;

	private String status;
	
	@Column(name="RIDC_ID")
	private Long ridcId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	 @JsonBackReference
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	@Temporal(TemporalType.DATE)
	@Column(name="TO_DATE")
	private Date toDate;

	public MasStoreLpc() {
	}

	public long getLpcId() {
		return this.lpcId;
	}

	public void setLpcId(long lpcId) {
		this.lpcId = lpcId;
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public void setLpcId(Long lpcId) {
		this.lpcId = lpcId;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getLetterNo() {
		return this.letterNo;
	}

	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
	}

	public Long getMember1Id() {
		return this.member1Id;
	}

	public void setMember1Id(Long member1Id) {
		this.member1Id = member1Id;
	}

	public Long getMember2Id() {
		return this.member2Id;
	}

	public void setMember2Id(Long member2Id) {
		this.member2Id = member2Id;
	}

	public Long getPresidentId() {
		return this.presidentId;
	}

	public void setPresidentId(Long presidentId) {
		this.presidentId = presidentId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Long getRidcId() {
		return ridcId;
	}

	public void setRidcId(Long ridcId) {
		this.ridcId = ridcId;
	}

}