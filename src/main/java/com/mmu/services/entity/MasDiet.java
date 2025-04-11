package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the MAS_DIET database table.
 * 
 */
@Entity
@Table(name="MAS_DIET")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedQuery(name="MasDiet.findAll", query="SELECT m FROM MasDiet m")
public class MasDiet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_DIET_SEQ", sequenceName="MAS_DIET_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_DIET_SEQ")
	@Column(name="DIET_ID")
	private long dietId;

	@Column(name="DIET_CODE")
	private String dietCode;

	@Column(name="DIET_NAME")
	private String dietName;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	public MasDiet() {
	}

	public long getDietId() {
		return this.dietId;
	}

	public void setDietId(long dietId) {
		this.dietId = dietId;
	}

	public String getDietCode() {
		return this.dietCode;
	}

	public void setDietCode(String dietCode) {
		this.dietCode = dietCode;
	}

	public String getDietName() {
		return this.dietName;
	}

	public void setDietName(String dietName) {
		this.dietName = dietName;
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