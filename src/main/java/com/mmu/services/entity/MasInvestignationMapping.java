package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the MAS_INVESTIGNATION_MAPPING database table.
 * 
 */
@Entity
@Table(name="MAS_INVESTIGNATION_MAPPING")
@NamedQuery(name="MasInvestignationMapping.findAll", query="SELECT m FROM MasInvestignationMapping m")
@SequenceGenerator(name="MAS_INVESTIGNATION_MAPPING_ID_GENERATOR", sequenceName="MAS_INVESTIGNATION_MAPPING_SEQ", allocationSize=1 )
public class MasInvestignationMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_INVESTIGNATION_MAPPING_ID_GENERATOR")
	private Long id;

	private String age;

	@Column(name="INVESTIGNATION_ID")
	private String investignationId;	
	
	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;
	
	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
	private Users user;
	
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	/*
	 * @Column(name="MEDICAL_EXAM_ID") private Long medicalExamId;
	 */

	private String status;

	public MasInvestignationMapping() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAge() {
		return this.age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getInvestignationId() {
		return this.investignationId;
	}

	public void setInvestignationId(String investignationId) {
		this.investignationId = investignationId;
	}
	
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}


	/*
	 * public Long getMedicalExamId() { return medicalExamId; }
	 * 
	 * public void setMedicalExamId(Long medicalExamId) { this.medicalExamId =
	 * medicalExamId; }
	 */

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		MasInvestignationMapping other = (MasInvestignationMapping) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	 
	 
}