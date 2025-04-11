package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/**
 * The persistent class for the MAS_MAIN_CHARGECODE database table.
 * 
 */
@Entity
@Table(name="MAS_MAIN_CHARGECODE")
@NamedQuery(name="MasMainChargecode.findAll", query="SELECT m FROM MasMainChargecode m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_MAIN_CHARGECODE_MAINCHARGECODEID_GENERATOR", sequenceName="MAS_MAIN_CHARGECODE_SEQ", allocationSize=1)
public class MasMainChargecode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_MAIN_CHARGECODE_MAINCHARGECODEID_GENERATOR")
	@Column(name="MAIN_CHARGECODE_ID")
	private Long mainChargecodeId;



	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="MAIN_CHARGECODE_CODE")
	private String mainChargecodeCode;

	@Column(name="MAIN_CHARGECODE_NAME")
	private String mainChargecodeName;

	private String status;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;

	public MasMainChargecode() {
	}

	public Long getMainChargecodeId() {
		return this.mainChargecodeId;
	}

	public void setMainChargecodeId(Long mainChargecodeId) {
		this.mainChargecodeId = mainChargecodeId;
	}

	

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getMainChargecodeCode() {
		return this.mainChargecodeCode;
	}

	public void setMainChargecodeCode(String mainChargecodeCode) {
		this.mainChargecodeCode = mainChargecodeCode;
	}

	public String getMainChargecodeName() {
		return this.mainChargecodeName;
	}

	public void setMainChargecodeName(String mainChargecodeName) {
		this.mainChargecodeName = mainChargecodeName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MasDepartment getMasDepartment() {
		return masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}
	
	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}