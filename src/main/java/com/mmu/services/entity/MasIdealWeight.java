package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/**
 * The persistent class for the MAS_IDEAL_WEIGHT database table.
 * 
 */
@Entity
@Table(name="MAS_IDEAL_WEIGHT")
@NamedQuery(name="MasIdealWeight.findAll", query="SELECT m FROM MasIdealWeight m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_IDEAL_WEIGHT_SEQ", sequenceName="MAS_IDEAL_WEIGHT_SEQ",allocationSize=1)
public class MasIdealWeight implements Serializable {
	private static final long serialVersionUID = 1247166928149155978L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_IDEAL_WEIGHT_SEQ")
	@Column(name="IDEAL_WEIGHT_ID")
	private Long idealWeightId;
		
	@Column(name="WEIGHT")
	private Double weight;
	
	//bi-directional many-to-one association to MasAdministrativeSex
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="GENDER_ID")
		private MasAdministrativeSex masAdministrativeSex;

		//bi-directional many-to-one association to MasRange
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="HEIGHT_RANGE_ID")
		private MasRange masRange1;

		//bi-directional many-to-one association to MasRange
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="AGE_RANGE_ID")
		private MasRange masRange2;
	
	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;
	
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="SD")
	private String sd;

	public MasIdealWeight() {
	}

	public long getIdealWeightId() {
		return this.idealWeightId;
	}

	public void setIdealWeightId(long idealWeightId) {
		this.idealWeightId = idealWeightId;
	}

	public void setIdealWeightId(Long idealWeightId) {
		this.idealWeightId = idealWeightId;
	}

	public Date getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getSd() {
		return this.sd;
	}

	public void setSd(String sd) {
		this.sd = sd;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public MasAdministrativeSex getMasAdministrativeSex() {
		return masAdministrativeSex;
	}

	public void setMasAdministrativeSex(MasAdministrativeSex masAdministrativeSex) {
		this.masAdministrativeSex = masAdministrativeSex;
	}

	public MasRange getMasRange1() {
		return masRange1;
	}

	public void setMasRange1(MasRange masRange1) {
		this.masRange1 = masRange1;
	}

	public MasRange getMasRange2() {
		return masRange2;
	}

	public void setMasRange2(MasRange masRange2) {
		this.masRange2 = masRange2;
	}

	
}