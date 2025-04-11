package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the CATEGORY_DUE database table.
 * 
 */
@Entity
@Table(name="CATEGORY_DUE")
@NamedQuery(name="CategoryDue.findAll", query="SELECT c FROM CategoryDue c")
public class CategoryDue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATEGORY_DUE_CATEGORYDUEID_GENERATOR", sequenceName="CATEGORY_DUE_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="CATEGORY_DUE_CATEGORYDUEID_GENERATOR")
	@Column(name="CATEGORY_DUE_ID")
	private long categoryDueId;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID")
	@JsonBackReference
	private MasEmployeeCategory masEmployeeCategory;
	
	/*
	 * @Column(name="CATEGORY_ID") private Long categoryId;
	 */

	@Column(name="FROM_MONTH")
	private Long fromMonth;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	@Column(name="TO_MONTH")
	private Long toMonth;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	public CategoryDue() {
	}

	public long getCategoryDueId() {
		return this.categoryDueId;
	}

	public void setCategoryDueId(long categoryDueId) {
		this.categoryDueId = categoryDueId;
	}

	/*
	 * public Long getCategoryId() { return this.categoryId; }
	 * 
	 * public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
	 */

	public Long getFromMonth() {
		return this.fromMonth;
	}

	public void setFromMonth(Long fromMonth) {
		this.fromMonth = fromMonth;
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

	public Long getToMonth() {
		return this.toMonth;
	}

	public void setToMonth(Long toMonth) {
		this.toMonth = toMonth;
	}
	
	public MasEmployeeCategory getMasEmployeeCategory() {
		return this.masEmployeeCategory;
	}

	public void setMasEmployeeCategory(MasEmployeeCategory masEmployeeCategory) {
		this.masEmployeeCategory = masEmployeeCategory;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}