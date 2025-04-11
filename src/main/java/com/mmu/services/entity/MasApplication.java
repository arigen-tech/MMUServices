package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mmu.services.entity.MasApplication;



@Entity
@Table(name="MAS_APPLICATION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MasApplication implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3933939796458409072L;

	@Id
	/*@SequenceGenerator(name="MAS_APPLICATION_GENERATOR", sequenceName="MAS_APPLICATION_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_APPLICATION_GENERATOR")*/
	@Column(name="APP_ID")
	private String applicationId;
	
	@Column(name="NAME")
	private String applicationName;
	
	@Column(name="PARENT_ID")
	private String parentId;
	
	@Column(name="URL")
	private String url;
	
	@Column(name="ORDER_NO")
	
	private Long orderNo;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="app_sequence_no")
	private Long appSequenceNo;

	//bi-directional many-to-one association to TemplateApplication
	@OneToMany(mappedBy="masApplication")
	@JsonBackReference
	private List<TemplateApplication> templateApplications;
	

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<TemplateApplication> getTemplateApplications() {
		return templateApplications;
	}

	public void setTemplateApplications(List<TemplateApplication> templateApplications) {
		this.templateApplications = templateApplications;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	
	
	public Long getAppSequenceNo() {
		return appSequenceNo;
	}

	public void setAppSequenceNo(Long appSequenceNo) {
		this.appSequenceNo = appSequenceNo;
	}

	@Override
	public int hashCode() {
		return new Integer("" + appSequenceNo);
	}
	@Override
	public boolean equals(Object object) {
	    if (object == null) {
	        return false;
	      }
	      if (object == this) {
	        return true;
	      }
	      if (object.getClass() != this.getClass()) {
	        return false;
	      }

	      MasApplication masApplication = (MasApplication) object;
	      if (this.appSequenceNo.equals(masApplication.getAppSequenceNo())) {
	        return true;
	      } else {
	        return false;
	      }
	}
	 @Override
	  public String toString() {
		 return "[" + appSequenceNo + " : " + applicationName + "]";
	  }
	 public static final Comparator<MasApplication> orderNoComparator = new Comparator<MasApplication>() {
		    public int compare(MasApplication templateApplication, MasApplication templateApplication2) {
		      return templateApplication.getAppSequenceNo().compareTo(templateApplication2.getAppSequenceNo());
		     
		    }
		  };
		public static final Comparator<MasApplication> parentIdComparator = new Comparator<MasApplication>() {
		    public int compare(MasApplication templateApplication, MasApplication templateApplication2) {
		      return templateApplication.getParentId().compareTo(templateApplication2.getParentId());
		    }
		  };	
}
