package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the MAS_BED_STATUS database table.
 * 
 */
@Entity
@Table(name="MAS_BED_STATUS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedQuery(name="MasBedStatus.findAll", query="SELECT m FROM MasBedStatus m")
public class MasBedStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_BED_STATUS_BEDSTATUSID_GENERATOR", sequenceName="MAS_BED_STATUS_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_BED_STATUS_BEDSTATUSID_GENERATOR")
	@Column(name="BED_STATUS_ID")
	private long bedStatusId;

	@Column(name="BED_STATUS_CODE")
	private String bedStatusCode;

	@Column(name="BED_STATUS_NAME")
	private String bedStatusName;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	//bi-directional many-to-one association to MasBed
	@OneToMany(mappedBy="masBedStatus")
	private List<MasBed> masBeds;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	public MasBedStatus() {
	}

	public long getBedStatusId() {
		return this.bedStatusId;
	}

	public void setBedStatusId(long bedStatusId) {
		this.bedStatusId = bedStatusId;
	}

	public String getBedStatusCode() {
		return this.bedStatusCode;
	}

	public void setBedStatusCode(String bedStatusCode) {
		this.bedStatusCode = bedStatusCode;
	}

	public String getBedStatusName() {
		return this.bedStatusName;
	}

	public void setBedStatusName(String bedStatusName) {
		this.bedStatusName = bedStatusName;
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

	public List<MasBed> getMasBeds() {
		return this.masBeds;
	}

	public void setMasBeds(List<MasBed> masBeds) {
		this.masBeds = masBeds;
	}

	public MasBed addMasBed(MasBed masBed) {
		getMasBeds().add(masBed);
		masBed.setMasBedStatus(this);

		return masBed;
	}

	public MasBed removeMasBed(MasBed masBed) {
		getMasBeds().remove(masBed);
		masBed.setMasBedStatus(null);

		return masBed;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}