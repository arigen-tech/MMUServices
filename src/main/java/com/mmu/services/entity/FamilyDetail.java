package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the FAMILY_DETAILS database table.
 * 
 */
@Entity
@Table(name="FAMILY_DETAILS")
@NamedQuery(name="FamilyDetail.findAll", query="SELECT f FROM FamilyDetail f")
@SequenceGenerator(name="FAMILY_DETAILS_SEQ", sequenceName="FAMILY_DETAILS_SEQ", allocationSize = 1)
public class FamilyDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="FAMILY_DETAILS_SEQ")
	@Column(name="FAMILY_ID")
	private Long familyId;

	private Long age;

	@Column(name="DEPENDENT_POR_NO")
	private String dependentPorNo;

	@Temporal(TemporalType.DATE)
	private Date dob;

	@Column(name="LEAVE_APPLICATION_ID")
	private Long leaveApplicationId;

	@Column(name="SELECT_FAMILY")
	private String selectFamily;

	@Column(name="VISIT_ID")
	private Long visitId;
	
	public FamilyDetail() {
	}

 

 

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}
 
	public Long getFamilyId() {
		return familyId;
	}





	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}





	public String getDependentPorNo() {
		return this.dependentPorNo;
	}

	public void setDependentPorNo(String dependentPorNo) {
		this.dependentPorNo = dependentPorNo;
	}

	public Date getDob() {
		return this.dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

 
	public Long getLeaveApplicationId() {
		return leaveApplicationId;
	}

	public void setLeaveApplicationId(Long leaveApplicationId) {
		this.leaveApplicationId = leaveApplicationId;
	}

	public String getSelectFamily() {
		return this.selectFamily;
	}

	public void setSelectFamily(String selectFamily) {
		this.selectFamily = selectFamily;
	}

}