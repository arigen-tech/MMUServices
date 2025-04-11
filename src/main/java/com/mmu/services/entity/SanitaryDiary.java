package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
 * The persistent class for the SANITARY_DIARY database table.
 * 
 */
@Entity
@Table(name="SANITARY_DIARY")
@NamedQuery(name="SanitaryDiary.findAll", query="SELECT s FROM SanitaryDiary s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="SANITARY_DIARY_SEQ", sequenceName="SANITARY_DIARY_SEQ",allocationSize=1)
public class SanitaryDiary implements Serializable {
	private static final long serialVersionUID = -81141547352681553L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="SANITARY_DIARY_SEQ")
	@Column(name="DIARY_ID")
	private long diaryId;

	private String area;

	@Column(name="CO_REMARKS")
	private String coRemarks;

	@Temporal(TemporalType.DATE)
	@Column(name="DIARY_DATE")
	private Date diaryDate;

	@Column(name="EXO_REMARKS")
	private String exoRemarks;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="PMO_OBSERVATION")
	private String pmoObservation;

	@Column(name="PMO_RECOMMENDATION")
	private String pmoRecommendation;

	@Column(name="TAKEN_ACTION")
	private String takenAction;

	@Column(name="TAKEN_FOLLOW")
	private String takenFollow;

	//bi-directional many-to-one association to MasHospital
	@ManyToOne
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public SanitaryDiary() {
	}

	public long getDiaryId() {
		return this.diaryId;
	}

	public void setDiaryId(long diaryId) {
		this.diaryId = diaryId;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCoRemarks() {
		return this.coRemarks;
	}

	public void setCoRemarks(String coRemarks) {
		this.coRemarks = coRemarks;
	}

	public Date getDiaryDate() {
		return this.diaryDate;
	}

	public void setDiaryDate(Date diaryDate) {
		this.diaryDate = diaryDate;
	}

	public String getExoRemarks() {
		return this.exoRemarks;
	}

	public void setExoRemarks(String exoRemarks) {
		this.exoRemarks = exoRemarks;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getPmoObservation() {
		return this.pmoObservation;
	}

	public void setPmoObservation(String pmoObservation) {
		this.pmoObservation = pmoObservation;
	}

	public String getPmoRecommendation() {
		return this.pmoRecommendation;
	}

	public void setPmoRecommendation(String pmoRecommendation) {
		this.pmoRecommendation = pmoRecommendation;
	}

	public String getTakenAction() {
		return this.takenAction;
	}

	public void setTakenAction(String takenAction) {
		this.takenAction = takenAction;
	}

	public String getTakenFollow() {
		return this.takenFollow;
	}

	public void setTakenFollow(String takenFollow) {
		this.takenFollow = takenFollow;
	}

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	
}