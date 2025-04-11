package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the MAS_SERVICE_TYPE database table.
 * 
 */
@Entity
@Table(name="MAS_SERVICE_TYPE")
@NamedQuery(name="MasServiceType.findAll", query="SELECT m FROM MasServiceType m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_SERVICE_TYPE_SERVICETYPEID_GENERATOR", sequenceName="MAS_SERVICE_TYPE_SEQ",allocationSize=1)
public class MasServiceType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6039151152468567078L;

	@Id
	@SequenceGenerator(name="MAS_SERVICE_TYPE_SERVICETYPEID_GENERATOR", sequenceName="MAS_SERVICE_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_SERVICE_TYPE_SERVICETYPEID_GENERATOR")
	@Column(name="SERVICE_TYPE_ID")
	private long serviceTypeId;

	/*
	 * @Column(name="LAST_CHG_BY") private String lastChgBy;
	 */
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;
	
	//@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	
	@Column(name="SERVICE_TYPE_CODE")
	private String serviceTypeCode;

	@Column(name="SERVICE_TYPE_NAME")
	private String serviceTypeName;

	private String status;

	

	public MasServiceType() {
	}

	public long getServiceTypeId() {
		return this.serviceTypeId;
	}

	public void setServiceTypeId(long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}



	public String getServiceTypeCode() {
		return this.serviceTypeCode;
	}

	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public String getServiceTypeName() {
		return this.serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}