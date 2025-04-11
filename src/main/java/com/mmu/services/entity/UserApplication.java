package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
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
 * The persistent class for the USER_APPLICATIONS database table.
 * 
 */
@Entity
@Table(name = "USER_APPLICATIONS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name = "USER_APPLICATIONS_ID_GENERATOR", sequenceName = "USER_APPLICATIONS_SEQ",allocationSize=1)
public class UserApplication implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7934534210438937307L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "USER_APPLICATIONS_ID_GENERATOR")
	@Column(name = "USER_APP_ID")
	private long id;

	@Column(name = "USER_APP_NAME")
	private String appName;

	@Column(name = "LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "URL")
	private String url;

	// bi-directional many-to-one association to User
	@ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
	@JoinColumn(name = "LAST_CHG_BY", insertable = false, updatable = false)
	private Users users;
	
	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	public UserApplication() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
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

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	

}