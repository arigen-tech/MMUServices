package com.mmu.services.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="HIERARCHY_DATA")

@SequenceGenerator(name="HIERARCHY_DATA_ID_GENERATOR", sequenceName="HIERARCHY_DATA_SEQ", allocationSize=1)
public class HierarchyData implements Serializable {
	

	private static final long serialVersionUID = 3253609914766418117L;
	
	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="HIERARCHY_DATA_ID_GENERATOR")
	@Column(name="ID")
	private long Id;
	
	@Column(name="HOSPITAL_ID")
	private long hospitalId;
	
	@Column(name="CODE")
	private String code;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="USER_HOSPITAL_ID")
	private Long userHospitalId;

	@Column(name="USER_ID")
	private Long userId;
	
	public HierarchyData() {
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUserHospitalId() {
		return userHospitalId;
	}

	public void setUserHospitalId(Long userHospitalId) {
		this.userHospitalId = userHospitalId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	
}