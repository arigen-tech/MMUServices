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

@Entity
@Table(name="MAS_ROLE_DSGN_MAPPING")
@SequenceGenerator(name = "MAS_ROLE_DSGN_MAPPING_SEQ", sequenceName = "MAS_ROLE_DSGN_MAPPING_SEQ", allocationSize=1)
public class RoleDesignation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3323954584722018583L;

		@Id		
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "MAS_ROLE_DSGN_MAPPING_SEQ")
		@Column(name="ROLE_DSGN_ID")
		public long roleDesignationId;
	
		/*@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name = "ROLE_ID")
		private MasRole masRole;*/
		
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name = "DESIGNATION_ID")
		private MasDesignation  masDesignation;
		
		@Column(name="ROLE_ID")
		private String roleId;
		
		/*@Column(name="DESIGNATION_ID")
		private String designationId;*/
		
		@Column(name="LOCATION_ID")
		private long locationId;
		
		@Column(name="STATUS")
		private String status;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="LAST_CHG_BY")
		private Users user;
		
		@Column(name="LAST_CHG_DATE")
		private Timestamp lastChgDate;

		public long getRoleDesignationId() {
			return roleDesignationId;
		}

		public void setRoleDesignationId(long roleDesignationId) {
			this.roleDesignationId = roleDesignationId;
		}

		
		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public long getLocationId() {
			return locationId;
		}

		public void setLocationId(long locationId) {
			this.locationId = locationId;
		}

		

		public Users getUser() {
			return user;
		}

		public void setUser(Users user) {
			this.user = user;
		}

		public Timestamp getLastChgDate() {
			return lastChgDate;
		}

		public void setLastChgDate(Timestamp lastChgDate) {
			this.lastChgDate = lastChgDate;
		}

		public String getRoleId() {
			return roleId;
		}

		public void setRoleId(String roleId) {
			this.roleId = roleId;
		}

		public MasDesignation getMasDesignation() {
			return masDesignation;
		}

		public void setMasDesignation(MasDesignation masDesignation) {
			this.masDesignation = masDesignation;
		}

				

}
