package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the mas_head_type database table.
 * 
 */
@Entity
@Table(name="mas_head_type")
@NamedQuery(name="MasHeadType.findAll", query="SELECT m FROM MasHeadType m")
public class MasHeadType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_HEAD_TYPE_HEADTYPEID_GENERATOR", sequenceName="MAS_HEAD_TYPE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_HEAD_TYPE_HEADTYPEID_GENERATOR")
	@Column(name="head_type_id")
	private Long headTypeId;

	@Column(name="head_type_code")
	private String headTypeCode;

	@Column(name="head_type_name")
	private String headTypeName;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	private String status;

	public MasHeadType() {
	}

	public Long getHeadTypeId() {
		return this.headTypeId;
	}

	public void setHeadTypeId(Long headTypeId) {
		this.headTypeId = headTypeId;
	}

	public String getHeadTypeCode() {
		return this.headTypeCode;
	}

	public void setHeadTypeCode(String headTypeCode) {
		this.headTypeCode = headTypeCode;
	}

	public String getHeadTypeName() {
		return this.headTypeName;
	}

	public void setHeadTypeName(String headTypeName) {
		this.headTypeName = headTypeName;
	}

	public Long getLastChgBy() {
		return this.lastChgBy;
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