package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;


/**
 * The persistent class for the APP_AUDIT_EQUIPMENT_DT database table.
 * 
 */
@Entity
@Table(name="APP_AUDIT_EQUIPMENT_DT")
@NamedQuery(name="AppAuditEquipmentDt.findAll", query="SELECT a FROM AppAuditEquipmentDt a")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="APP_AUDIT_EQUIPMENT_DT_SEQ", sequenceName="APP_AUDIT_EQUIPMENT_DT_SEQ",allocationSize=1)
public class AppAuditEquipmentDt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="APP_AUDIT_EQUIPMENT_DT_SEQ")
	@Column(name="AUDIT_ID")
	private long auditId;

	@Column(name="AUDIT_BY")
	private String auditBy;

	@Temporal(TemporalType.DATE)
	@Column(name="AUDIT_DATE")
	private Date auditDate;

	@Column(name="AUDIT_REMARKS")
	private String auditRemarks;

	@Column(name="BOARD_OUT")
	private String boardOut;

	//bi-directional many-to-one association to AppEquipmentDt
	@ManyToOne
	@JoinColumn(name="EQUIPMENT_DT_ID")
	private AppEquipmentDt appEquipmentDt;

	//bi-directional many-to-one association to AppEquipmentHd
	@ManyToOne
	@JoinColumn(name="EQUIPMENT_HD_ID")
	private AppEquipmentHd appEquipmentHd;

	public AppAuditEquipmentDt() {
	}

	public long getAuditId() {
		return this.auditId;
	}

	public void setAuditId(long auditId) {
		this.auditId = auditId;
	}

	public String getAuditBy() {
		return this.auditBy;
	}

	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}

	public Date getAuditDate() {
		return this.auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditRemarks() {
		return this.auditRemarks;
	}

	public void setAuditRemarks(String auditRemarks) {
		this.auditRemarks = auditRemarks;
	}

	public String getBoardOut() {
		return this.boardOut;
	}

	public void setBoardOut(String boardOut) {
		this.boardOut = boardOut;
	}

	public AppEquipmentDt getAppEquipmentDt() {
		return this.appEquipmentDt;
	}

	public void setAppEquipmentDt(AppEquipmentDt appEquipmentDt) {
		this.appEquipmentDt = appEquipmentDt;
	}

	public AppEquipmentHd getAppEquipmentHd() {
		return this.appEquipmentHd;
	}

	public void setAppEquipmentHd(AppEquipmentHd appEquipmentHd) {
		this.appEquipmentHd = appEquipmentHd;
	}

}