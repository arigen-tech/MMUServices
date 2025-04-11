package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * The persistent class for the MAS_RELATION database table.
 * 
 */


@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name = "MAS_RELATION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MasRelation implements Serializable {

	private static final long serialVersionUID = -2885106901173702634L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RELATION_ID", updatable = false, nullable = false)
	private Long relationId;

	@Column(name = "RELATION_CODE")
	private String relationCode;

	@Column(name = "RELATION_NAME")
	private String relationName;
	
	private String status;
	
	@Column(name = "last_chg_date")
	private Date lastChgDate;
	
	@Column(name = "last_chg_by")
	private Long lastChgBy;

	public Long getRelationId() {
		return relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}

	public String getRelationCode() {
		return relationCode;
	}

	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}
	
}