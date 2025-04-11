package com.mmu.services.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;



/**
 * The persistent class for the DG_MAS_COLLECTION database table.
 * 
 */
@Entity
@Table(name="DG_MAS_COLLECTION")
@NamedQuery(name="DgMasCollection.findAll", query="SELECT d FROM DgMasCollection d")
@SequenceGenerator(name="DG_MAS_COLLECTION_COLLECTIONID_GENERATOR", sequenceName="DG_MAS_COLLECTION_SEQ", allocationSize=1)
public class DgMasCollection implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="DG_MAS_COLLECTION_COLLECTIONID_GENERATOR")
	@Column(name="COLLECTION_ID")
	private long collectionId;

	@Column(name="COLLECTION_CODE")
	private String collectionCode;

	@Column(name="COLLECTION_NAME")
	private String collectionName;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;	

	private String status;

	public DgMasCollection() {
	}

	public long getCollectionId() {
		return this.collectionId;
	}

	public void setCollectionId(long collectionId) {
		this.collectionId = collectionId;
	}

	public String getCollectionCode() {
		return this.collectionCode;
	}

	public void setCollectionCode(String collectionCode) {
		this.collectionCode = collectionCode;
	}

	public String getCollectionName() {
		return this.collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}	

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	
	public Timestamp getLastChgDate() {
		return lastChgDate;
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