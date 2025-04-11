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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The persistent class for the MAS_COMMANDTYPE database table.
 * 
 */
@Entity
@Table(name = "MAS_COMMANDTYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name = "MAS_COMMANDTYPE_COMMANDTYPEID_GENERATOR", sequenceName = "MAS_COMMANDTYPE_SEQ1", allocationSize = 1)
public class MasCommandType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3560877860823663662L;


	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MAS_COMMANDTYPE_COMMANDTYPEID_GENERATOR")
	@Column(name = "COMMANDTYPE_ID")
	private long commandtypeId;

	@Column(name = "COMMANDTYPE_CODE")
	private String commandtypeCode;

	@Column(name = "COMMANDTYPE_NAME")
	private String commandtypeName;

	@Column(name = "LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY")
	private Users user;

	public MasCommandType() {
	}

	public long getCommandtypeId() {
		return this.commandtypeId;
	}

	public void setCommandtypeId(long commandtypeId) {
		this.commandtypeId = commandtypeId;
	}

	public String getCommandtypeCode() {
		return this.commandtypeCode;
	}

	public void setCommandtypeCode(String commandtypeCode) {
		this.commandtypeCode = commandtypeCode;
	}

	public String getCommandtypeName() {
		return this.commandtypeName;
	}

	public void setCommandtypeName(String commandtypeName) {
		this.commandtypeName = commandtypeName;
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

	
	

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}