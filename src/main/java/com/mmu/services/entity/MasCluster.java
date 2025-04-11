package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the mas_cluster database table.
 * 
 */
@Entity
@Table(name="mas_cluster")
@NamedQuery(name="MasCluster.findAll", query="SELECT m FROM MasCluster m")
public class MasCluster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_CLUSTER_CLUSTERID_GENERATOR", sequenceName="MAS_CLUSTER_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_CLUSTER_CLUSTERID_GENERATOR")
	@Column(name="cluster_id")
	private Long clusterId;

	@Column(name="cluster_code")
	private String clusterCode;

	@Column(name="cluster_name")
	private String clusterName;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	private String status;

	public MasCluster() {
	}

	public Long getClusterId() {
		return this.clusterId;
	}

	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}

	public String getClusterCode() {
		return this.clusterCode;
	}

	public void setClusterCode(String clusterCode) {
		this.clusterCode = clusterCode;
	}

	public String getClusterName() {
		return this.clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
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

}