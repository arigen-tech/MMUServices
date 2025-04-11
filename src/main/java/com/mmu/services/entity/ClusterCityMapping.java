package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the cluster_city_mapping database table.
 * 
 */
@Entity
@Table(name="cluster_city_mapping")
@NamedQuery(name="ClusterCityMapping.findAll", query="SELECT c FROM ClusterCityMapping c")
public class ClusterCityMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CLUSTER_CITY_MAPPING_CLUSTERCITYMAPPINGID_GENERATOR", sequenceName="CLUSTER_CITY_MAPPING_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CLUSTER_CITY_MAPPING_CLUSTERCITYMAPPINGID_GENERATOR")
	@Column(name="cluster_city_mapping_id")
	private Long clusterCityMappingId;

	@Column(name="city_id")
	private Long cityId;

	@Column(name="cluster_id")
	private Long clusterId;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	private String status;

	public ClusterCityMapping() {
	}

	public Long getClusterCityMappingId() {
		return this.clusterCityMappingId;
	}

	public void setClusterCityMappingId(Long clusterCityMappingId) {
		this.clusterCityMappingId = clusterCityMappingId;
	}

	public Long getCityId() {
		return this.cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getClusterId() {
		return this.clusterId;
	}

	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
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
		@JoinColumn(name="city_id",nullable=false,insertable=false,updatable=false)
		private MasCity masCity;
		
		//bi-directional many-to-one association to Patient
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="cluster_id",nullable=false,insertable=false,updatable=false)
		private MasCluster masCluster;

		public MasCity getMasCity() {
			return masCity;
		}

		public void setMasCity(MasCity masCity) {
			this.masCity = masCity;
		}

		public MasCluster getMasCluster() {
			return masCluster;
		}

		public void setMasCluster(MasCluster masCluster) {
			this.masCluster = masCluster;
		}
		
		

}