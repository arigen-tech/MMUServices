package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;


/**
 * The persistent class for the VISITOR_DETAILS database table.
 * 
 */
@Entity
@Table(name="VISITOR_DETAILS")
@NamedQuery(name="VisitorDetail.findAll", query="SELECT v FROM VisitorDetail v")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="VISITOR_DETAILS_SEQ", sequenceName="VISITOR_DETAILS_SEQ",allocationSize=1)
public class VisitorDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="VISITOR_DETAILS_SEQ")
	@Column(name="VISITOR_DETAILS_ID")
	private long visitorDetailsId;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="RANK_AND_NAME")
	private String rankAndName;

	private String remarks;

	@Column(name="VISITOR_NAME")
	private String visitorName;

	//bi-directional many-to-one association to HospitalVisitRegister
	@ManyToOne
	@JoinColumn(name="HOSTPITAL_VISIT_REGISTER_ID")
	private HospitalVisitRegister hospitalVisitRegister;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	public VisitorDetail() {
	}

	public long getVisitorDetailsId() {
		return this.visitorDetailsId;
	}

	public void setVisitorDetailsId(long visitorDetailsId) {
		this.visitorDetailsId = visitorDetailsId;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getRankAndName() {
		return this.rankAndName;
	}

	public void setRankAndName(String rankAndName) {
		this.rankAndName = rankAndName;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getVisitorName() {
		return this.visitorName;
	}

	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}

	public HospitalVisitRegister getHospitalVisitRegister() {
		return this.hospitalVisitRegister;
	}

	public void setHospitalVisitRegister(HospitalVisitRegister hospitalVisitRegister) {
		this.hospitalVisitRegister = hospitalVisitRegister;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}