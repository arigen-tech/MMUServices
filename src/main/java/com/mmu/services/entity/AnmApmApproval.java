package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the anm_apm_approval database table.
 * 
 */
@Entity
@Table(name="anm_apm_approval")
@NamedQuery(name="AnmApmApproval.findAll", query="SELECT a FROM AnmApmApproval a")
public class AnmApmApproval implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ANM_APM_APPROVAL_ANMAPMID_GENERATOR", sequenceName="ANM_APM_APPROVAL_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ANM_APM_APPROVAL_ANMAPMID_GENERATOR")
	@Column(name="anm_apm_id")
	private Long anmApmId;

	@Column(name="city_id")
	private Long cityId;

	@Temporal(TemporalType.DATE)
	@Column(name="date_of_entry")
	private Date dateOfEntry;

	private String flag;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="mmu_id")
	private Long mmuId;

	@Column(name="patient_no_apply_labour_registration")
	private Long patientNoApplyLabourRegistration;

	@Column(name="patient_no_lab_test")
	private Long patientNoLabTest;

	@Column(name="patient_no_labour_registred_department")
	private Long patientNoLabourRegistredDepartment;

	@Column(name="patient_no_medicine_dispensed")
	private Long patientNoMedicineDispensed;

	private String status;

	@Column(name="total_no_patients")
	private Long totalNoPatients;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="last_chg_by",nullable=false,insertable=false,updatable=false)
	private Users user;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="mmu_id",nullable=false,insertable=false,updatable=false)
	private MasMMU masMMU;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="city_id",nullable=false,insertable=false,updatable=false)
	private MasCity masCity;
	
	@Temporal(TemporalType.DATE)
	@Column(name="opd_date")
	private Date opdDate;
	
	@Column(name="approval_action")
	private String approvalAction;

	
	private String remarks;
	

	
	
	

	public AnmApmApproval() {
	}

	public Long getAnmApmId() {
		return this.anmApmId;
	}

	public void setAnmApmId(Long anmApmId) {
		this.anmApmId = anmApmId;
	}

	public Long getCityId() {
		return this.cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Date getDateOfEntry() {
		return this.dateOfEntry;
	}

	public void setDateOfEntry(Date dateOfEntry) {
		this.dateOfEntry = dateOfEntry;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Long getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Long getMmuId() {
		return this.mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}

	public Long getPatientNoApplyLabourRegistration() {
		return this.patientNoApplyLabourRegistration;
	}

	public void setPatientNoApplyLabourRegistration(Long patientNoApplyLabourRegistration) {
		this.patientNoApplyLabourRegistration = patientNoApplyLabourRegistration;
	}

	public Long getPatientNoLabTest() {
		return this.patientNoLabTest;
	}

	public void setPatientNoLabTest(Long patientNoLabTest) {
		this.patientNoLabTest = patientNoLabTest;
	}

	public Long getPatientNoLabourRegistredDepartment() {
		return this.patientNoLabourRegistredDepartment;
	}

	public void setPatientNoLabourRegistredDepartment(Long patientNoLabourRegistredDepartment) {
		this.patientNoLabourRegistredDepartment = patientNoLabourRegistredDepartment;
	}

	public Long getPatientNoMedicineDispensed() {
		return this.patientNoMedicineDispensed;
	}

	public void setPatientNoMedicineDispensed(Long patientNoMedicineDispensed) {
		this.patientNoMedicineDispensed = patientNoMedicineDispensed;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTotalNoPatients() {
		return this.totalNoPatients;
	}

	public void setTotalNoPatients(Long totalNoPatients) {
		this.totalNoPatients = totalNoPatients;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public MasMMU getMasMMU() {
		return masMMU;
	}

	public void setMasMMU(MasMMU masMMU) {
		this.masMMU = masMMU;
	}

	public MasCity getMasCity() {
		return masCity;
	}

	public void setMasCity(MasCity masCity) {
		this.masCity = masCity;
	}

	public Date getOpdDate() {
		return opdDate;
	}

	public void setOpdDate(Date opdDate) {
		this.opdDate = opdDate;
	}

	public String getApprovalAction() {
		return approvalAction;
	}

	public void setApprovalAction(String approvalAction) {
		this.approvalAction = approvalAction;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	
	

}