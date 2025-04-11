package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*import model.JoinColumn;
import model.ManyToOne;
import model.MasDepartment;
import model.User;*/

@Entity
@Table(name="MAS_DOCTOR_MAPPING")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_DOCTOR_MAPPING_SEQ", sequenceName="MAS_DOCTOR_MAPPING_SEQ", allocationSize=1)
public class MasDoctorMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_DOCTOR_MAPPING_SEQ")
	@Column(name="DOCTOR_MAP_ID")
	private long doctorMapId;
		
		@Column(name="LAST_CHG_DATE")
		private Timestamp lastChgDate;

		@Column(name="LOCATION_ID")
		private Long locationId;
		

		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="DEPARTMENT_ID")
		private MasDepartment masDepartment;

		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="DOCTOR_ID")
		private Users doctorId;

		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="LAST_CHG_BY")
		private Users lastChgBy;
		
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name = "LOCATION_ID",nullable=false,insertable=false,updatable=false)
		@JsonBackReference
		private MasHospital masHospital;
		
		
		private String status;

		public MasDoctorMapping() {
		}

		public long getDoctorMapId() {
			return this.doctorMapId;
		}

		public void setDoctorMapId(long doctorMapId) {
			this.doctorMapId = doctorMapId;
		}

		public Timestamp getLastChgDate() {
			return this.lastChgDate;
		}

		public void setLastChgDate(Timestamp lastChgDate) {
			this.lastChgDate = lastChgDate;
		}

		public Long getLocationId() {
			return this.locationId;
		}

		public void setLocationId(Long locationId) {
			this.locationId = locationId;
		}

		public MasDepartment getMasDepartment() {
			return this.masDepartment;
		}

		public void setMasDepartment(MasDepartment masDepartment) {
			this.masDepartment = masDepartment;
		}

		public Users getDoctorId() {
			return this.doctorId;
		}

		public void setDoctorId(Users doctorId) {
			this.doctorId = doctorId;
		}

		public Users getLastChgBy() {
			return this.lastChgBy;
		}

		public void setLastChgBy(Users lastChgBy) {
			this.lastChgBy = lastChgBy;
		}
		public String getStatus() {
			return this.status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public MasHospital getMasHospital() {
			return masHospital;
		}

		public void setMasHospital(MasHospital masHospital) {
			this.masHospital = masHospital;
		}

	}