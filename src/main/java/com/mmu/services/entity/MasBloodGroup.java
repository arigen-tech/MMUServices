package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the MAS_BLOOD_GROUP database table.
 * 
 */
@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="MAS_BLOOD_GROUP")
@NamedQuery(name="MasBloodGroup.findAll", query="SELECT m FROM MasBloodGroup m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_BLOOD_GROUP_SEQ", sequenceName="MAS_BLOOD_GROUP_SEQ", allocationSize=1)
public class MasBloodGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 50681676974136779L;

	@Id
	@SequenceGenerator(name="MAS_BLOOD_GROUP_SEQ", sequenceName="MAS_BLOOD_GROUP_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_BLOOD_GROUP_SEQ")
	@Column(name="BLOOD_GROUP_ID")
	private long bloodGroupId;

	@Column(name="BLOOD_GROUP_CODE")
	private long bloodGroupCode;

	@Column(name="BLOOD_GROUP_NAME")
	private String bloodGroupName;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY")
	private Users user;

	//@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;



	private String status;
	
	/*
	@OneToMany(mappedBy="masBloodGroup")
	@JsonBackReference
	private List<MasEmployee> masEmployees;

	
	@OneToMany(mappedBy="masBloodGroup")
	@JsonBackReference
	private List<MasEmployeeDependent> masEmployeeDependents;
    */
	
	public MasBloodGroup() {
	}

	public long getBloodGroupId() {
		return this.bloodGroupId;
	}

	public void setBloodGroupId(long bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public Long getBloodGroupCode() {
		return this.bloodGroupCode;
	}

	public void setBloodGroupCode(Long bloodGroupCode) {
		this.bloodGroupCode = bloodGroupCode;
	}

	public String getBloodGroupName() {
		return this.bloodGroupName;
	}

	public void setBloodGroupName(String bloodGroupName) {
		this.bloodGroupName = bloodGroupName;
	}

	/*
	 * public String getLastChgBy() { return this.lastChgBy; }
	 * 
	 * public void setLastChgBy(String lastChgBy) { this.lastChgBy = lastChgBy; }
	 */

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}


	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	/*
	public List<MasEmployee> getMasEmployees() {
		return this.masEmployees;
	}

	public void setMasEmployees(List<MasEmployee> masEmployees) {
		this.masEmployees = masEmployees;
	}

	
	
	public List<MasEmployeeDependent> getMasEmployeeDependents() {
		return this.masEmployeeDependents;
	}

	public void setMasEmployeeDependents(List<MasEmployeeDependent> masEmployeeDependents) {
		this.masEmployeeDependents = masEmployeeDependents;
	}

	public MasEmployeeDependent addMasEmployeeDependent(MasEmployeeDependent masEmployeeDependent) {
		getMasEmployeeDependents().add(masEmployeeDependent);
		masEmployeeDependent.setMasBloodGroup(this);

		return masEmployeeDependent;
	}

	public MasEmployeeDependent removeMasEmployeeDependent(MasEmployeeDependent masEmployeeDependent) {
		getMasEmployeeDependents().remove(masEmployeeDependent);
		masEmployeeDependent.setMasBloodGroup(null);

		return masEmployeeDependent;
	}*/

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
}