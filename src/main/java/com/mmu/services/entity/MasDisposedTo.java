package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;


/**
 * The persistent class for the MAS_DISPOSED_TO database table.
 * 
 */
@Entity
@Table(name="MAS_DISPOSED_TO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedQuery(name="MasDisposedTo.findAll", query="SELECT m FROM MasDisposedTo m")
public class MasDisposedTo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_DISPOSED_TO_SEQ", sequenceName="MAS_DISPOSED_TO_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_DISPOSED_TO_SEQ")
	@Column(name="DISPOSED_TO_ID")
	private long disposedToId;

	@Column(name="DISPOSED_TO_CODE")
	private String disposedToCode;

	@Column(name="DISPOSED_TO_NAME")
	private String disposedToName;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	public MasDisposedTo() {
	}

	public long getDisposedToId() {
		return this.disposedToId;
	}

	public void setDisposedToId(long disposedToId) {
		this.disposedToId = disposedToId;
	}

	public String getDisposedToCode() {
		return this.disposedToCode;
	}

	public void setDisposedToCode(String disposedToCode) {
		this.disposedToCode = disposedToCode;
	}

	public String getDisposedToName() {
		return this.disposedToName;
	}

	public void setDisposedToName(String disposedToName) {
		this.disposedToName = disposedToName;
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