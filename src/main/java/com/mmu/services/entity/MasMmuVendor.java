package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the mas_mmu_vendor database table.
 * 
 */
@Entity
@Table(name="mas_mmu_vendor")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedQuery(name="MasMmuVendor.findAll", query="SELECT m FROM MasMmuVendor m")
@SequenceGenerator(name="MAS_MMU_VENDOR_GENERATOR", sequenceName="mas_mmu_vendor_seq", allocationSize=1)
public class MasMmuVendor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator = "MAS_MMU_VENDOR_GENERATOR")
	@Column(name="mmu_vendor_id")
	private Long mmuVendorId;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	@Column(name="mmu_vendor_code")
	private String mmuVendorCode;

	@Column(name="mmu_vendor_name")
	private String mmuVendorName;

	private String status;

	public MasMmuVendor() {
	}

	public Long getMmuVendorId() {
		return this.mmuVendorId;
	}

	public void setMmuVendorId(Long mmuVendorId) {
		this.mmuVendorId = mmuVendorId;
	}

	public Long getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getMmuVendorCode() {
		return this.mmuVendorCode;
	}

	public void setMmuVendorCode(String mmuVendorCode) {
		this.mmuVendorCode = mmuVendorCode;
	}

	public String getMmuVendorName() {
		return this.mmuVendorName;
	}

	public void setMmuVendorName(String mmuVendorName) {
		this.mmuVendorName = mmuVendorName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}