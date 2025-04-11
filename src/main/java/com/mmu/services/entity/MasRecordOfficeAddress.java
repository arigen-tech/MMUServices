package com.mmu.services.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the MAS_RECORD_OFFICE_ADDRESS database table.
 * 
 */
@Entity
@Immutable
@Table(name="VU_MAS_RECORD_OFFICE_ADDRESS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MasRecordOfficeAddress implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2094202350259517643L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="RECORD_OFFICE_ADDRESS_ID",updatable = false, nullable = false)
	private long recordOfficeAddressId;


	@Column(name="RECORD_OFFICE_ADDRESS_CODE")
	private String recordOfficeCode;

	@Column(name="RECORD_OFFICE_ADDRESS_NAME")
	private String recordOfficeAddressName;	
	
	@OneToMany(mappedBy="masRecordOfficeAddress")
	@JsonBackReference
	private List<MasEmployee> masEmployee;

	

	public MasRecordOfficeAddress() {
	}

	public long getRecordOfficeAddressId() {
		return this.recordOfficeAddressId;
	}

	public void setRecordOfficeAddressId(long recordOfficeAddressId) {
		this.recordOfficeAddressId = recordOfficeAddressId;
	}

	

	public String getRecordOfficeCode() {
		return this.recordOfficeCode;
	}

	public void setRecordOfficeCode(String recordOfficeCode) {
		this.recordOfficeCode = recordOfficeCode;
	}

	

	public String getRecordOfficeAddressName() {
		return recordOfficeAddressName;
	}

	public void setRecordOfficeAddressName(String recordOfficeAddressName) {
		this.recordOfficeAddressName = recordOfficeAddressName;
	}

	
	public List<MasEmployee> getMasEmployee() {
		return masEmployee;
	}

	public void setMasEmployee(List<MasEmployee> masEmployee) {
		this.masEmployee = masEmployee;
	}

}