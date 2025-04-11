package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;


/**
 * The persistent class for the MAS_STORE_GROUP database table.
 * 
 */
@Entity
@Table(name="MAS_STORE_GROUP")
@NamedQuery(name="MasStoreGroup.findAll", query="SELECT m FROM MasStoreGroup m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@SequenceGenerator(name = "MAS_STORE_GROUP_SEQ", sequenceName = "MAS_STORE_GROUP_SEQ", allocationSize = 1)
public class MasStoreGroup implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id	
	//@GeneratedValue(strategy = GenerationType.AUTO, generator = "MAS_STORE_GROUP_SEQ")
	@Column(name="GROUP_ID")
	private long groupId;

	@Column(name="GROUP_CODE")
	private String groupCode;

	@Column(name="GROUP_NAME")
	private String groupName;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	public MasStoreGroup() {
	}

	public long getGroupId() {
		return this.groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getGroupCode() {
		return this.groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	
	public Long getLastChgBy() {
		return lastChgBy;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}