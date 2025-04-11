package com.mmu.services.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "city_mmu_invoice_mapping")
@SequenceGenerator(name="city_mmu_invoice_mapping_seq", sequenceName="city_mmu_invoice_mapping_seq", allocationSize=1)
public class CityMmuInvoiceMapping implements Serializable {

    private static final long serialVersionUID = 984527268265139443L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator ="city_mmu_invoice_mapping_seq")
    @Column(name = "city_mmu_invoice_mapping_id")
    private Long cityMmuInvoiceMappingId;

    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "mmu_id")
    private Long mmuId;

    @Column(name = "status")
    private String status;

    @Column(name = "last_chg_by")
    private Long lastChangeBy;

    @Column(name = "last_chg_date")
    private Date lastChangeDate;
    
    @Column(name = "phase_name")
    private String phaseName;
    
    @Column(name = "phase_id")
    private Long phaseId;
    
    
  //bi-directional many-to-one association to Patient
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="phase_id",nullable=false,insertable=false,updatable=false)
    @JsonBackReference
    private MasPhase masPhase;
    
    
    //bi-directional many-to-one association to Patient
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="city_id",nullable=false,insertable=false,updatable=false)
    @JsonBackReference
    private MasCity masCity;

    //bi-directional many-to-one association to Patient
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="mmu_id",nullable=false,insertable=false,updatable=false)
    @JsonBackReference
    private MasMMU masMmu;

    public Long getCityMmuInvoiceMappingId() {
        return cityMmuInvoiceMappingId;
    }

    public void setCityMmuInvoiceMappingId(Long cityMmuInvoiceMappingId) {
        this.cityMmuInvoiceMappingId = cityMmuInvoiceMappingId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getMmuId() {
        return mmuId;
    }

    public void setMmuId(Long mmuId) {
        this.mmuId = mmuId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getLastChangeBy() {
        return lastChangeBy;
    }

    public void setLastChangeBy(Long lastChangeBy) {
        this.lastChangeBy = lastChangeBy;
    }

    public Date getLastChangeDate() {
        return lastChangeDate;
    }

    public void setLastChangeDate(Date lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

	public MasCity getMasCity() {
		return masCity;
	}

	public void setMasCity(MasCity masCity) {
		this.masCity = masCity;
	}

	public MasMMU getMasMmu() {
		return masMmu;
	}

	public void setMasMmu(MasMMU masMmu) {
		this.masMmu = masMmu;
	}
    
	public static final Comparator<CityMmuInvoiceMapping> orderNoComparator = new Comparator<CityMmuInvoiceMapping>() {
	    public int compare(CityMmuInvoiceMapping o1, CityMmuInvoiceMapping o2) {
	    	  
	    	 String o1StringPart =o1.getMasMmu().getMmuName().replaceAll("\\d", "");
             String o2StringPart = o2.getMasMmu().getMmuName().replaceAll("\\d", "");


             if(o1StringPart.equalsIgnoreCase(o2StringPart))
             {
                 return extractInt(o1.getMasMmu().getMmuName()) - extractInt(o2.getMasMmu().getMmuName());
             }
             return o1.getMasMmu().getMmuName().compareTo(o2.getMasMmu().getMmuName());
             
	    	//return extractInt(o1.getMmuName()) - extractInt(o2.getMmuName());
	    } 
	     int extractInt(String s) {
	    	
		   String num = s.replaceAll("\\D", "");
	        // return 0 if no digits found
	        return num.isEmpty() ? 0 : Integer.parseInt(num);
	    }
	  };

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public Long getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	public MasPhase getMasPhase() {
		return masPhase;
	}

	public void setMasPhase(MasPhase masPhase) {
		this.masPhase = masPhase;
	}

	
 
		   	  
}
