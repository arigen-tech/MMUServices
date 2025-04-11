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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "mmu_department")
@SequenceGenerator(name="MMU_DEPARTMENT_ID_GENERATOR", sequenceName="mmu_department_seq", allocationSize=1)
public class MMUDepartment implements Serializable {

	private static final long serialVersionUID = -360465555676867904L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="MMU_DEPARTMENT_ID_GENERATOR")
	@Column(name = "mmu_department_id")
	private Long mmuDepartmentId;
	
	@Column(name = "mmu_id")
	private Long mmuId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mmu_id", insertable = false, updatable = false, nullable = false)
	private MasMMU masMMU;
	
	@Column(name = "department_id")
	private Long departmentId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", insertable = false, updatable = false, nullable = false)
	private MasDepartment masDepartment;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "last_chg_by")
	private Long lastChangeBy;
	
	@Column(name = "last_chg_date")
	private Date lastChangeDate;

	public Long getMmuDepartmentId() {
		return mmuDepartmentId;
	}

	public void setMmuDepartmentId(Long mmuDepartmentId) {
		this.mmuDepartmentId = mmuDepartmentId;
	}

	public Long getMmuId() {
		return mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	
	public MasMMU getMasMMU() {
		return masMMU;
	}

	public void setMasMMU(MasMMU masMMU) {
		this.masMMU = masMMU;
	}

	public MasDepartment getMasDepartment() {
		return masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getLastChangeBy() {
		return lastChangeBy;
	}

	public void setLastChangeBy(Long lastChangeBy) {
		this.lastChangeBy = lastChangeBy;
	}

	public Date getLastChangeDate() {
		return lastChangeDate;
	}

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}
	
}
