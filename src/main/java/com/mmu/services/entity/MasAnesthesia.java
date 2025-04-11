package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the MAS_ANESTHESIA database table.
 * 
 */
@Entity
@Table(name="MAS_ANESTHESIA")
@NamedQuery(name="MasAnesthesia.findAll", query="SELECT m FROM MasAnesthesia m")
@SequenceGenerator(name="MAS_ANESTHESIA_ANESTHESIAID_GENERATOR", sequenceName="SYNC_TABLE_SEQ", allocationSize=1)
public class MasAnesthesia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_ANESTHESIA_ANESTHESIAID_GENERATOR")
	@Column(name="ANESTHESIA_ID")
	private long anesthesiaId;

	@Column(name="ANESTHESIA_CODE")
	private String anesthesiaCode;

	@Column(name="ANESTHESIA_NAME")
	private String anesthesiaName;

	@Column(name="LAST_CHG_BY")
	private java.math.BigDecimal lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	public MasAnesthesia() {
	}

	public long getAnesthesiaId() {
		return this.anesthesiaId;
	}

	public void setAnesthesiaId(long anesthesiaId) {
		this.anesthesiaId = anesthesiaId;
	}

	public String getAnesthesiaCode() {
		return this.anesthesiaCode;
	}

	public void setAnesthesiaCode(String anesthesiaCode) {
		this.anesthesiaCode = anesthesiaCode;
	}

	public String getAnesthesiaName() {
		return this.anesthesiaName;
	}

	public void setAnesthesiaName(String anesthesiaName) {
		this.anesthesiaName = anesthesiaName;
	}

	public java.math.BigDecimal getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(java.math.BigDecimal lastChgBy) {
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