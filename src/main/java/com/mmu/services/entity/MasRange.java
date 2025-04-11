package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;


/**
 * The persistent class for the MAS_RANGE database table.
 * 
 */
@Entity
@Table(name="MAS_RANGE")
@NamedQuery(name="MasRange.findAll", query="SELECT m FROM MasRange m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_RANGE_SEQ", sequenceName="MAS_RANGE_SEQ",allocationSize=1)
public class MasRange implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_RANGE_SEQ")
	@Column(name="RANGE_ID")
	private long rangeId;

	@Column(name="FROM_RANGE")
	private Long fromRange;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="RANGE_FLAG")
	private String rangeFlag;

	private String status;

	@Column(name="TO_RANGE")
	private Long toRange;

	//bi-directional many-to-one association to MasAdministrativeSex
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="GENDER_ID")
	private MasAdministrativeSex masAdministrativeSex;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	public MasRange() {
	}

	public long getRangeId() {
		return this.rangeId;
	}

	public void setRangeId(long rangeId) {
		this.rangeId = rangeId;
	}

	public Long getFromRange() {
		return this.fromRange;
	}

	public void setFromRange(Long fromRange) {
		this.fromRange = fromRange;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getRangeFlag() {
		return this.rangeFlag;
	}

	public void setRangeFlag(String rangeFlag) {
		this.rangeFlag = rangeFlag;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getToRange() {
		return this.toRange;
	}

	public void setToRange(Long toRange) {
		this.toRange = toRange;
	}

	public MasAdministrativeSex getMasAdministrativeSex() {
		return this.masAdministrativeSex;
	}

	public void setMasAdministrativeSex(MasAdministrativeSex masAdministrativeSex) {
		this.masAdministrativeSex = masAdministrativeSex;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}