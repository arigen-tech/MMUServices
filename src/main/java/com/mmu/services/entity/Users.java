package com.mmu.services.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the USERS database table.
 * 
 */
@Entity
@Table(name="USERS")
@NamedQuery(name="Users.findAll", query="SELECT u FROM Users u")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="USERS_SEQ", sequenceName="USERS_SEQ",allocationSize=1)
public class Users implements Serializable {
	private static final long serialVersionUID = -863915371702584702L;

	
	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="USERS_SEQ")
	@Column(name="user_id")
	private Long userId;

	@Column(name="admin_flag")
	private String adminFlag;

	@Column(name="administrative_sex_id")
	private Long administrativeSexId;

	@Column(name="city_id")
	private String cityId;

	@Column(name="district_id")
	private String districtId;

	@Temporal(TemporalType.DATE)
	private Date dob;

	@Column(name="email_address")
	private String emailAddress;


	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	@Column(name="level_of_user")
	private String levelOfUser;

	@Column(name="login_name")
	private String loginName;

	@Column(name="mmu_id")
	private String mmuId;

	@Column(name="mobile_no")
	private String mobileNo;

	private String password;

	@Column(name="reset_pwd_count")
	private Long resetPwdCount;

	@Column(name="role_id")
	private String roleId;

	@Column(name="state_id")
	private String stateId;

	@Column(name="user_flag")
	private Long userFlag;

	@Column(name="user_name")
	private String userName;

	@Column(name="user_type_id")
	private Long userTypeId;
	
	@Column(name="employee_id")
	private Long employeeIdValue;

	@Column(name="signature_file_name")
	private String signatureFileName;
	
	@Column(name="vendor_id")
	private String vendorId;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="employee_id",nullable=false,insertable=false,updatable=false)
	private EmployeeRegistration employeeId;

	public void User() {
	}

	public String getSignatureFileName() {
		return signatureFileName;
	}

	public void setSignatureFileName(String signatureFileName) {
		this.signatureFileName = signatureFileName;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAdminFlag() {
		return this.adminFlag;
	}

	public void setAdminFlag(String adminFlag) {
		this.adminFlag = adminFlag;
	}

	public Long getAdministrativeSexId() {
		return this.administrativeSexId;
	}

	public void setAdministrativeSexId(Long administrativeSexId) {
		this.administrativeSexId = administrativeSexId;
	}

	public String getCityId() {
		return this.cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getDistrictId() {
		return this.districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public Date getDob() {
		return this.dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getLevelOfUser() {
		return this.levelOfUser;
	}

	public void setLevelOfUser(String levelOfUser) {
		this.levelOfUser = levelOfUser;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getMmuId() {
		return this.mmuId;
	}

	public void setMmuId(String mmuId) {
		this.mmuId = mmuId;
	}

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getResetPwdCount() {
		return this.resetPwdCount;
	}

	public void setResetPwdCount(Long resetPwdCount) {
		this.resetPwdCount = resetPwdCount;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getStateId() {
		return this.stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public Long getUserFlag() {
		return this.userFlag;
	}

	public void setUserFlag(Long userFlag) {
		this.userFlag = userFlag;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserTypeId() {
		return this.userTypeId;
	}

	public void setUserTypeId(Long userTypeId) {
		this.userTypeId = userTypeId;
	}
	
	//bi-directional many-to-one association to MasEmployee
	@OneToMany(fetch=FetchType.LAZY)
		@JsonBackReference
		@JoinColumn(name="MMU_ID")	
		private List<MasMMU> masMMU;
		
		
		@OneToMany(fetch=FetchType.LAZY)
		@JsonBackReference
		@JoinColumn(name="ROLE_ID")	
		private List<MasRole> masRole;
		
		@OneToMany(fetch=FetchType.LAZY)
		@JsonBackReference
		@JoinColumn(name="DISTRICT_ID")	
		private List<MasDistrict> masDist;
		
		@OneToMany(fetch=FetchType.LAZY)
		@JsonBackReference
		@JoinColumn(name="CITY_ID")	
		private List<MasCity> masCity;

		public List<MasMMU> getMasMMU() {
			return masMMU;
		}

		public void setMasMMU(List<MasMMU> masMMU) {
			this.masMMU = masMMU;
		}

		public List<MasRole> getMasRole() {
			return masRole;
		}

		public void setMasRole(List<MasRole> masRole) {
			this.masRole = masRole;
		}

		private List<MasDistrict> getMasDist() {
			return masDist;
		}

		private void setMasDist(List<MasDistrict> masDist) {
			this.masDist = masDist;
		}

		private List<MasCity> getMasCity() {
			return masCity;
		}

		private void setMasCity(List<MasCity> masCity) {
			this.masCity = masCity;
		}

		public EmployeeRegistration getEmployeeId() {
			return employeeId;
		}

		public void setEmployeeId(EmployeeRegistration employeeId) {
			this.employeeId = employeeId;
		}
		
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="user_type_id",nullable=false,insertable=false,updatable=false)
		private MasUserType masUserType;

		public MasUserType getMasUserType() {
			return masUserType;
		}

		public void setMasUserType(MasUserType masUserType) {
			this.masUserType = masUserType;
		}

		public Long getEmployeeIdValue() {
			return employeeIdValue;
		}

		public void setEmployeeIdValue(Long employeeIdValue) {
			this.employeeIdValue = employeeIdValue;
		}

		public String getVendorId() {
			return vendorId;
		}

		public void setVendorId(String vendorId) {
			this.vendorId = vendorId;
		}

		@Override
		public String toString() {
			return "Users [userId=" + userId + ", adminFlag=" + adminFlag + ", administrativeSexId="
					+ administrativeSexId + ", cityId=" + cityId + ", districtId=" + districtId + ", dob=" + dob
					+ ", emailAddress=" + emailAddress + ", lastChgDate=" + lastChgDate + ", levelOfUser=" + levelOfUser
					+ ", loginName=" + loginName + ", mmuId=" + mmuId + ", mobileNo=" + mobileNo + ", password="
					+ password + ", resetPwdCount=" + resetPwdCount + ", roleId=" + roleId + ", stateId=" + stateId
					+ ", userFlag=" + userFlag + ", userName=" + userName + ", userTypeId=" + userTypeId
					+ ", employeeIdValue=" + employeeIdValue + ", signatureFileName=" + signatureFileName
					+ ", vendorId=" + vendorId + ", employeeId=" + employeeId + ", masMMU=" + masMMU + ", masRole="
					+ masRole + ", masDist=" + masDist + ", masCity=" + masCity + ", masUserType=" + masUserType + "]";
		}
		
		
		
		
		
		

	
}