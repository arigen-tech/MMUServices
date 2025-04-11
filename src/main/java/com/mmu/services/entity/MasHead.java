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
@Table(name = "mas_head_type")
@SequenceGenerator(name="MAS_HEAD_TYPE_GENERATOR", sequenceName="MAS_HEAD_TYPE_seq", allocationSize=1)
public class MasHead implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,generator ="MAS_HEAD_TYPE_GENERATOR")
	@Column(name = "head_type_id")
	private Long headTypeId;
	
	@Column(name = "head_type_code")
	private String headTypeCode;
	
	@Column(name = "head_type_name")
	public String headTypeName;
	
	 
	
	private String status;
	
	@Column(name = "last_chg_by")
	private Long lastChangeBy;
	
	@Column(name = "last_chg_date")
	private Date lastChangeDate;

	public Long getHeadTypeId() {
		return headTypeId;
	}

	public void setHeadTypeId(Long headTypeId) {
		this.headTypeId = headTypeId;
	}

	public String getHeadTypeCode() {
		return headTypeCode;
	}

	public void setHeadTypeCode(String headTypeCode) {
		this.headTypeCode = headTypeCode;
	}

	public String getHeadTypeName() {
		return headTypeName;
	}

	public void setHeadTypeName(String headTypeName) {
		this.headTypeName = headTypeName;
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
