package com.mmu.services.entity;

import java.io.Serializable;
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
 * The persistent class for the MAS_COMMAND database table.
 * 
 */
@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="VU_MAS_REGION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MasCommand implements Serializable {
	
	private static final long serialVersionUID = -6248054786135223665L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="REGION_ID",updatable = false, nullable = false)
	private long commandId;

	@Column(name="REGION_CODE")
	private String commandCode;

	@Column(name="REGION_NAME")
	private String commandName;

	

	@OneToMany(mappedBy="masCommand")
	@JsonBackReference
	private List<MasStation> masStation;
	
	
	public MasCommand() {
	}

	public long getCommandId() {
		return this.commandId;
	}

	public void setCommandId(long commandId) {
		this.commandId = commandId;
	}

	public String getCommandCode() {
		return this.commandCode;
	}

	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}

	public String getCommandName() {
		return this.commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	

	

}