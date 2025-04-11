package com.mmu.services.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;


/**
 * The persistent class for the VU_MAS_STATION database table.
 * 
 */
@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="VU_MAS_STATION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MasStation implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8715657507359526092L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="STATION_ID",updatable = false, nullable = false)
	private long stationId;
	
	
	@Column(name="STATION_CODE")
	private String stationCode;
	

	@Column(name="STATION_NAME")
	private String stationName;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REGION_ID")
	@JsonBackReference
	private MasCommand masCommand;

	
	@OneToMany(mappedBy="masStation")
	@JsonBackReference
	private List<MasUnit> masUnit;
	
	
	
	public MasStation() {
	}

	

	public String getStationCode() {
		return this.stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public long getStationId() {
		return this.stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return this.stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}



	public MasCommand getMasCommand() {
		return masCommand;
	}



	public void setMasCommand(MasCommand masCommand) {
		this.masCommand = masCommand;
	}

	
	
}