package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the cluster_district_mapping database table.
 * 
 */
@Entity
@Table(name="cluster_district_mapping")
@NamedQuery(name="ClusterDistrictMapping.findAll", query="SELECT c FROM ClusterDistrictMapping c")
public class ClusterDistrictMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CLUSTER_DISTRICT_MAPPING_CLUSTERDISTRICTMAPPINGID_GENERATOR", sequenceName="CLUSTER_DISTRICT_MAPPING_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CLUSTER_DISTRICT_MAPPING_CLUSTERDISTRICTMAPPINGID_GENERATOR")
	@Column(name="cluster_district_mapping_id")
	private Long clusterDistrictMappingId;

	@Column(name="cluster_id")
	private Long clusterId;

	@Column(name="district_id")
	private Long districtId;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	private String status;

	public ClusterDistrictMapping() {
	}

	public Long getClusterDistrictMappingId() {
		return this.clusterDistrictMappingId;
	}

	public void setClusterDistrictMappingId(Long clusterDistrictMappingId) {
		this.clusterDistrictMappingId = clusterDistrictMappingId;
	}

	public Long getClusterId() {
		return this.clusterId;
	}

	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}

	public Long getDistrictId() {
		return this.districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public Long getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
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

	//bi-directional many-to-one association to Patient
			@ManyToOne(fetch=FetchType.LAZY)
			@JoinColumn(name="district_id",nullable=false,insertable=false,updatable=false)
			private MasDistrict masDistrict;
			
			//bi-directional many-to-one association to Patient
			@ManyToOne(fetch=FetchType.LAZY)
			@JoinColumn(name="cluster_id",nullable=false,insertable=false,updatable=false)
			private MasCluster masCluster;

			

			public MasDistrict getMasDistrict() {
				return masDistrict;
			}

			public void setMasDistrict(MasDistrict masDistrict) {
				this.masDistrict = masDistrict;
			}

			public MasCluster getMasCluster() {
				return masCluster;
			}

			public void setMasCluster(MasCluster masCluster) {
				this.masCluster = masCluster;
			}
}