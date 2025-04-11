package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the lagecy_data database table.
 * 
 */
@Entity
@Table(name="lagecy_data")
@NamedQuery(name="LagecyData.findAll", query="SELECT l FROM LagecyData l")
public class LagecyData implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LAGECY_DATA_LAGECYDATAID_GENERATOR", sequenceName="LAGECY_DATA_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LAGECY_DATA_LAGECYDATAID_GENERATOR")
	@Column(name="lagecy_data_id")
	private Long lagecyDataId;

	@Column(name="city_id")
	private Integer cityId;

	@Column(name="dai_avg_patient")
	private Integer daiAvgPatient;

	@Column(name="dai_camp_count")
	private Integer daiCampCount;

	@Column(name="dai_dep_reg_patient")
	private Integer daiDepRegPatient;

	@Column(name="dai_lab_patient")
	private Integer daiLabPatient;

	@Column(name="dai_lab_reg_patient")
	private Integer daiLabRegPatient;

	@Column(name="dai_med_patient")
	private Integer daiMedPatient;

	@Column(name="dai_tot_patient")
	private Integer daiTotPatient;

	@Column(name="labour_app_boc")
	private Integer labourAppBoc;

	@Column(name="labour_child")
	private Integer labourChild;

	@Column(name="labour_female")
	private Integer labourFemale;

	@Column(name="labour_male")
	private Integer labourMale;

	@Column(name="labour_o")
	private Integer labourO;

	@Column(name="labour_oth_boc")
	private Integer labourOthBoc;

	@Column(name="labour_reg_boc")
	private Integer labourRegBoc;

	@Column(name="labour_reg_gc")
	private Integer labourRegGc;

	@Column(name="labour_reg_other")
	private Integer labourRegOther;

	@Column(name="labour_tot")
	private Integer labourTot;

	@Column(name="labour_unreg_boc")
	private Integer labourUnregBoc;

	@Column(name="labour_unreg_other")
	private Integer labourUnregOther;

	@Column(name="mmssy_avg_patient")
	private Integer mmssyAvgPatient;

	@Column(name="mmssy_camp_count")
	private Integer mmssyCampCount;

	@Column(name="mmssy_dep_reg_patient")
	private Integer mmssyDepRegPatient;

	@Column(name="mmssy_lab_patient")
	private Integer mmssyLabPatient;

	@Column(name="mmssy_lab_reg_patient")
	private Integer mmssyLabRegPatient;

	@Column(name="mmssy_med_patient")
	private Integer mmssyMedPatient;

	@Column(name="mmssy_tot_patient")
	private Integer mmssyTotPatient;

	public LagecyData() {
	}

	public Long getLagecyDataId() {
		return this.lagecyDataId;
	}

	public void setLagecyDataId(Long lagecyDataId) {
		this.lagecyDataId = lagecyDataId;
	}

	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getDaiAvgPatient() {
		return this.daiAvgPatient;
	}

	public void setDaiAvgPatient(Integer daiAvgPatient) {
		this.daiAvgPatient = daiAvgPatient;
	}

	public Integer getDaiCampCount() {
		return this.daiCampCount;
	}

	public void setDaiCampCount(Integer daiCampCount) {
		this.daiCampCount = daiCampCount;
	}

	public Integer getDaiDepRegPatient() {
		return this.daiDepRegPatient;
	}

	public void setDaiDepRegPatient(Integer daiDepRegPatient) {
		this.daiDepRegPatient = daiDepRegPatient;
	}

	public Integer getDaiLabPatient() {
		return this.daiLabPatient;
	}

	public void setDaiLabPatient(Integer daiLabPatient) {
		this.daiLabPatient = daiLabPatient;
	}

	public Integer getDaiLabRegPatient() {
		return this.daiLabRegPatient;
	}

	public void setDaiLabRegPatient(Integer daiLabRegPatient) {
		this.daiLabRegPatient = daiLabRegPatient;
	}

	public Integer getDaiMedPatient() {
		return this.daiMedPatient;
	}

	public void setDaiMedPatient(Integer daiMedPatient) {
		this.daiMedPatient = daiMedPatient;
	}

	public Integer getDaiTotPatient() {
		return this.daiTotPatient;
	}

	public void setDaiTotPatient(Integer daiTotPatient) {
		this.daiTotPatient = daiTotPatient;
	}

	public Integer getLabourAppBoc() {
		return this.labourAppBoc;
	}

	public void setLabourAppBoc(Integer labourAppBoc) {
		this.labourAppBoc = labourAppBoc;
	}

	public Integer getLabourChild() {
		return this.labourChild;
	}

	public void setLabourChild(Integer labourChild) {
		this.labourChild = labourChild;
	}

	public Integer getLabourFemale() {
		return this.labourFemale;
	}

	public void setLabourFemale(Integer labourFemale) {
		this.labourFemale = labourFemale;
	}

	public Integer getLabourMale() {
		return this.labourMale;
	}

	public void setLabourMale(Integer labourMale) {
		this.labourMale = labourMale;
	}

	public Integer getLabourO() {
		return this.labourO;
	}

	public void setLabourO(Integer labourO) {
		this.labourO = labourO;
	}

	public Integer getLabourOthBoc() {
		return this.labourOthBoc;
	}

	public void setLabourOthBoc(Integer labourOthBoc) {
		this.labourOthBoc = labourOthBoc;
	}

	public Integer getLabourRegBoc() {
		return this.labourRegBoc;
	}

	public void setLabourRegBoc(Integer labourRegBoc) {
		this.labourRegBoc = labourRegBoc;
	}

	public Integer getLabourRegGc() {
		return this.labourRegGc;
	}

	public void setLabourRegGc(Integer labourRegGc) {
		this.labourRegGc = labourRegGc;
	}

	public Integer getLabourRegOther() {
		return this.labourRegOther;
	}

	public void setLabourRegOther(Integer labourRegOther) {
		this.labourRegOther = labourRegOther;
	}

	public Integer getLabourTot() {
		return this.labourTot;
	}

	public void setLabourTot(Integer labourTot) {
		this.labourTot = labourTot;
	}

	public Integer getLabourUnregBoc() {
		return this.labourUnregBoc;
	}

	public void setLabourUnregBoc(Integer labourUnregBoc) {
		this.labourUnregBoc = labourUnregBoc;
	}

	public Integer getLabourUnregOther() {
		return this.labourUnregOther;
	}

	public void setLabourUnregOther(Integer labourUnregOther) {
		this.labourUnregOther = labourUnregOther;
	}

	public Integer getMmssyAvgPatient() {
		return this.mmssyAvgPatient;
	}

	public void setMmssyAvgPatient(Integer mmssyAvgPatient) {
		this.mmssyAvgPatient = mmssyAvgPatient;
	}

	public Integer getMmssyCampCount() {
		return this.mmssyCampCount;
	}

	public void setMmssyCampCount(Integer mmssyCampCount) {
		this.mmssyCampCount = mmssyCampCount;
	}

	public Integer getMmssyDepRegPatient() {
		return this.mmssyDepRegPatient;
	}

	public void setMmssyDepRegPatient(Integer mmssyDepRegPatient) {
		this.mmssyDepRegPatient = mmssyDepRegPatient;
	}

	public Integer getMmssyLabPatient() {
		return this.mmssyLabPatient;
	}

	public void setMmssyLabPatient(Integer mmssyLabPatient) {
		this.mmssyLabPatient = mmssyLabPatient;
	}

	public Integer getMmssyLabRegPatient() {
		return this.mmssyLabRegPatient;
	}

	public void setMmssyLabRegPatient(Integer mmssyLabRegPatient) {
		this.mmssyLabRegPatient = mmssyLabRegPatient;
	}

	public Integer getMmssyMedPatient() {
		return this.mmssyMedPatient;
	}

	public void setMmssyMedPatient(Integer mmssyMedPatient) {
		this.mmssyMedPatient = mmssyMedPatient;
	}

	public Integer getMmssyTotPatient() {
		return this.mmssyTotPatient;
	}

	public void setMmssyTotPatient(Integer mmssyTotPatient) {
		this.mmssyTotPatient = mmssyTotPatient;
	}
	
	//bi-directional many-to-one association to Patient
			@ManyToOne(fetch=FetchType.LAZY)
			@JoinColumn(name="city_id",nullable=false,insertable=false,updatable=false)
			private MasCity masCity;
			
			public MasCity getMasCity() {
				return masCity;
			}

			public void setMasCity(MasCity masCity) {
				this.masCity = masCity;
			}

}