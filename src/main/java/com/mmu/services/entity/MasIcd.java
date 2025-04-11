package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the MAS_ICD database table.
 * 
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="MAS_ICD")
@NamedQuery(name="MasIcd.findAll", query="SELECT m FROM MasIcd m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_ICD_ICDID_GENERATOR", sequenceName="MAS_ICD_SEQ", allocationSize=1)
public class MasIcd implements Serializable {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((icdCode == null) ? 0 : icdCode.hashCode());
		result = prime * result + ((icdId == null) ? 0 : icdId.hashCode());
		result = prime * result + ((icdName == null) ? 0 : icdName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MasIcd other = (MasIcd) obj;
		if (icdCode == null) {
			if (other.icdCode != null)
				return false;
		} else if (!icdCode.equals(other.icdCode))
			return false;
		if (icdId == null) {
			if (other.icdId != null)
				return false;
		} else if (!icdId.equals(other.icdId))
			return false;
		if (icdName == null) {
			if (other.icdName != null)
				return false;
		} else if (!icdName.equals(other.icdName))
			return false;
		return true;
	}

	private static final long serialVersionUID = -7505013476362497072L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_ICD_ICDID_GENERATOR")
	@Column(name="ICD_ID")
	private Long icdId;

	@Column(name="ICD_CAUSE_ID")
	private Long icdCauseId;

	@Column(name="ICD_CODE")
	private String icdCode;

	@Column(name="ICD_NAME")
	public String icdName;

	@Column(name="ICD_SUB_CATEGORY_ID")
	private Long icdSubCategoryId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;
	
	@Column(name="OPD_FLAG")
	private String opdFlag;

	private String status;
	
	@Column(name="communicable_flag")
	public String communicableFlag;
	
	@Column(name="infectious_flag")
	public String infectionsFlag;
	
	@OneToMany(mappedBy="masIcd")
	@JsonBackReference
	private List<ReferralPatientDt> referralPatientDt;

	public Long getIcdId() {
		return icdId;
	}

	public void setIcdId(Long icdId) {
		this.icdId = icdId;
	}

	public Long getIcdCauseId() {
		return icdCauseId;
	}

	public void setIcdCauseId(Long icdCauseId) {
		this.icdCauseId = icdCauseId;
	}

	public String getIcdCode() {
		return icdCode;
	}

	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}

	public String getIcdName() {
		return icdName;
	}

	public void setIcdName(String icdName) {
		this.icdName = icdName;
	}

	public Long getIcdSubCategoryId() {
		return icdSubCategoryId;
	}

	public void setIcdSubCategoryId(Long icdSubCategoryId) {
		this.icdSubCategoryId = icdSubCategoryId;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ReferralPatientDt> getReferralPatientDt() {
		return referralPatientDt;
	}

	public void setReferralPatientDt(List<ReferralPatientDt> referralPatientDt) {
		this.referralPatientDt = referralPatientDt;
	}
	
	 @OneToMany(mappedBy="masIcd", cascade = CascadeType.ALL)
	 @JsonBackReference
	 private List<DischargeIcdCode> dischargeIcdCode;

	public List<DischargeIcdCode> getDischargeIcdCode() {
		return dischargeIcdCode;
	}

	public void setDischargeIcdCode(List<DischargeIcdCode> dischargeIcdCode) {
		this.dischargeIcdCode = dischargeIcdCode;
	}
	 
	
	
	public String getOpdFlag() {
		return opdFlag;
	}

	public void setOpdFlag(String opdFlag) {
		this.opdFlag = opdFlag;
	}
	

	public String getCommunicableFlag() {
		return communicableFlag;
	}

	public void setCommunicableFlag(String communicableFlag) {
		this.communicableFlag = communicableFlag;
	}
	
	public String getInfectionsFlag() {
		return infectionsFlag;
	}

	public void setInfectionsFlag(String infectionsFlag) {
		this.infectionsFlag = infectionsFlag;
	}
	
	
	@Column(name="most_common_user")
	private String mostCommonUser;



	public String getMostCommonUser() {
		return mostCommonUser;
	}

	public void setMostCommonUser(String mostCommonUser) {
		this.mostCommonUser = mostCommonUser;
	}
	
	
	
	 
	

}